package ru.edu.queryexecutor.rabbit.consumer

import mu.KotlinLogging
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
import ru.edu.queryexecutor.feign.InstancesManagerClient
import ru.edu.queryexecutor.feign.request.InstanceEntity
import ru.edu.queryexecutor.request.QueryRequest
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.Security
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.sql.SQLException
import java.util.*
import javax.crypto.Cipher

@Service
class QueryConsumer(
    private val instancesManagerClient: InstancesManagerClient
) {
    private val logger = KotlinLogging.logger { }
    fun findDriver(driverName: String): InstanceEntity? {
        return try {
            instancesManagerClient.findInstanceByDbms(driverName)
        } catch (ex: Exception) {
            logger.error(ex.message)
            null
        }
    }

    @RabbitListener(queues = ["\${spring.rabbitmq.consumer.request.queue}"], returnExceptions = "true")
    fun queryHandler(message: Message<String>): Any? {
        return try {
            logger.info(message.toString())
            val request = QueryRequest().apply {
                sql = message.payload
                database = message.headers["database"].toString()
                dbms = message.headers["dbms"].toString()
                login = message.headers["login"].toString()
                password = message.headers["password"].toString()
            }
            executeQuery(request)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ex.message
        }
    }

    fun executeQuery(request: QueryRequest): Any? {
        try {
            logger.info("Request execute: $request")
            val decryptCipher = Cipher.getInstance("RSA")
            decryptCipher.init(Cipher.DECRYPT_MODE, getPrivateKey())
            val dataSource = findDriver(request.dbms)
            val driverManagerDataSources = DriverManagerDataSource().apply {
                url = dataSource?.url + request.database
                username = request.login
                password = String(decryptCipher.doFinal(Base64.getDecoder().decode(request.password)))
            }
            logger.debug("Current connection: ${dataSource?.url}")
            val resultSet = driverManagerDataSources.connection.createStatement()?.executeQuery(request.sql)

            val result = mutableListOf<MutableMap<String, Any?>>()
            var map = mutableMapOf<String, Any?>()

            while (resultSet?.next() == true) {
                for (i in 1..resultSet.metaData.columnCount) {
                    map[resultSet.metaData.getColumnName(i)] = resultSet.getObject(i)
                }
                result.add(map)
                map = mutableMapOf()
            }
            driverManagerDataSources.connection.close()
            return result
        } catch (ex: SQLException) {
            logger.error(ex.message)
            throw ex
        }
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