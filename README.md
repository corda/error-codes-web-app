# Error Codes Web Application

## How to run

Run/Debug `ErrorCodesWebAppStarter.kt` from Intellij.

## API

By default, the error location is available at `http://localhost:8080/editions/[OS|ENT]/releases/<release_version>[e.g., 3.2.1]/errors/<error_code>`.

## Configuration

Configuration can be partially overridden by:

- Specific the path to an external YAML file, by passing `-Dconfiguration.file.path=/<path_to_file>.yml` as system property, or by setting `configuration.file.path=/<path_to_file>.yml` as an environmental variable.
- By specifying the full path of a property, by passing `-D<property_path>=<property_value>` as system property, or by setting `<property_path>=<property_value>` as an environmental property.

## How to build

Run `./gradlew clean build`, which will test the application and produce an executable JAR at `./starter/build/libs/error-codes-server-starter.jar`.

### Docker

Running `./gradlew clean build docker` will test, build and create a Docker image out of the application. After that, the container can be run with exposed port e.g., `docker run -p=8085:8080 net.corda.tools.error-codes-server/error-codes-server-starter`.