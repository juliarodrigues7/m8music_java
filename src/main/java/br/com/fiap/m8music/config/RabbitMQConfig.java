package br.com.fiap.m8music.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE     = "m8music.exchange";
    public static final String PEDIDO_QUEUE = "pedido.queue";
    public static final String PEDIDO_DLQ   = "pedido.dlq";
    public static final String ROUTING_KEY  = "pedido.criado";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue pedidoQueue() {
        return QueueBuilder.durable(PEDIDO_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", PEDIDO_DLQ)
                .build();
    }

    @Bean
    public Queue pedidoDlq() {
        return QueueBuilder.durable(PEDIDO_DLQ).build();
    }

    @Bean
    public Binding pedidoBinding(Queue pedidoQueue, DirectExchange exchange) {
        return BindingBuilder.bind(pedidoQueue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
