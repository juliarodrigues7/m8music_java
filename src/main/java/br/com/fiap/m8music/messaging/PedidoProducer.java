package br.com.fiap.m8music.messaging;

import br.com.fiap.m8music.config.RabbitMQConfig;
import br.com.fiap.m8music.messaging.dto.PedidoEventDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PedidoProducer {

    private static final Logger log = LoggerFactory.getLogger(PedidoProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public void publicar(PedidoEventDTO evento) {
        log.info("Publicando pedido na fila: pedidoId={}", evento.pedidoId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, evento);
    }
}
