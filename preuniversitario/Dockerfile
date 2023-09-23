FROM openjdk:17
ARG JAR_FILE=target/MyWebApp.jar
COPY ${JAR_FILE} MyWebApp.jar
ENTRYPOINT ["java", "-jar", "/MyWebApp.jar"]