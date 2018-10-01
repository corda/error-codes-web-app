rootProject.name = "error-codes-server"

val modules = setOf("starter", "webserver", "application", "domain", "properties-based-error-descriptions-repository")

val commons = setOf("configuration", "di", "domain", "events", "identity", "lifecycle", "logging", "logging-log4j2", "reactive", "spring-boot", "spring-boot-test", "test", "vertx", "vertx-web", "vertx-web-test")

modules::forEach { include(it) }

commons.forEach { module ->

    val moduleName = "commons-$module"
    include(moduleName)
    project(":$moduleName").projectDir = file("./commons/$module")
}