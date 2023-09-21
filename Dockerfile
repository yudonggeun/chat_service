FROM gradle as builder
RUN mkdir /myapp
WORKDIR /myapp
COPY . /myapp
RUN gradle clean
RUN gradle build

FROM openjdk
CMD ["./mvnw", "clean", "package"]
COPY --from=builder /myapp/build/libs/demo-0.0.1-SNAPSHOT.jar /spring-webapp.jar
EXPOSE 8080
ENTRYPOINT java -jar /spring-webapp.jar