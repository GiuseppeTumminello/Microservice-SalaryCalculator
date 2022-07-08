FROM gradle:7.4-jdk11-alpine AS build
COPY . .
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:11-jre-slim
EXPOSE 8086
COPY --from=build /home/gradle/src/build/libs/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD java $JAVA_OPTIONS -jar app.jar