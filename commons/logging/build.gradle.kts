dependencies {
    compile(project(":commons-identity"))

    "org.slf4j".let {

        val slf4j_version: String by extra

        compile(group = it, name = "slf4j-api", version = slf4j_version)
    }

    testCompile(project(":commons-test"))
}