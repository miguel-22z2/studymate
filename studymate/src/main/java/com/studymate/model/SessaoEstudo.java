package com.studymate.model;

import java.time.LocalDateTime;

/**
 * Representa uma sessão de estudos/quiz do aluno.
 */
public class SessaoEstudo {

    private int           id;
    private int           usuarioId;
    private int           materiaId;
    private LocalDateTime iniciadoEm;
    private LocalDateTime encerradoEm;
    private int           totalQuestoes;
    private int           totalAcertos;

    public SessaoEstudo() {}

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getMateriaId() { return materiaId; }
    public void setMateriaId(int materiaId) { this.materiaId = materiaId; }

    public LocalDateTime getIniciadoEm() { return iniciadoEm; }
    public void setIniciadoEm(LocalDateTime iniciadoEm) { this.iniciadoEm = iniciadoEm; }

    public LocalDateTime getEncerradoEm() { return encerradoEm; }
    public void setEncerradoEm(LocalDateTime encerradoEm) { this.encerradoEm = encerradoEm; }

    public int getTotalQuestoes() { return totalQuestoes; }
    public void setTotalQuestoes(int totalQuestoes) { this.totalQuestoes = totalQuestoes; }

    public int getTotalAcertos() { return totalAcertos; }
    public void setTotalAcertos(int totalAcertos) { this.totalAcertos = totalAcertos; }

    /** Calcula o percentual de acertos da sessão. */
    public int getPercentualAcertos() {
        if (totalQuestoes == 0) return 0;
        return (totalAcertos * 100) / totalQuestoes;
    }
}
