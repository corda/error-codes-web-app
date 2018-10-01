import com.palantir.gradle.docker.DockerExtension
import org.gradle.api.tasks.bundling.Jar
import org.springframework.boot.gradle.tasks.bundling.BootJar

apply {
    plugin("org.springframework.boot")
    plugin("com.palantir.docker")
}

dependencies {

    "org.springframework.boot".let {

        val spring_boot_version: String by extra

        compile(group = it, name = "spring-boot", version = spring_boot_version)
        compile(group = it, name = "spring-boot-autoconfigure", version = spring_boot_version)
    }

    compile(project(":domain"))
    runtime(project(":webserver"))
    runtime(project(":application"))
    runtime(project(":properties-based-error-descriptions-repository"))

    compile(project(":commons-configuration"))
    compile(project(":commons-di"))
    compile(project(":commons-domain"))
    compile(project(":commons-reactive"))
    compile(project(":commons-events"))
    compile(project(":commons-lifecycle"))
    compile(project(":commons-logging"))
    runtime(project(":commons-logging-log4j2"))

    testCompile(project(":webserver"))
    testCompile(project(":application"))
    testCompile(project(":commons-test"))
    testCompile(project(":commons-spring-boot-test"))
    testCompile(project(":commons-vertx-web-test"))
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

jar.enabled = true

bootJar.enabled = true
bootJar.baseName = "error-codes-server-starter"
bootJar.archiveName = "${bootJar.baseName}.${bootJar.extension}"

configure<DockerExtension> {

    dependsOn(jar)
    name = "${project.group}/${bootJar.baseName}"
    setDockerfile(file("$rootDir/docker/Dockerfile"))
    files(bootJar.archivePath)
    buildArgs(mapOf("JAR_FILE" to bootJar.archiveName))
}