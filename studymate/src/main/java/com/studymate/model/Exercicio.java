package com.studymate.model;

import java.time.LocalDateTime;

/**
 * Representa um exercício gerado pela IA para uma matéria.
 */
public class Exercicio {

    private int           id;
    private int           materiaId;
    private String        enunciado;
    private String        gabaritoIa;
    private String        tipo;          // "dissertativo" ou "multipla_escolha"
    private String        opcoesJson;    // JSON com alternativas (múltipla escolha)
    private LocalDateTime geradoEm;

    public Exercicio() {}

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMateriaId() { return materiaId; }
    public void setMateriaId(int materiaId) { this.materiaId = materiaId; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getGabaritoIa() { return gabaritoIa; }
    public void setGabaritoIa(String gabaritoIa) { this.gabaritoIa = gabaritoIa; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getOpcoesJson() { return opcoesJson; }
    public void setOpcoesJson(String opcoesJson) { this.opcoesJson = opcoesJson; }

    public LocalDateTime getGeradoEm() { return geradoEm; }
    public void setGeradoEm(LocalDateTime geradoEm) { this.geradoEm = geradoEm; }

    @Override
    public String toString() {
        return "Exercicio{id=" + id + ", tipo='" + tipo + "', materiaId=" + materiaId + "}";
    }
}
