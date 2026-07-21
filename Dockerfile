# Imagen con Tomcat 10 + JDK 17 (misma versión que ya usa Railway según tu log)
FROM tomcat:10.1-jdk17-temurin

# Limpia los webapps de ejemplo que trae Tomcat por defecto
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia el WAR ya compilado y lo renombra a ROOT.war
# para que quede disponible directamente en "/" en vez de "/Cafeteriatalleres"
COPY dist/Cafeteriatalleres.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
