package Controlador;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/** Envia los correos de recuperacion usando una cuenta Gmail con contrasena de aplicacion. */
public class CorreoRecuperacion {

    public void enviarCodigo(String destinatario, String codigo) throws Exception {
        final String correoRemitente = valor("CASA_JARDIN_EMAIL", "casa.jardin.email");
        // Gmail muestra la clave de aplicacion agrupada visualmente. Para SMTP se
        // debe enviar sin espacios ni saltos de linea.
        final String claveAplicacion = limpiarClave(valor("CASA_JARDIN_EMAIL_PASSWORD", "casa.jardin.email.password"));

        if (correoRemitente == null || claveAplicacion == null) {
            throw new IllegalStateException("Falta configurar CASA_JARDIN_EMAIL y CASA_JARDIN_EMAIL_PASSWORD en el servidor.");
        }

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        // GlassFish puede tener un almacen de certificados desactualizado. Se limita
        // la confianza TLS al servidor SMTP oficial de Gmail, sin quitar el cifrado.
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoRemitente, claveAplicacion);
            }
        });

        MimeMessage mensaje = new MimeMessage(sesion);
        mensaje.setFrom(new InternetAddress(correoRemitente, "Casa y Jardin"));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject("Codigo de recuperacion - Casa y Jardin", "UTF-8");
        mensaje.setText("Hola!! Te saludamos con cariño desde Casa y Jardín\n"
                + "Este es tu código de recuperación " + codigo + "\n\n"
                + "Te invitamos a seguir utilizando nuestros servicios", "UTF-8");
        Transport.send(mensaje);
    }

    private String valor(String variableEntorno, String propiedadSistema) {
        String valor = System.getenv(variableEntorno);
        return valor != null && !valor.trim().isEmpty() ? valor : System.getProperty(propiedadSistema);
    }

    private String limpiarClave(String clave) {
        return clave == null ? null : clave.replaceAll("\\s", "");
    }
}
