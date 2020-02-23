# Surly

This project provides a simple URL-shortening service.

It is NOT intended to be a real Production service, it is used as a reasonable example to to demonstrate how to build a
microservice using various contemporary cloudy technologies:

 * Java
 * Spring Boot
 * Kubernetes
 * Docker
 * AWS
 * DynamoDB

## Status

This is a work-in-progress.

The application code itself is finished and usable, next steps are to work with Docker and Kubernetes.

## Important!

Currently there are necessary but useless _local_ AWS access keys in the `application.properties` file - this does not
matter when running locally, but do not do this in a public Production environment.

## Local DynamoDB

It is advisable to run a local instance of DynamoDB for testing, for information on how to do that see the AWS guide
[here](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html).

## Running

The main goal is to use Kubernetes and Docker to deploy and run this application in an AWS cloud environment.

To run locally instead:

```
mvn clean install spring-boot:run
```

## Servlet Container

Jetty is preferred over Tomcat, because Jetty is kinda awesome and Tomcat kinda isn't.

## Docker

Reference documentation [here](https://docs.docker.com/install).

### Installation and Setup on Linux
```
sudo apt install docker.io
```

User privileges (need to logout and login before the group changes will apply):
```
sudo usermod -a -G docker $USER
```

Start Docker at boot-time:
```
sudo systemctl start docker
sudo systemctl enable docker
```

To stop or restart Docker, if needed:
```
sudo systemctl stop docker
sudo systemctl restart docker
```

### Image Basics

By default, Docker is already setup to use the default image repository.

There are official and un-official images in there. Be wary of un-official images.

Searching for images:
```
docker search <image-name>
```

Using images:
```
docker pull <image-name>
```

### Docker and Spring Boot

Reference documentation [here](https://spring.io/guides/gs/spring-boot-docker).

The `Dockerfile` is in the project root directory:

```
FROM openjdk:11-jdk
LABEL maintainer="mark.lee@capricasoftware.co.uk"
ARG GROUP=surly
ARG USER=surly
RUN groupadd ${GROUP} && useradd -g ${GROUP} -s /bin/sh ${USER}
USER surly:surly
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} surly.jar
ENTRYPOINT ["java", "-jar", "surly.jar"]
```

Building the image:

```
docker build -t caprica/surly .
```

Listing the images:

```
docker images
```

Running the image locally:

```
docker run -i -t caprica/surly
```

_Note currently the container version does not work as-is because it tries to connect to a local DynamoDB instance._

## Kubernetes

Reference documentation [here](https://kubernetes.io/docs/tasks).

## Copyright and License

This project is licensed according to the [Apache Software License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).

©2020 [Caprica Software Limited](http://capricasoftware.co.uk)
