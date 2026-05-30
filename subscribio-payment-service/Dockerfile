FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar payment-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "payment-service.jar"]