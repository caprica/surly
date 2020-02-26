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

To run the local DynamoDB server:

```
java -jar DynamoDBLocal.jar
```

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

### Layered Images

Each line in a Dockerfile potentially (most likely) creates a new layer in the image. This depends on the command given,
some commands may not in fact cause a new layer to be generated.

When images are built, they are built from all of those layers. Layers are only built if they have not changed,
otherwise they are simply pulled from a local cache.

This is important, because structuring the Dockerfile in the most optimal way will reduce image downloads (from
repositories) and decrease build times.

The strategy therefore should be (where possible) to specify commands in the Dockerfile in order of least likely to
change (or perhaps slowest to build) first, followed later by those commands that are most likely to change.

Typically this will mean:

 * the main image (e.g. the one containing the base Operating System and perhaps some minimal set of installed software)
   will be specified first;
 * next perhaps any installations of dependent software using operating system package managers;
 * things like unchanging settings such as adding groups and users;
 * commands that deal with copying project build artifacts to the image;
 * finally the main command that runs, or is the entry-point, for the image.

Here it is clear the operating system image will rarely change, so this is first, whereas the build artifacts which
will of course change frequently are specified as late as possible.

### Stale/Dangling Images

Each docker build invocation creates a new image - how many of the layers in the image are built depends on what is
unchanged from the previous build and can be pulled from the cache, and what has changed since the last build.

Each build will end up in the local image repository.

As mentioned above, `docker images` can be used to show the current state of the local image repository.

From the list of images displayed it can be determined if there are any old, dangling, or otherwise no longer needed
images in the repository. These old images will consume potentially large amounts of disk space, so from time to time
these redundant images should be 'pruned' to remove them from the local repository.

From the Docker documentation, "a dangling image is an image that is not tagged and not referenced by any container".
These images can be cleaned up by:

```
docker image prune
```

There may be other redundant images than purely dangling ones. To remove all of those images that are not currently
associated with a container, use:

```
docker image prune -a
```

For more information refer to the [reference documentation](https://docs.docker.com/config/pruning).

## Kubernetes

Reference documentation [here](https://kubernetes.io/docs/tasks).

Example Kubernetes configuration files are in the project `etc` directory.

### Deployment

Container configuration for the application is specified in a standard Kubernetes "deployment" file.

In `surly-deployment.yaml`:

```
pending
```

To apply the file:
```
kubectl create -f surly-deployment.yaml
```

### Secrets

Passwords, credentials, and any sensitive information must of course not be committed to the repository.

Instead, such values are specified in a standard Kubernetes "secret" file and later injected via environment variables
when the container is created.

Kubernetes secret values must be base64 encoded, using for example:

```
echo "my aws accesskey" | base64
```

In `surly-secret.yaml`:

```
pending
```

To apply the file:
```
kubectl create -f surly-secret.yaml
```

### Service

To expose the application as a usable service, the necessary configuration is specified in a standard Kubernetes
"service" file.

In `surly-service.yaml`:

```
pending
```

To apply the file:
```
kubectl create -f surly-service.yaml
```

## Swagger API Documentation

### Local End-point

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

```
pending
```

### Customisation

To re-skin the Swagger UI, e.g. with corporate or project branding instead of the default Swagger branding, it is
necessary to copy the resources from the Swagger UI project (springfox) into your project `resources` folder and do
whatever customisations you want there.

This would be instead of including the springfox dependency in the project `pom.xml` file.

## Copyright and License

This project is licensed according to the [Apache Software License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).

©2020 [Caprica Software Limited](http://capricasoftware.co.uk)
