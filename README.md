# Error Codes Web Application

## How to run

Run/Debug `ErrorCodesWebAppStarter.kt` from Intellij.

## API

By default, the error location is available at `http://localhost:8080/editions/[OS|ENT]/releases/<release_version>[e.g., 3.2.1]/errors/<error_code>`.

## Configuration 

Configuration can be partially overridden by:

- Specific the path to an external YAML file, by passing `-Dconfiguration.file.path=/<path_to_file>.yml` as system property, or by setting `configuration.file.path=/<path_to_file>.yml` as an environmental variable.
- By specifying the full path of a property, by passing `-D<property_path>=<property_value>` as system property, or by setting `<property_path>=<property_value>` as an environmental property.