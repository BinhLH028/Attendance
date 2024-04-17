FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
COPY --from=build /app/target/AttendanceApplication-0.0.1-SNAPSHOT.jar AttendanceApplication.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","AttendanceApplication.jar"]