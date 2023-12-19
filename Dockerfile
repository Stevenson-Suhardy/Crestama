FROM openjdk:17-jdk-alpine
COPY ./target/crestama-website-0.0.1-SNAPSHOT.jar crestama-website-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "crestama-website-0.0.1-SNAPSHOT.jar"]