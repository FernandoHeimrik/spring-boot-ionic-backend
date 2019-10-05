FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD target/spring-boot-backend-docker.jar spring-boot-backend-docker.jar
ENTRYPOINT ["java","-jar","/spring-boot-backend-docker.jar"]