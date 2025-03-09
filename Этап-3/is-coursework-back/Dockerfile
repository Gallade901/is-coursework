FROM openjdk:17

COPY target/is-coursework-back-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx1G", "-jar", "app.jar"]