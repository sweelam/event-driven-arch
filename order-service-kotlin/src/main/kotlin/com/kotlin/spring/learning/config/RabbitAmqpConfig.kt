package com.kotlin.spring.learning.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component
class RabbitAmqpConfig (@Value("\${spring.rabbitmq.username}") val username: String,
                        @Value("\${spring.rabbitmq.password}") val password: String) {
    companion object {
        val DIRECT_ROUTING_KEY: String = "order"
        val DIRECT_QUEUE: String = "order"
        val DIRECT_EXCHANGE_NAME: String = "order"
    }


    @Bean
    fun cashingConnectionFactory() : CachingConnectionFactory{
        val cachingFactory = CachingConnectionFactory("localhost")
        cachingFactory.setUsername(username)
        cachingFactory.setPassword(password)
        return cachingFactory
    }

    @Bean
    fun rabbitAdmin(cashingConnectionFactory: CachingConnectionFactory): RabbitAdmin
        = RabbitAdmin(cashingConnectionFactory)

    @Bean
    fun jsonMessageConverter() : MessageConverter = Jackson2JsonMessageConverter()

    @Bean
    @Primary
    fun ampqTemplate(cashingConnectionFactory: CachingConnectionFactory,
                    jsonMessageConverter: MessageConverter
                     ): AmqpTemplate {
        val rabbitTemplate = RabbitTemplate(cashingConnectionFactory)
        rabbitTemplate.messageConverter = jsonMessageConverter
        return rabbitTemplate
    }

    @PostConstruct
    fun init() {
        var queueMgrMap = mapOf(DIRECT_QUEUE to DirectExchange(DIRECT_EXCHANGE_NAME))
        val rabbitAdmin = RabbitAdmin(cashingConnectionFactory())

        for (s in queueMgrMap.keys) {
            rabbitAdmin.declareQueue(Queue(s, false));

            var exchange = queueMgrMap.get(s);
            rabbitAdmin.declareExchange(exchange as Exchange);

            rabbitAdmin.declareBinding(
                 Binding(s, Binding.DestinationType.QUEUE, exchange?.name, "*.*", null)
            );
        }
    }
}