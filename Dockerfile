FROM openjdk:17
WORKDIR /app
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["/wait-for-it.sh", "postgres:5432", "-t", "15", "--", "java", "-jar", "/app.jar"]