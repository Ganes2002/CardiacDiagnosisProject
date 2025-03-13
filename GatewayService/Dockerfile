FROM openjdk:17-alpine
EXPOSE 8999
WORKDIR /usr/src
ADD target/gatewayservice.jar /usr/src/gatewayservice.jar
ENTRYPOINT ["java", "-jar", "/usr/src/gatewayservice.jar"]