import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

    extra["spring_boot_version"] = "2.0.5.RELEASE"
    extra["vertx_version"] = "3.5.3"
    extra["javax_annotation_api_version"] = "1.3.2"
    extra["javax_inject_version"] = "1"
    extra["project_reactor_version"] = "3.2.0.RELEASE"
    extra["slf4j_version"] = "1.7.25"
    extra["konf_version"] = "0.11"
    extra["juud_version"] = "3.1.5"
    extra["apache_commons_version"] = "3.8"
    extra["caffeine_version"] = "2.6.2"
    extra["junit_version"] = "5.3.1"
    extra["assertj_version"] = "3.11.1"
    extra["spring_test_junit5_version"] = "1.2.0"
}

plugins {
    kotlin("jvm") version "1.2.71"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

val thisProject = this

allprojects {
    group = "net.corda.tools.error-codes-server"
    version = "1.0-SNAPSHOT"

    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        compile(kotlin("stdlib"))
        compile(kotlin("reflect"))
        testCompile(kotlin("test"))
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

subprojects {

    thisProject.extra.properties.forEach { key, value ->
        extra[key] = value
    }
}