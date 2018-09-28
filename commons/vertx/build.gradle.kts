dependencies {

    "io.vertx".let {

        val vertx_version: String by extra

        compile(group = it, name = "vertx-core", version = vertx_version)
    }

    testCompile(project(":commons-test"))
}