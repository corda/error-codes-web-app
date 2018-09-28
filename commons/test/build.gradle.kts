dependencies {
    "org.junit.jupiter".let {

        val junit_version: String by extra

        compile(group = it, name = "junit-jupiter-api", version = junit_version)
        compile(group = it, name = "junit-jupiter-engine", version = junit_version)
    }
    "org.assertj".let {

        val assertj_version: String by extra

        compile(group = it, name = "assertj-core", version = assertj_version)
    }
}