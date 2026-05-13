# ============================================
# ETAPA 1: Construcción (Builder)
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# ============================================
# ETAPA 2: Ejecución (Runtime)
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 8080

# ✅ Las variables se pasan en tiempo de ejecución desde Render
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} app.jar"]