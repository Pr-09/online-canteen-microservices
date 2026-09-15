FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven

RUN mvn clean package -DskipTests

EXPOSE 8761

CMD ["sh", "-c", "java -jar target/*.jar"]