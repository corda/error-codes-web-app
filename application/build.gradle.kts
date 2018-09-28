dependencies {

    "com.uchuhimo".let {

        val konf_version: String by extra

        compile(group = it, name = "konf", version = konf_version)
    }
    "com.github.ben-manes.caffeine".let {

        val caffeine_version: String by extra

        compile(group = it, name = "caffeine", version = caffeine_version)
    }

    compile(project(":domain"))

    compile(project(":commons-di"))
    compile(project(":commons-domain"))
    compile(project(":commons-reactive"))
    compile(project(":commons-events"))
    compile(project(":commons-logging"))
    runtime(project(":commons-logging-log4j2"))

    testCompile(project(":commons-test"))
}