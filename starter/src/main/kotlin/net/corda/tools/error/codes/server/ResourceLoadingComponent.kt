package net.corda.tools.error.codes.server

import org.springframework.core.io.ResourceLoader
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named

@Named
internal class ResourceLoadingComponent @Inject constructor(private val resourceLoader: ResourceLoader) : (String) -> InputStream {

    override fun invoke(resourceName: String): InputStream = resourceLoader.getResource(resourceName).inputStream
}