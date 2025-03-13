FROM openjdk:17-alpine
EXPOSE 8082
WORKDIR /usr/src
ADD target/bookmarkfile.jar /usr/src/bookmarkfile.jar
ENTRYPOINT ["java", "-jar", "/usr/src/bookmarkfile.jar"]