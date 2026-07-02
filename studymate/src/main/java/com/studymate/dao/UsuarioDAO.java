package com.studymate.dao;

import com.studymate.model.Usuario;
import com.studymate.util.ConexaoDB;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * DAO responsável pelas operações de banco relacionadas ao Usuario.
 */
public class UsuarioDAO {

    /**
     * Cadastra um novo usuário com a senha já encriptada via BCrypt.
     * Retorna false se o e-mail já estiver cadastrado.
     */
    public boolean cadastrar(String nome, String email, String senhaPura) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha_hash) VALUES (?, ?, ?)";

        String senhaHash = BCrypt.hashpw(senhaPura, BCrypt.gensalt(12));

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senhaHash);
            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            // E-mail duplicado (UNIQUE no banco)
            return false;
        }
    }

    /**
     * Busca um usuário pelo e-mail e verifica a senha com BCrypt.
     * Retorna o Usuario se as credenciais forem válidas, ou null caso contrário.
     */
    public Usuario autenticar(String email, String senhaPura) throws SQLException {
        String sql = "SELECT id, nome, email, senha_hash FROM usuarios WHERE email = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashSalvo = rs.getString("senha_hash");

                    if (BCrypt.checkpw(senhaPura, hashSalvo)) {
                        Usuario u = new Usuario();
                        u.setId(rs.getInt("id"));
                        u.setNome(rs.getString("nome"));
                        u.setEmail(rs.getString("email"));

                        atualizarUltimoAcesso(u.getId(), con);
                        return u;
                    }
                }
            }
        }

        return null; // credenciais inválidas
    }

    /**
     * Atualiza o campo ultimo_acesso do usuário no banco.
     */
    private void atualizarUltimoAcesso(int usuarioId, Connection con) throws SQLException {
        String sql = "UPDATE usuarios SET ultimo_acesso = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        }
    }

    /**
     * Verifica se um e-mail já está cadastrado no banco.
     */
    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE email = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
