package br.com.fiap.m8music.messaging;

import br.com.fiap.m8music.config.RabbitMQConfig;
import br.com.fiap.m8music.messaging.dto.PedidoEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.PEDIDO_QUEUE)
    public void consumir(PedidoEventDTO evento) {
        log.info("Pedido recebido da fila: pedidoId={}, clienteId={}, musicaId={}",
                evento.pedidoId(), evento.clienteId(), evento.musicaId());
    }
}
