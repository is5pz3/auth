# Multi-stage build setup (https://docs.docker.com/develop/develop-images/multistage-build/)

# Stage 1 (to create a "build" image)
FROM openjdk:12-jdk-alpine AS builder
RUN java -version

COPY . /usr/src/myapp/
WORKDIR /usr/src/myapp/
RUN apk --no-cache add maven && mvn --version
RUN mvn package

# Stage 2 (to create a downsized "container executable")
FROM openjdk:12-jdk-alpine
WORKDIR /root/
COPY --from=builder /usr/src/myapp/target/auth-0.0.1-SNAPSHOT.jar .

EXPOSE 8080
CMD java -Xmx256m -jar ./auth-0.0.1-SNAPSHOT.jar --server.port=$PORT
