package net.corda.tools.error.codes.server

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication

// TODO create Docker image as part of Gradle build (maybe a separate task).
// This is the default, but it's better explicit.
@SpringBootApplication(scanBasePackageClasses = [ErrorCodesWebAppStarter::class])
internal open class ErrorCodesWebAppStarter

fun main(args: Array<String>) {

    val application = SpringApplication(ErrorCodesWebAppStarter::class.java)

    application.setBannerMode(Banner.Mode.OFF)
    application.webApplicationType = WebApplicationType.NONE

    application.run(*args)
}