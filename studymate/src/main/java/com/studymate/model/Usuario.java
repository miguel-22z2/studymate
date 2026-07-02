package com.studymate.model;

import java.time.LocalDateTime;

/**
 * Representa um usuário cadastrado no StudyMate.
 */
public class Usuario {

    private int           id;
    private String        nome;
    private String        email;
    private String        senhaHash;
    private LocalDateTime criadoEm;
    private LocalDateTime ultimoAcesso;

    public Usuario() {}

    public Usuario(int id, String nome, String email) {
        this.id    = id;
        this.nome  = nome;
        this.email = email;
    }

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
    public void setUltimoAcesso(LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', email='" + email + "'}";
    }
}
