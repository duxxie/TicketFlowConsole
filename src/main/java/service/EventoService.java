package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Evento;
import model.Usuario;
import pattern.builder.EventoBuilder;

/** @note Papel no Padrão: Client (Builder). Cadastro e consulta de eventos, guardados em memória. */
public class EventoService {
    private final List<Evento> eventos = new ArrayList<>();
    private long proximoId = 1;

    public Evento criarEvento(Usuario responsavel, EventoBuilder builder) {
        if (!responsavel.isOrganizador()) {
            throw new IllegalStateException("Apenas organizadores podem cadastrar eventos.");
        }

        Evento evento = builder.comId(proximoId).build();
        proximoId++;
        eventos.add(evento);
        return evento;
    }

    public Evento buscarPorId(long id) {
        for (Evento evento : eventos) {
            if (evento.getId() == id) {
                return evento;
            }
        }
        throw new IllegalArgumentException("Evento " + id + " não encontrado.");
    }

    public List<Evento> listar() {
        return Collections.unmodifiableList(eventos);
    }
}
