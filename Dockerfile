FROM tomcat:10.1-jdk17

# Actualiza certificados
RUN apt-get update && \
    apt-get install -y --reinstall ca-certificates && \
    update-ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Limpia apps por defecto
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia tu WAR y lo renombra a ROOT.war
COPY Cafeteriatalleres.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
