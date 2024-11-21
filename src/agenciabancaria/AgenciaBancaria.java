package agenciabancaria;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class AgenciaBancaria {

    // Adicionar um gerenciador de banco de dados
    static ConexaoBD conexaoBD;
    static ArrayList<Conta> contasBancarias;

    public static void main(String[] args) {
        try {
            // Inicializar o gerenciador de banco de dados
            conexaoBD = new ConexaoBD();  // Initialize the static variable
            contasBancarias = new ArrayList<Conta>();

            // Carregar contas existentes do banco de dados
            contasBancarias = conexaoBD.buscarTodasContas();  // Use the initialized conexaoBD

            operacoes();
        } finally {
            // Garantir que a conexão seja fechada mesmo se houver um erro
            if (conexaoBD != null) {
                conexaoBD.fecharConexao();
            }
        }
    }

    public static void operacoes() {
        int operacao = Integer.parseInt(JOptionPane.showInputDialog("Selecione uma operação:" + 
                "\n1 - Criar conta" + 
                "\n2 - Depositar" + 
                "\n3 - Sacar" + 
                "\n4 - Transferir" + 
                "\n5 - Excluir Conta" + 
                "\n6 - Listar" + 
                "\n7 - Sair"));

        switch (operacao) {
            case 1:
                criarConta();
                break;
            case 2:
                depositar();
                break;
            case 3:
                sacar();
                break;
            case 4:
                transferir();
                break;
            case 5:
                excluirConta();
                break;
            case 6:
                listarContas();
                break;
            case 7:
                JOptionPane.showMessageDialog(null, "Encerrando Operação");
                System.exit(0);
            default:
                JOptionPane.showMessageDialog(null, "Operação Inválida!");
                operacoes();
                break;
        }
    }

    public static void criarConta() {
        Pessoa p = new Pessoa();

        p.setNome(JOptionPane.showInputDialog("Nome: "));
        p.setCpf(JOptionPane.showInputDialog("CPF: "));
        p.setEmail(JOptionPane.showInputDialog("E-mail: "));

        Conta c = new Conta(p);

        // Salvar conta no banco de dados
        conexaoBD.salvarConta(c);

        // Adicionar conta
        contasBancarias.add(c);

        JOptionPane.showMessageDialog(null, "Conta criada com sucesso!");

        operacoes();
    }

    private static Conta encontrarConta(int numeroConta) {
        Conta conta = null;
        // Verifica se existe conta na lista
        if (contasBancarias.size() > 0) {
            // Percorrer lista
            for (Conta c : contasBancarias) {
                // Verifica se numero da conta é igual ao parametro
                if (c.getNumeroConta() == numeroConta) {
                    conta = c;
                }
            }
        }
        return conta;
    }

    public static void depositar() {
        int numeroConta = Integer.parseInt(JOptionPane.showInputDialog("Número da conta para deposito: "));
        Conta c = encontrarConta(numeroConta);

        // Verificar se conta existe
        if (c != null) {
            Double valorDeposito = Double.parseDouble(JOptionPane.showInputDialog("Qual valor deseja depositar: "));
            c.depositar(valorDeposito);

            // Atualizar saldo no banco de dados
            conexaoBD.atualizarSaldo(c);
        } else {
            JOptionPane.showMessageDialog(null, "Conta não encontrada!");
        }

        operacoes();
    }

    public static void sacar() {
        int numeroConta = Integer.parseInt(JOptionPane.showInputDialog("Número da conta: "));
        Conta c = encontrarConta(numeroConta);

        // Verificar se conta existe
        if (c != null) {
            Double valorSaque = Double.parseDouble(JOptionPane.showInputDialog("Qual valor deseja sacar: "));
            c.sacar(valorSaque);

            // Atualizar saldo no banco de dados
            conexaoBD.atualizarSaldo(c);
        } else {
            JOptionPane.showMessageDialog(null, "Conta não encontrada!");
        }

        operacoes();
    }

    public static void transferir() {
        int contaRemetente = Integer.parseInt(JOptionPane.showInputDialog("Número da conta do remetente: "));
        Conta cr = encontrarConta(contaRemetente);

        // Verificar se conta existe
        if (cr != null) {
            int contaDestinatario = Integer.parseInt(JOptionPane.showInputDialog("Número da conta do destinatário: "));
            Conta cd = encontrarConta(contaDestinatario);

            if (cd != null) {
                Double valorTransferencia = Double.parseDouble(JOptionPane.showInputDialog("Qual valor da transferência: "));
                cr.transferir(cd, valorTransferencia);

                // Atualizar saldos no banco de dados
                conexaoBD.atualizarSaldo(cr);
                conexaoBD.atualizarSaldo(cd);
            } else {
                JOptionPane.showMessageDialog(null, "Conta destinatário não encontrada!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Conta remetente não encontrada!");
        }

        operacoes();
    }

    public static void excluirConta() {
        int numeroConta = Integer.parseInt(JOptionPane.showInputDialog("Número da conta a ser excluída:"));
        Conta c = encontrarConta(numeroConta);

        if (c != null) {
                // Remover conta do banco de dados
                conexaoBD.excluirConta(c);

                // Remover conta da lista local
                contasBancarias.remove(c);

                JOptionPane.showMessageDialog(null, "Conta excluída com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Conta inválida!");
            }

        operacoes();
    }

    public static void listarContas() {
        // Recarregar contas do banco de dados
        contasBancarias = conexaoBD.buscarTodasContas();

        // Verifica se existe conta na lista
        if (contasBancarias.size() > 0) {
            for (Conta c : contasBancarias) {
                JOptionPane.showMessageDialog(null, c);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Não há nenhuma conta criada!");
        }

        operacoes();
    }
}
