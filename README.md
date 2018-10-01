# Error Codes Web Application

## How to run

Run/Debug `ErrorCodesWebAppStarter.kt` from Intellij.

## API

By default, the error location is available at `http://localhost:8080/editions/[OS|ENT]/releases/<release_version>[e.g., 3.2.1]/errors/<error_code>`.

Configuration could be optionally set to a specific YAML file by passing `-Dconfiguration.file.path=/<path_to_file>.yml` as system property, or by setting `configuration.file.path=/<path_to_file>.yml` as an environmental variable.