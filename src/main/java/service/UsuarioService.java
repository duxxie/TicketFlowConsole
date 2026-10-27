package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Usuario;

/** Cadastro e consulta de usuários, guardados em memória. */
public class UsuarioService {
    private final List<Usuario> usuarios = new ArrayList<>();
    private long proximoId = 1;

    public Usuario cadastrar(String nome, String cpf, String email, LocalDate dataNascimento, boolean organizador) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário deve ser informado.");
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do usuário deve ser informado.");
        }
        if (dataNascimento == null || dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Informe uma data de nascimento válida.");
        }
        for (Usuario existente : usuarios) {
            if (existente.getCpf().equals(cpf)) {
                throw new IllegalArgumentException("Já existe um usuário com o CPF " + cpf + ".");
            }
        }

        Usuario usuario = new Usuario(proximoId++, nome, cpf, email, dataNascimento, organizador);
        usuarios.add(usuario);
        return usuario;
    }

    public Usuario buscarPorId(long id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        throw new IllegalArgumentException("Usuário " + id + " não encontrado.");
    }

    public List<Usuario> listar() {
        return Collections.unmodifiableList(usuarios);
    }
}
