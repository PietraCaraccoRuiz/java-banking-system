package agenciabancaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*CREATE TABLE pessoas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100)
);

CREATE TABLE contas (
    numero_conta INT AUTO_INCREMENT PRIMARY KEY,
    pessoa_id INT NOT NULL,
    saldo DECIMAL(10, 2) DEFAULT 0.0,
    FOREIGN KEY (pessoa_id) REFERENCES pessoas(id) ON DELETE CASCADE
);
 */
public class ConexaoBD {

    private Connection conexao;

    public ConexaoBD() {
        try {
            // Estabelecer conexão com o banco de dados
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexao = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/agencia",
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
            PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Recuperar o ID gerado
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getCpf());
            stmt.setString(3, pessoa.getEmail());

            stmt.executeUpdate();

            // Recuperar o ID gerado automaticamente
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                pessoa.setId(generatedKeys.getInt(1)); // Atualiza o ID da pessoa no objeto
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para salvar uma conta
    public void salvarConta(Conta conta) {
        try {
            // Garantir que a pessoa esteja salva primeiro (o id será gerado automaticamente)
            salvarPessoa(conta.getPessoa());

            // A conta será salva sem o número da conta (ele será gerado automaticamente)
            String sql = "INSERT INTO contas (pessoa_id, saldo) VALUES (?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, conta.getPessoa().getId());
            stmt.setDouble(2, conta.getSaldo());

            // Executar o comando de inserção
            stmt.executeUpdate();

            // Recuperar o número da conta gerado automaticamente
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                conta.setNumeroConta(rs.getInt(1));  // Recupera o número gerado automaticamente
            }

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
    // Método para excluir uma conta
    public void excluirConta(Conta conta) {
        try {
            // Primeiro, buscar o ID da pessoa associada à conta
            String sqlBuscarPessoa = "SELECT pessoa_id FROM contas WHERE numero_conta = ?";
            PreparedStatement stmtBuscarPessoa = conexao.prepareStatement(sqlBuscarPessoa);
            stmtBuscarPessoa.setInt(1, conta.getNumeroConta());
            ResultSet rs = stmtBuscarPessoa.executeQuery();

            int pessoaId = -1;
            if (rs.next()) {
                pessoaId = rs.getInt("pessoa_id");
            }
            stmtBuscarPessoa.close();

            if (pessoaId == -1) {
                System.err.println("Pessoa associada à conta não encontrada.");
                return;
            }

            // Excluir a conta
            String sqlConta = "DELETE FROM contas WHERE numero_conta = ?";
            PreparedStatement stmtConta = conexao.prepareStatement(sqlConta);
            stmtConta.setInt(1, conta.getNumeroConta());
            stmtConta.executeUpdate();
            stmtConta.close();

            // Excluir a pessoa associada
            String sqlPessoa = "DELETE FROM pessoas WHERE id = ?";
            PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa);
            stmtPessoa.setInt(1, pessoaId);
            stmtPessoa.executeUpdate();
            stmtPessoa.close();

            System.out.println("Conta e pessoa associada excluídas com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir conta e pessoa associada: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para buscar todas as contas do banco de dados
    public ArrayList<Conta> buscarTodasContas() {
        ArrayList<Conta> contas = new ArrayList<>();
        try {
            String sql = "SELECT c.numero_conta, c.saldo, p.nome, p.cpf, p.email "
                    + "FROM contas c "
                    + "JOIN pessoas p ON c.pessoa_id = p.id";
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
