FROM openjdk:17
ADD /target/drone-*.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]