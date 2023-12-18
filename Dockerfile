FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/crestama-web-application-jar-with-dependencies.jar crestama.jar
ENTRYPOINT ["java", "-jar", "/crestama.jar"]