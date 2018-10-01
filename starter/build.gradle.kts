apply {
    plugin("io.spring.dependency-management")
}

dependencies {

    "org.springframework.boot".let {

        val spring_boot_version: String by extra

        compile(group = it, name = "spring-boot", version = spring_boot_version)
        compile(group = it, name = "spring-boot-autoconfigure", version = spring_boot_version)
    }

    compile(project(":domain"))
    runtime(project(":webserver"))
    runtime(project(":application"))
    runtime(project(":properties-based-error-descriptions-repository"))

    compile(project(":commons-configuration"))
    compile(project(":commons-di"))
    compile(project(":commons-domain"))
    compile(project(":commons-reactive"))
    compile(project(":commons-events"))
    compile(project(":commons-lifecycle"))
    compile(project(":commons-logging"))
    runtime(project(":commons-logging-log4j2"))

    testCompile(project(":webserver"))
    testCompile(project(":application"))
    testCompile(project(":commons-test"))
    testCompile(project(":commons-spring-boot-test"))
    testCompile(project(":commons-vertx-web-test"))
}