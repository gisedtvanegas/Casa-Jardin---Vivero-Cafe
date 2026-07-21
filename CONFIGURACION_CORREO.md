# Configuración del correo de recuperación

El flujo de recuperación usa Gmail y SMTP seguro (puerto 587). No se guardan credenciales dentro del proyecto.

En la configuración de inicio de GlassFish agregue estas propiedades de JVM:

```
-Dcasa.jardin.email=cuenta@gmail.com
-Dcasa.jardin.email.password=contrasena_de_aplicacion_de_google
```

También se pueden definir como variables de entorno del servidor:

```
CASA_JARDIN_EMAIL
CASA_JARDIN_EMAIL_PASSWORD
```

La contraseña debe ser una **contraseña de aplicación** de Google, no la contraseña habitual de Gmail. Para crearla, active la verificación en dos pasos en la cuenta remitente y genere una contraseña de aplicación en la seguridad de la cuenta de Google. Después reinicie GlassFish.

El código enviado al usuario tiene cinco dígitos, queda ligado a la sesión del navegador y vence a los 15 minutos.
