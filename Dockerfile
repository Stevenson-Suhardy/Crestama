FROM openjdk:17

COPY target/crestama-website-0.0.1-SNAPSHOT.jar crestama.jar

ENTRYPOINT ["java", "-jar", "/crestama.jar"]