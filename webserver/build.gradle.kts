dependencies {

    compile(project(":application"))
    compile(project(":domain"))

    compile(project(":commons-vertx"))
    compile(project(":commons-vertx-web"))
    compile(project(":commons-configuration"))
    compile(project(":commons-di"))
    compile(project(":commons-domain"))
    compile(project(":commons-reactive"))
    compile(project(":commons-events"))
    compile(project(":commons-lifecycle"))
    compile(project(":commons-logging"))
    runtime(project(":commons-logging-log4j2"))

    testCompile(project(":commons-test"))
}