package com.studymate.dao;

import com.studymate.model.Exercicio;
import com.studymate.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco relacionadas a Exercicio.
 */
public class ExercicioDAO {

    /**
     * Salva uma lista de exercícios gerados pela IA de uma só vez.
     */
    public void salvarLote(List<Exercicio> exercicios) throws SQLException {
        String sql = "INSERT INTO exercicios (materia_id, enunciado, gabarito_ia, tipo) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (Exercicio e : exercicios) {
                ps.setInt(1, e.getMateriaId());
                ps.setString(2, e.getEnunciado());
                ps.setString(3, e.getGabaritoIa());
                ps.setString(4, e.getTipo());
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    /**
     * Lista todos os exercícios de uma matéria, do mais recente ao mais antigo.
     */
    public List<Exercicio> listarPorMateria(int materiaId) throws SQLException {
        String sql = "SELECT id, materia_id, enunciado, gabarito_ia, tipo, gerado_em " +
                     "FROM exercicios WHERE materia_id = ? ORDER BY gerado_em DESC";

        List<Exercicio> lista = new ArrayList<>();

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
     * Busca um exercício pelo ID.
     */
    public Exercicio buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, materia_id, enunciado, gabarito_ia, tipo, gerado_em " +
                     "FROM exercicios WHERE id = ?";

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
     * Conta quantos exercícios existem para uma matéria.
     */
    public int contarPorMateria(int materiaId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM exercicios WHERE materia_id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, materiaId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /**
     * Conta o total de exercícios de todas as matérias de um usuário.
     */
    public int contarTotalPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM exercicios e " +
                     "INNER JOIN materias m ON e.materia_id = m.id " +
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
    // Mapeia ResultSet → Exercicio
    // ----------------------------------------------------------------

    private Exercicio mapear(ResultSet rs) throws SQLException {
        Exercicio e = new Exercicio();
        e.setId(rs.getInt("id"));
        e.setMateriaId(rs.getInt("materia_id"));
        e.setEnunciado(rs.getString("enunciado"));
        e.setGabaritoIa(rs.getString("gabarito_ia"));
        e.setTipo(rs.getString("tipo"));

        Timestamp ts = rs.getTimestamp("gerado_em");
        if (ts != null) e.setGeradoEm(ts.toLocalDateTime());

        return e;
    }
}
