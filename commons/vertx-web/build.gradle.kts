dependencies {

    "io.vertx".let {

        val vertx_version: String by extra

        compile(group = it, name = "vertx-web", version = vertx_version)
    }

    compile(project(":commons-vertx"))

    testCompile(project(":commons-test"))
}