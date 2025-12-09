# Etapa 1: construir el JAR con Maven
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace/app

# Copiamos los archivos de Maven
COPY pom.xml .
COPY src ./src

# Compilamos el proyecto (salta tests para ir más rápido)
RUN mvn -B clean package -DskipTests

# Etapa 2: imagen liviana solo con el JAR
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /workspace/app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]