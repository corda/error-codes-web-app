dependencies {
    "org.apache.commons".let {

        val apache_commons_version: String by extra

        compile(group = it, name = "commons-lang3", version = apache_commons_version)
    }
    "com.fasterxml.uuid".let {

        val juud_version: String by extra

        compile(group = it, name = "java-uuid-generator", version = juud_version)
    }

    testCompile(project(":commons-test"))
}