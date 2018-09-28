val versions: Map<String, String> by project.parent?.extra!!

dependencies {
    "org.junit.jupiter".let {
        val version = versions["junit"]
        compile(group = it, name = "junit-jupiter-api", version = version)
        compile(group = it, name = "junit-jupiter-engine", version = version)
    }
    "org.assertj".let {
        val version = versions["assertj"]
        compile(group = it, name = "assertj-core", version = version)
    }
}