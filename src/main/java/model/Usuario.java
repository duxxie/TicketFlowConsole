package model;

import java.time.LocalDate;
import java.time.Period;

/** Usuário da plataforma: compra ingressos e, se for organizador, cria eventos. */
public class Usuario {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
    private boolean organizador;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String cpf, String email, LocalDate dataNascimento,
                   boolean organizador) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.organizador = organizador;
    }

    public int getIdade() {
        if (dataNascimento == null) {
            throw new IllegalStateException("A data de nascimento do usuário não foi informada.");
        }
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isOrganizador() {
        return organizador;
    }

    public void setOrganizador(boolean organizador) {
        this.organizador = organizador;
    }
}
