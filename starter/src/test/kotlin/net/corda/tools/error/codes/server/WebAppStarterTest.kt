package net.corda.tools.error.codes.server

import net.corda.tools.error.codes.server.web.WebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.inject.Inject

@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ErrorCodesWebAppStarter::class])
internal class WebAppStarterTest {

    @Inject
    private lateinit var webServer: WebServer

    @Test
    fun web_server_is_up() {

        assertThat(webServer).isNotNull
    }
}