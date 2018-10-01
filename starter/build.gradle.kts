dependencies {

    "org.springframework.boot".let {

        val spring_boot_version: String by extra

        compile(group = it, name = "spring-boot", version = spring_boot_version)
        compile(group = it, name = "spring-boot-autoconfigure", version = spring_boot_version)
    }

//    TODO trim the build.gradle.kts files, changing from compile to runtime, etc.
    // TODO make webserver a runtime dependency
    compile(project(":webserver"))
    compile(project(":application"))
    compile(project(":domain"))
    runtime(project(":properties-based-error-descriptions-repository"))

    compile(project(":commons-vertx"))
    compile(project(":commons-vertx-web"))
    compile(project(":commons-configuration"))
    compile(project(":commons-di"))
    compile(project(":commons-domain"))
    compile(project(":commons-reactive"))
    compile(project(":commons-events"))
    compile(project(":commons-lifecycle"))
    compile(project(":commons-logging"))
    runtime(project(":commons-logging-log4j2"))

    testCompile(project(":commons-test"))
    testCompile(project(":commons-spring-boot-test"))
    testCompile(project(":commons-vertx-web-test"))
}