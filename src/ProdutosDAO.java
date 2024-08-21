/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void venderProduto(int produtoId) {
        String checkStatusSql = "SELECT status FROM produtos WHERE id = ?";
        String updateSql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
    
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/uc11", "root", "123")) {
            // Primeiro, verifique o status atual do produto
            try (PreparedStatement checkStmt = conn.prepareStatement(checkStatusSql)) {
                checkStmt.setInt(1, produtoId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("status");

                        // Verifique se o status já é "Vendido"
                        if ("Vendido".equalsIgnoreCase(status)) {
                            JOptionPane.showMessageDialog(null, "Este produto já está vendido.");
                            return; // Sai do método se o produto já estiver vendido
                        }
                    } else {
                        // Se o produto não foi encontrado, informa o usuário
                        JOptionPane.showMessageDialog(null, "Produto não encontrado.");
                        return; // Sai do método se o produto não existir
                    }
                }
            }

        // Se o produto não estiver vendido, procede com a atualização
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, produtoId);
            int rowsAffected = updateStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Produto vendido com sucesso!");
            }
        }
        
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao vender o produto: " + e.getMessage(), e);
    }
}
    
    public void cadastrarProduto (ProdutosDTO produto){
        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/uc11", "root", "123");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produto.getNome());
            pstmt.setInt(2, produto.getValor());
            pstmt.setString(3, produto.getStatus());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar o produto: " + e.getMessage(), e);
        }
    }
    
    public List<ProdutosDTO> listarProdutos() {
        
        List<ProdutosDTO> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
    
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/uc11", "root", "123");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));
                produtos.add(produto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
        return produtos;
    }
}

