package com.kotlin.spring.learning.controllers

import com.kotlin.spring.learning.config.RabbitAmqpConfig
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class Order (var quantity: Int = 1, val title: String, val customerName: String)

@Service
class OrderService (var amqpTemplate: AmqpTemplate) {
    fun charge(order: Order) : Unit =   amqpTemplate.convertAndSend(RabbitAmqpConfig.DIRECT_ROUTING_KEY, order.toString())
}

@RestController
@RequestMapping("api")
class OrderController (var orderService: OrderService){

    @PostMapping("/order")
    fun submitOrder (@RequestBody order: Order) : Unit {

        orderService.charge(order)

    }
}