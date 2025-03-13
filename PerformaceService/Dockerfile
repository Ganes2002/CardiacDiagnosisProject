FROM openjdk:17-alpine
EXPOSE 8084
WORKDIR /usr/src
ADD target/diagnosisfile.jar /usr/src/diagnosisfile.jar
ENTRYPOINT ["java", "-jar", "/usr/src/diagnosisfile.jar"]