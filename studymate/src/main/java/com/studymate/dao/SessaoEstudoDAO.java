package com.studymate.dao;

import com.studymate.model.SessaoEstudo;
import com.studymate.util.ConexaoDB;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * DAO responsável pelas operações de banco relacionadas a SessaoEstudo.
 */
public class SessaoEstudoDAO {

    /**
     * Cria uma nova sessão e retorna o ID gerado.
     */
    public int criar(int usuarioId, int materiaId) throws SQLException {
        String sql = "INSERT INTO sessoes_estudo (usuario_id, materia_id) VALUES (?, ?)";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, usuarioId);
            ps.setInt(2, materiaId);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        throw new SQLException("Falha ao criar sessão de estudo.");
    }

    /**
     * Encerra a sessão com o placar final.
     */
    public void encerrar(int sessaoId, int totalQuestoes, int totalAcertos) throws SQLException {
        String sql = "UPDATE sessoes_estudo SET encerrado_em = ?, total_questoes = ?, total_acertos = ? " +
                     "WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, totalQuestoes);
            ps.setInt(3, totalAcertos);
            ps.setInt(4, sessaoId);
            ps.executeUpdate();
        }
    }

    /**
     * Busca uma sessão pelo ID.
     */
    public SessaoEstudo buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, usuario_id, materia_id, iniciado_em, encerrado_em, " +
                     "total_questoes, total_acertos FROM sessoes_estudo WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SessaoEstudo s = new SessaoEstudo();
                    s.setId(rs.getInt("id"));
                    s.setUsuarioId(rs.getInt("usuario_id"));
                    s.setMateriaId(rs.getInt("materia_id"));
                    s.setTotalQuestoes(rs.getInt("total_questoes"));
                    s.setTotalAcertos(rs.getInt("total_acertos"));

                    Timestamp ini = rs.getTimestamp("iniciado_em");
                    if (ini != null) s.setIniciadoEm(ini.toLocalDateTime());

                    Timestamp fim = rs.getTimestamp("encerrado_em");
                    if (fim != null) s.setEncerradoEm(fim.toLocalDateTime());

                    return s;
                }
            }
        }

        return null;
    }

    /**
     * Conta o total de sessões realizadas por um usuário.
     */
    public int contarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM sessoes_estudo WHERE usuario_id = ? AND encerrado_em IS NOT NULL";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
