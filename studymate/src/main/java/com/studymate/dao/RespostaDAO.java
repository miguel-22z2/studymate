package com.studymate.dao;

import com.studymate.model.Resposta;
import com.studymate.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco relacionadas a Resposta.
 */
public class RespostaDAO {

    /**
     * Salva a resposta do aluno com o feedback da IA.
     */
    public void salvar(Resposta resposta) throws SQLException {
        String sql = "INSERT INTO respostas (sessao_id, exercicio_id, resposta_aluno, feedback_ia, acertou) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, resposta.getSessaoId());
            ps.setInt(2, resposta.getExercicioId());
            ps.setString(3, resposta.getRespostaAluno());
            ps.setString(4, resposta.getFeedbackIa());
            ps.setBoolean(5, resposta.isAcertou());
            ps.executeUpdate();
        }
    }

    /**
     * Lista todas as respostas de uma sessão de estudo.
     */
    public List<Resposta> listarPorSessao(int sessaoId) throws SQLException {
        String sql = "SELECT r.id, r.sessao_id, r.exercicio_id, r.resposta_aluno, " +
                     "r.feedback_ia, r.acertou, r.respondido_em, e.enunciado " +
                     "FROM respostas r " +
                     "INNER JOIN exercicios e ON r.exercicio_id = e.id " +
                     "WHERE r.sessao_id = ? ORDER BY r.respondido_em ASC";

        List<Resposta> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sessaoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Resposta r = new Resposta();
                    r.setId(rs.getInt("id"));
                    r.setSessaoId(rs.getInt("sessao_id"));
                    r.setExercicioId(rs.getInt("exercicio_id"));
                    r.setRespostaAluno(rs.getString("resposta_aluno"));
                    r.setFeedbackIa(rs.getString("feedback_ia"));
                    r.setAcertou(rs.getBoolean("acertou"));
                    r.setEnunciadoExercicio(rs.getString("enunciado"));

                    Timestamp ts = rs.getTimestamp("respondido_em");
                    if (ts != null) r.setRespondidoEm(ts.toLocalDateTime());

                    lista.add(r);
                }
            }
        }

        return lista;
    }

    /**
     * Conta o total de acertos de um usuário em todas as sessões.
     */
    public int contarAcertosPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM respostas r " +
                     "INNER JOIN sessoes_estudo s ON r.sessao_id = s.id " +
                     "WHERE s.usuario_id = ? AND r.acertou = TRUE";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
