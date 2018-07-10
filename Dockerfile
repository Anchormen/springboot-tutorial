# image
FROM openjdk:8

WORKDIR /app

ADD ./target/usermanager-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "usermanager-1.0-SNAPSHOT.jar"]
