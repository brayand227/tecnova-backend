# ============================================
# ETAPA 1: Construcción (Builder)
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el archivo de configuración de Maven primero (para cachear dependencias)
COPY pom.xml .

# Descargar dependencias (esto se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar la aplicación y generar el JAR
RUN mvn clean package -DskipTests

# ============================================
# ETAPA 2: Ejecución (Runtime)
# ============================================
FROM eclipse-temurin:21-jre-alpine

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Crear un usuario no root para seguridad (opcional pero recomendado)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Exponer el puerto que usará la aplicación
EXPOSE 8080

# Variables de entorno que Render puede sobreescribir
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

# Comando para ejecutar la aplicación
# Usamos exec-form para mejor manejo de señales
ENTRYPOINT ["java", "-jar", "-Dserver.port=${PORT}", "app.jar"]