package br.com.fiap.m8music.service;

import br.com.fiap.m8music.domain.Cliente;
import br.com.fiap.m8music.domain.Musica;
import br.com.fiap.m8music.domain.Pedido;
import br.com.fiap.m8music.exception.BusinessException;
import br.com.fiap.m8music.getaway.dto.pedido.PedidoDTO;
import br.com.fiap.m8music.getaway.repository.PedidoRepository;
import br.com.fiap.m8music.messaging.PedidoProducer;
import br.com.fiap.m8music.messaging.dto.PedidoEventDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository repo;
    private final ClienteService clienteService;
    private final MusicaService musicaService;
    private final PedidoProducer pedidoProducer;

    private PedidoDTO toDTO(Pedido p) {
        return new PedidoDTO(p.getId(), p.getCliente().getId(), p.getMusica().getId());
    }

    private void apply(Pedido p, PedidoDTO d) {
        Cliente c = clienteService.getEntity(d.clienteId());
        Musica m = musicaService.getEntity(d.musicaId());
        p.setCliente(c);
        p.setMusica(m);
    }

    @Transactional
    public PedidoDTO create(PedidoDTO dto) {
        Pedido p = new Pedido();
        apply(p, dto);
        PedidoDTO saved = toDTO(repo.save(p));
        pedidoProducer.publicar(new PedidoEventDTO(saved.id(), saved.clienteId(), saved.musicaId()));
        return saved;
    }

    public List<PedidoDTO> list() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public PedidoDTO get(Long id) {
        return toDTO(getEntity(id));
    }

    @Transactional
    public PedidoDTO update(Long id, PedidoDTO dto) {
        Pedido p = getEntity(id);
        apply(p, dto);
        return toDTO(repo.save(p));
    }

    public void delete(Long id) {
        repo.delete(getEntity(id));
    }

    public Pedido getEntity(Long id) {
        return repo.findById(id).orElseThrow(() -> new BusinessException("Pedido não encontrado"));
    }
}
