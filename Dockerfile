FROM openjdk:11-jdk
LABEL maintainer="mark.lee@capricasoftware.co.uk"
ARG GROUP=surly
ARG USER=surly
RUN groupadd ${GROUP} && useradd -g ${GROUP} -s /bin/sh ${USER}
USER surly:surly
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} surly.jar
ENTRYPOINT ["java", "-jar", "surly.jar"]
