import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

    println(gradle.gradleHomeDir)

    val versions = mapOf(
            "spring_boot" to "2.0.5.RELEASE",
            "vertx" to "3.5.3",
            "javax_annotation_api" to "1.3.2",
            "javax_inject" to "1",
            "project_reactor" to "3.2.0.RELEASE",
            "konf" to "0.11",
            "juud" to "3.1.5",
            "apache_commons" to "3.8",
            "caffeine" to "2.6.2",
            "junit" to "5.3.1",
            "assertj" to "3.11.1",
            "spring_test_junit5" to "1.2.0")
    project.extra["versions"] = versions
}

plugins {
    kotlin("jvm") version "1.2.71"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

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