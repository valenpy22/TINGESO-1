FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} MyWebApp.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "/MyWebApp.jar"]