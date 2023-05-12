FROM openjdk:8
EXPOSE 8080
ADD /target/topdata-api.jar topdata-api.jar
ENTRYPOINT [ "java", "-jar","topdata-api.jar" ]