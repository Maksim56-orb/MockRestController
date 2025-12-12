FROM eclipse-temurin:21-jdk-alpine

VOLUME /tmp

# Скачиваем JMX Exporter
ADD https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.17.2/jmx_prometheus_javaagent-0.17.2.jar /opt/jmx_exporter.jar

# Копируем конфиг JMX Exporter
COPY consumer_jmx.yml /opt/consumer_jmx.yml

# Копируем Spring Boot JAR
COPY build/libs/*.jar /app.jar

# Открываем порты
EXPOSE 9001      # Spring Boot
EXPOSE 9404      # JMX Exporter (Prometheus)
EXPOSE 7071      # JMX порт

# Запуск приложения с JMX Exporter и JVM параметрами
ENTRYPOINT ["java",
  "-javaagent:/opt/jmx_exporter.jar=9404:/opt/consumer_jmx.yml",
  "-Dcom.sun.management.jmxremote",
  "-Dcom.sun.management.jmxremote.port=7071",
  "-Dcom.sun.management.jmxremote.rmi.port=7071",
  "-Dcom.sun.management.jmxremote.authenticate=false",
  "-Dcom.sun.management.jmxremote.ssl=false",
  "-Djava.rmi.server.hostname=144.31.77.177",
  "-jar",
  "/app.jar"]
