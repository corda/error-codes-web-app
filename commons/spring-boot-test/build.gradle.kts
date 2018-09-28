dependencies {

    compile(project(":commons-spring-boot"))

    "com.github.sbrannen".let {

        val spring_test_junit5_version: String by extra

        println("MICHELE $spring_test_junit5_version")
        compile(group = it, name = "spring-test-junit5", version = spring_test_junit5_version)
    }

    testCompile(project(":commons-test"))
}