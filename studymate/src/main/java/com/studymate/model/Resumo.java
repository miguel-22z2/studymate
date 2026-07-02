package com.studymate.model;

import java.time.LocalDateTime;

/**
 * Representa um resumo gerado pela IA a partir de um conteúdo enviado pelo aluno.
 */
public class Resumo {

    private int           id;
    private int           materiaId;
    private String        conteudoRaw;   // texto original enviado pelo aluno
    private String        resumoIa;      // resumo gerado pela IA
    private LocalDateTime geradoEm;

    public Resumo() {}

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMateriaId() { return materiaId; }
    public void setMateriaId(int materiaId) { this.materiaId = materiaId; }

    public String getConteudoRaw() { return conteudoRaw; }
    public void setConteudoRaw(String conteudoRaw) { this.conteudoRaw = conteudoRaw; }

    public String getResumoIa() { return resumoIa; }
    public void setResumoIa(String resumoIa) { this.resumoIa = resumoIa; }

    public LocalDateTime getGeradoEm() { return geradoEm; }
    public void setGeradoEm(LocalDateTime geradoEm) { this.geradoEm = geradoEm; }

    @Override
    public String toString() {
        return "Resumo{id=" + id + ", materiaId=" + materiaId + "}";
    }
}
