dependencies {

    "com.uchuhimo".let {

        val konf_version: String by extra

        compile(group = it, name = "konf", version = konf_version)
    }

    testCompile(project(":commons-test"))
}