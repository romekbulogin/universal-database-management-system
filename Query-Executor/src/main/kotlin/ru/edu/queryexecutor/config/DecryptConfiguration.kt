package ru.edu.queryexecutor.config

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.Security
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

@Configuration
class DecryptConfiguration {

    @Bean
    fun decryptCipher(): Cipher = Cipher.getInstance("RSA").apply {
        init(Cipher.DECRYPT_MODE, getPrivateKey())
    }

    private fun getPrivateKey(): RSAPrivateKey {
        Security.addProvider(
            BouncyCastleProvider()
        )
        val key = String(
            Files.readAllBytes(Paths.get("Query-Executor\\src\\main\\resources\\privatekey.pem")),
            Charset.defaultCharset()
        )

        val privateKeyPEM = key
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace(System.lineSeparator().toRegex(), "")
            .replace("-----END PRIVATE KEY-----", "")

        val encoded: ByteArray = Base64.getDecoder().decode(privateKeyPEM)

        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = PKCS8EncodedKeySpec(encoded)
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }
}