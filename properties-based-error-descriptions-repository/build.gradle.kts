dependencies {

    "org.springframework".let {

        val spring_version: String by extra
        compile(group = it, name = "spring-core", version = spring_version)
    }

    compile(project(":domain"))

    compile(project(":commons-configuration"))
    compile(project(":commons-di"))
    compile(project(":commons-domain"))
    compile(project(":commons-reactive"))
    compile(project(":commons-events"))
    compile(project(":commons-lifecycle"))
    compile(project(":commons-logging"))
    runtime(project(":commons-logging-log4j2"))

    testCompile(project(":commons-test"))
}