package agenciabancaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConexaoBD {
    private Connection conexao;

    public ConexaoBD() {
        try {
            // Estabelecer conexão com o banco de dados
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexao = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/agenciabancaria", 
                "root", 
                ""
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    // Método para salvar uma pessoa
    public void salvarPessoa(Pessoa pessoa) {
        try {
            String sql = "INSERT INTO pessoas (nome, cpf, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getCpf());
            stmt.setString(3, pessoa.getEmail());
            
            stmt.executeUpdate();
            
            // Recuperar o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                // Você pode definir o ID gerado na pessoa se quiser
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para salvar uma conta
    public void salvarConta(Conta conta) {
        try {
            // Primeiro, garantir que a pessoa esteja salva
            salvarPessoa(conta.getPessoa());
            
            String sql = "INSERT INTO contas (numero_conta, pessoa_id, saldo) " +
                         "SELECT ?, id, ? FROM pessoas WHERE cpf = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, conta.getNumeroConta());
            stmt.setDouble(2, conta.getSaldo());
            stmt.setString(3, conta.getPessoa().getCpf());
            
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar o saldo da conta
    public void atualizarSaldo(Conta conta) {
        try {
            String sql = "UPDATE contas SET saldo = ? WHERE numero_conta = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setDouble(1, conta.getSaldo());
            stmt.setInt(2, conta.getNumeroConta());
            
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Método para excluir uma conta
public void excluirConta(Conta conta) {
    try {
        // Excluir a conta pelo número da conta
        String sql = "DELETE FROM contas WHERE numero_conta = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, conta.getNumeroConta());
        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Conta excluída com sucesso!");
        } else {
            System.out.println("Nenhuma conta encontrada com o número fornecido.");
        }

        stmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Método para buscar todas as contas do banco de dados
    public ArrayList<Conta> buscarTodasContas() {
        ArrayList<Conta> contas = new ArrayList<>();
        try {
            String sql = "SELECT c.numero_conta, c.saldo, p.nome, p.cpf, p.email " +
                         "FROM contas c " +
                         "JOIN pessoas p ON c.pessoa_id = p.id";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("nome"));
                pessoa.setCpf(rs.getString("cpf"));
                pessoa.setEmail(rs.getString("email"));
                
                Conta conta = new Conta(pessoa);
                conta.setNumeroConta(rs.getInt("numero_conta"));
                conta.setSaldo(rs.getDouble("saldo"));
                
                contas.add(conta);
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contas;
    }

    // Fechar conexão
    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}