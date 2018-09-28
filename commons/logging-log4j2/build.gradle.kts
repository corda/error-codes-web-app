dependencies {
    "org.springframework.boot".let {

        val spring_boot_version: String by extra

        compile(group = it, name = "spring-boot-starter-log4j2", version = spring_boot_version)
    }

    testCompile(project(":commons-test"))
}