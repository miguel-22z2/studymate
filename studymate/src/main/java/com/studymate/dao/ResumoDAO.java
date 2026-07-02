package com.studymate.dao;

import com.studymate.model.Resumo;
import com.studymate.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco relacionadas a Resumo.
 */
public class ResumoDAO {

    /**
     * Salva um novo resumo no banco e retorna o ID gerado.
     */
    public int salvar(Resumo resumo) throws SQLException {
        String sql = "INSERT INTO resumos (materia_id, conteudo_raw, resumo_ia) VALUES (?, ?, ?)";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, resumo.getMateriaId());
            ps.setString(2, resumo.getConteudoRaw());
            ps.setString(3, resumo.getResumoIa());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        throw new SQLException("Falha ao salvar resumo.");
    }

    /**
     * Lista todos os resumos de uma matéria, do mais recente ao mais antigo.
     */
    public List<Resumo> listarPorMateria(int materiaId) throws SQLException {
        String sql = "SELECT id, materia_id, conteudo_raw, resumo_ia, gerado_em " +
                     "FROM resumos WHERE materia_id = ? ORDER BY gerado_em DESC";

        List<Resumo> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, materiaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }

        return lista;
    }

    /**
     * Busca um resumo pelo ID.
     */
    public Resumo buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, materia_id, conteudo_raw, resumo_ia, gerado_em " +
                     "FROM resumos WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }

        return null;
    }

    /**
     * Remove um resumo pelo ID.
     */
    public boolean deletar(int id, int materiaId) throws SQLException {
        String sql = "DELETE FROM resumos WHERE id = ? AND materia_id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, materiaId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Conta quantos resumos existem para uma matéria.
     */
    public int contarPorMateria(int materiaId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM resumos WHERE materia_id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, materiaId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /**
     * Conta o total de resumos gerados por um usuário.
     */
    public int contarTotalPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM resumos r " +
                     "INNER JOIN materias m ON r.materia_id = m.id " +
                     "WHERE m.usuario_id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // ----------------------------------------------------------------
    // Mapeia ResultSet → Resumo
    // ----------------------------------------------------------------

    private Resumo mapear(ResultSet rs) throws SQLException {
        Resumo r = new Resumo();
        r.setId(rs.getInt("id"));
        r.setMateriaId(rs.getInt("materia_id"));
        r.setConteudoRaw(rs.getString("conteudo_raw"));
        r.setResumoIa(rs.getString("resumo_ia"));

        Timestamp ts = rs.getTimestamp("gerado_em");
        if (ts != null) r.setGeradoEm(ts.toLocalDateTime());

        return r;
    }
}
