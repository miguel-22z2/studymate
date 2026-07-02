package com.studymate.model;

import java.time.LocalDateTime;

/**
 * Representa uma matéria criada por um usuário.
 */
public class Materia {

    private int           id;
    private int           usuarioId;
    private String        nome;
    private String        descricao;
    private LocalDateTime criadoEm;

    public Materia() {}

    public Materia(int usuarioId, String nome, String descricao) {
        this.usuarioId = usuarioId;
        this.nome      = nome;
        this.descricao = descricao;
    }

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    @Override
    public String toString() {
        return "Materia{id=" + id + ", nome='" + nome + "'}";
    }
}
