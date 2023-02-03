package ru.edu.authorizationservice.service

import mu.KotlinLogging
import org.apache.logging.log4j.message.SimpleMessage
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(private val mailSender: JavaMailSender) {
    private val logger = KotlinLogging.logger { }
    fun sendMessageVerify(to: String, subject: String, text: String) {
        try {
            val message = SimpleMailMessage().apply {
                from = "norely@gmail.com"
                setTo(to)
                setSubject(subject)
                setText(text)
            }
            mailSender.send(message)
        } catch (ex: Exception) {
            logger.error(ex.message)
        }

    }
}