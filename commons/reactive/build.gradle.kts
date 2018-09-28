dependencies {

    "io.projectreactor".let {

        val project_reactor_version: String by extra

        compile(group = it, name = "reactor-core", version = project_reactor_version)
    }

    testCompile(project(":commons-test"))
}