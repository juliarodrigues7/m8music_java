package br.com.fiap.m8music.messaging.dto;

public record PedidoEventDTO(
        Long pedidoId,
        Long clienteId,
        Long musicaId
) {
}
