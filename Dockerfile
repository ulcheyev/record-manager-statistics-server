FROM eclipse-temurin:21-jre

RUN groupadd --system spring && \
    useradd --system --gid spring --create-home --home-dir /app spring

WORKDIR /app

COPY --chown=spring:spring target/record-manager-statistics-server.jar app.jar

USER spring

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90.0", "-jar", "app.jar"]