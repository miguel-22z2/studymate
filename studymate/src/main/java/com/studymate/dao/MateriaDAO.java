package com.studymate.dao;

import com.studymate.model.Materia;
import com.studymate.util.ConexaoDB;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco relacionadas à Materia.
 */
@SuppressWarnings("unused")
public class MateriaDAO {

    /**
     * Lista todas as matérias de um usuário, ordenadas pela mais recente.
     */
    public List<Materia> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT id, usuario_id, nome, descricao, criado_em " +
                     "FROM materias WHERE usuario_id = ? ORDER BY criado_em DESC";

        List<Materia> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }

        return lista;
    }

    /**
     * Busca uma matéria pelo ID, garantindo que pertence ao usuário.
     * Retorna null se não encontrada ou se não pertencer ao usuário.
     */
    public Materia buscarPorId(int id, int usuarioId) throws SQLException {
        String sql = "SELECT id, usuario_id, nome, descricao, criado_em " +
                     "FROM materias WHERE id = ? AND usuario_id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }

        return null;
    }

    /**
     * Insere uma nova matéria no banco.
     */
    public void criar(Materia materia) throws SQLException {
        String sql = "INSERT INTO materias (usuario_id, nome, descricao) VALUES (?, ?, ?)";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, materia.getUsuarioId());
            ps.setString(2, materia.getNome());
            ps.setString(3, materia.getDescricao());
            ps.executeUpdate();
        }
    }

    /**
     * Atualiza nome e descrição de uma matéria existente.
     * Só atualiza se a matéria pertencer ao usuário.
     */
    public boolean atualizar(Materia materia, int usuarioId) throws SQLException {
        String sql = "UPDATE materias SET nome = ?, descricao = ? " +
                     "WHERE id = ? AND usuario_id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, materia.getNome());
            ps.setString(2, materia.getDescricao());
            ps.setInt(3, materia.getId());
            ps.setInt(4, usuarioId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Remove uma matéria do banco.
     * O CASCADE no banco já remove exercícios e resumos vinculados.
     */
    public boolean deletar(int id, int usuarioId) throws SQLException {
        String sql = "DELETE FROM materias WHERE id = ? AND usuario_id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, usuarioId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Conta quantas matérias o usuário tem cadastradas.
     */
    public int contarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM materias WHERE usuario_id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // ----------------------------------------------------------------
    // Mapeia um ResultSet para um objeto Materia
    // ----------------------------------------------------------------

    private Materia mapear(ResultSet rs) throws SQLException {
        Materia m = new Materia();
        m.setId(rs.getInt("id"));
        m.setUsuarioId(rs.getInt("usuario_id"));
        m.setNome(rs.getString("nome"));
        m.setDescricao(rs.getString("descricao"));

        Timestamp ts = rs.getTimestamp("criado_em");
        if (ts != null) {
            m.setCriadoEm(ts.toLocalDateTime());
        }

        return m;
    }
}
