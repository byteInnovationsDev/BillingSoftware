FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy all project files
COPY . .

# 🔧 Ensure mvnw has execute permission
RUN chmod +x ./mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Run the final JAR
CMD ["java", "-jar", "target/BillingSoftware-0.0.1-SNAPSHOT.jar"]
