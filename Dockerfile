FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["/wait-for-it.sh", "postgres:5432", "-t", "15", "--", "sh", "-c", \
            "java", "-jar", "/app.jar"]