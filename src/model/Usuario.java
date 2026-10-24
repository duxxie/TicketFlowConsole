package model;

public class Usuario {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private boolean eOrganizador;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String cpf, String email, boolean eOrganizador) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.eOrganizador = eOrganizador;
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

    public boolean isEOrganizador() {
        return eOrganizador;
    }

    public void setEOrganizador(boolean eOrganizador) {
        this.eOrganizador = eOrganizador;
    }
}
