val versions: Map<String, String> by project.parent?.extra!!

dependencies {
    "org.apache.commons".let {
        val version = versions["apache_commons"]
        compile(group = it, name = "commons-lang3", version = version)
    }
    "com.fasterxml.uuid".let {
        val version = versions["juud"]
        compile(group = it, name = "java-uuid-generator", version = version)
    }

    testCompile(project(":commons-test"))
}