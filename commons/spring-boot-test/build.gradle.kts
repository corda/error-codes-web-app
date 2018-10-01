dependencies {

    compile(project(":commons-spring-boot"))
    compile(project(":commons-di"))

    "org.springframework".let {

        val spring_version: String by extra

        compile(group = it, name = "spring-test", version = spring_version)
    }

    "org.springframework.boot".let {

        val spring_boot_version: String by extra

        compile(group = it, name = "spring-boot-test", version = spring_boot_version)
    }

    testCompile(project(":commons-test"))
}