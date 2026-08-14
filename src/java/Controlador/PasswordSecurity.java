package Controlador;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Utilidades para almacenar y comprobar contraseñas sin conservar texto plano. */
public final class PasswordSecurity {
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACIONES = 210_000;
    private static final int TAMANO_SAL = 16;
    private static final int TAMANO_HASH_BITS = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private PasswordSecurity() { }

    public static String hash(String clave) {
        if (clave == null) throw new IllegalArgumentException("La contraseña no puede ser nula.");
        byte[] sal = new byte[TAMANO_SAL];
        ALEATORIO.nextBytes(sal);
        return "pbkdf2$" + ITERACIONES + "$" + codificar(sal) + "$"
                + codificar(derivar(clave.toCharArray(), sal, ITERACIONES));
    }

    public static boolean verificar(String clave, String almacenada) {
        if (clave == null || almacenada == null || !esHash(almacenada)) return false;
        try {
            String[] partes = almacenada.split("\\$", -1);
            if (partes.length != 4) return false;
            int iteraciones = Integer.parseInt(partes[1]);
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] esperada = Base64.getDecoder().decode(partes[3]);
            byte[] calculada = derivar(clave.toCharArray(), sal, iteraciones);
            return MessageDigest.isEqual(esperada, calculada);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean esHash(String valor) {
        return valor != null && valor.startsWith("pbkdf2$");
    }

    private static byte[] derivar(char[] clave, byte[] sal, int iteraciones) {
        try {
            PBEKeySpec especificacion = new PBEKeySpec(clave, sal, iteraciones, TAMANO_HASH_BITS);
            try {
                return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(especificacion).getEncoded();
            } finally {
                especificacion.clearPassword();
            }
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No fue posible proteger la contraseña.", e);
        }
    }

    private static String codificar(byte[] valor) {
        return Base64.getEncoder().withoutPadding().encodeToString(valor);
    }
}
