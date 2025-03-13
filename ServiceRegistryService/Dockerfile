FROM openjdk:17-alpine
EXPOSE 8761
WORKDIR /usr/src
ADD target/serviceregistry.jar /usr/src/serviceregistry.jar
ENTRYPOINT ["java", "-jar", "/usr/src/serviceregistry.jar"]