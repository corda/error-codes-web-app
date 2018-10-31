FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ARG USER_NAME
ARG USER_GROUP
ARG REVISION_ID
#This is because you cannot use an argument in ENTRYPOINT
ENV CONFIGURATION_METADATA_REVISION_ID=${REVISION_ID}
RUN mkdir .vertx
RUN addgroup ${USER_GROUP} && adduser -G ${USER_GROUP} -D -s /bin/bash ${USER_NAME} && chown -R ${USER_NAME}:${GROUP_NAME} .vertx
USER ${USER_NAME}:${GROUP_NAME}
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]