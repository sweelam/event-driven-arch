package com.kotlin.spring.learning.services

import com.kotlin.spring.learning.config.RabbitAmqpConfig
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.util.logging.Level
import java.util.logging.Logger

data class Order (var quantity: Int = 1, val title: String, val customerName: String)

@Component
class EmailListener {
    var logger = Logger.getLogger(RabbitListener::class.java.name)

    @RabbitListener(bindings = [
        QueueBinding(
            value = Queue(RabbitAmqpConfig.DIRECT_QUEUE, durable = "false") ,
            exchange = Exchange(RabbitAmqpConfig.DIRECT_EXCHANGE_NAME),
            key = [RabbitAmqpConfig.DIRECT_ROUTING_KEY]
        )
    ])
    fun listen(`in`: Message) {
        logger.log(
            Level.INFO, String.format(
                "Message ID %s from Queue %s the message %s",
                `in`.messageProperties.consumerQueue,
                `in`.messageProperties.consumerQueue,
                String(`in`.body)
            )
        )
    }
}