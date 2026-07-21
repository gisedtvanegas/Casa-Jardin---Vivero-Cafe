# Imagen con Tomcat 10 + JDK 17 (misma versión que ya usa Railway según tu log)
FROM tomcat:10.1-jdk17-temurin

# Limpia los webapps de ejemplo que trae Tomcat por defecto
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia el WAR ya compilado y lo descomprime como carpeta ROOT
# (para poder agregarle un jar que le falta antes de que Tomcat lo despliegue)
COPY dist/Cafeteriatalleres.war /usr/local/tomcat/webapps/ROOT.war
RUN mkdir -p /usr/local/tomcat/webapps/ROOT \
    && cd /usr/local/tomcat/webapps/ROOT \
    && jar -xf ../ROOT.war \
    && rm ../ROOT.war

# jakarta.mail no viene incluido en el WAR: en tu PC lo tomaba de los
# modulos de GlassFish, pero Tomcat no lo trae. Se agrega directo a WEB-INF/lib.
ADD https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/2.0.1/jakarta.mail-2.0.1.jar \
    /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/jakarta.mail.jar

EXPOSE 8080

CMD ["catalina.sh", "run"]
