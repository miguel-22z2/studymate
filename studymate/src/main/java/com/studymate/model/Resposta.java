package com.studymate.model;

import java.time.LocalDateTime;

/**
 * Representa a resposta do aluno a um exercício, com feedback da IA.
 */
public class Resposta {

    private int           id;
    private int           sessaoId;
    private int           exercicioId;
    private String        respostaAluno;
    private String        feedbackIa;
    private boolean       acertou;
    private LocalDateTime respondidoEm;

    // Campo extra para exibição nas JSPs (vem do JOIN com exercicios)
    private String enunciadoExercicio;

    public Resposta() {}

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSessaoId() { return sessaoId; }
    public void setSessaoId(int sessaoId) { this.sessaoId = sessaoId; }

    public int getExercicioId() { return exercicioId; }
    public void setExercicioId(int exercicioId) { this.exercicioId = exercicioId; }

    public String getRespostaAluno() { return respostaAluno; }
    public void setRespostaAluno(String respostaAluno) { this.respostaAluno = respostaAluno; }

    public String getFeedbackIa() { return feedbackIa; }
    public void setFeedbackIa(String feedbackIa) { this.feedbackIa = feedbackIa; }

    public boolean isAcertou() { return acertou; }
    public void setAcertou(boolean acertou) { this.acertou = acertou; }

    public LocalDateTime getRespondidoEm() { return respondidoEm; }
    public void setRespondidoEm(LocalDateTime respondidoEm) { this.respondidoEm = respondidoEm; }

    public String getEnunciadoExercicio() { return enunciadoExercicio; }
    public void setEnunciadoExercicio(String enunciadoExercicio) { this.enunciadoExercicio = enunciadoExercicio; }
}
