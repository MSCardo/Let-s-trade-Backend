FROM openjdk
ADD /build/libs/trade-backend-0.0.1-SNAPSHOT.jar trade-backend-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "trade-backend-0.0.1-SNAPSHOT.jar"]