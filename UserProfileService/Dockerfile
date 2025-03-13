FROM openjdk:17-alpine
EXPOSE 8083
WORKDIR /usr/src
ADD target/userservice.jar /usr/src/userservice.jar
ENTRYPOINT ["java", "-jar", "/usr/src/userservice.jar"]