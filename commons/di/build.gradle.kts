dependencies {

    "javax.annotation".let {

        val javax_annotation_api_version: String by extra

        compile(group = it, name = "javax.annotation-api", version = javax_annotation_api_version)
    }
    "javax.inject".let {

        val javax_inject_version: String by extra

        compile(group = it, name = "javax.inject", version = javax_inject_version)
    }

    testCompile(project(":commons-test"))
}