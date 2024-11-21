package agenciabancaria;

import Utilitarios.Utils;
import javax.swing.JOptionPane;

public class Conta {
	
	//definir automaticando um número para a conta toda vez que for criada
	private static int contadorConta = 1;
	
	private int numeroConta;
	private Pessoa pessoa;
	private Double saldo = 0.0;
	
	//Metodo Construtor
	public Conta(Pessoa pessoa) {
		this.numeroConta = contadorConta;
		this.pessoa = pessoa;
		
		contadorConta += 1;
	}

	//Get Set
	public int getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(int numeroConta) {
		this.numeroConta = numeroConta;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	
	public String toString() {
		return "\nNúmero da Conta: " + this.getNumeroConta() +
				"\nNome: " + this.pessoa.getNome() +
				"\nCPF: " + this.pessoa.getCpf() +
				"\nE-mail: " + this.pessoa.getEmail() +
				"\nSaldo: " + Utils.doubleToString(this.getSaldo()) +
				"\n";
	}
	
	////OPERAÇÕES
	
	// 1- Depositar
	public void depositar(Double valor) {
		if(valor > 0) {
			setSaldo(getSaldo() +valor);
                        JOptionPane.showMessageDialog(null, "Seu deposito foi realizado com sucesso!");
		}else {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar deposito!\n\n*Não é possivel realizar deposito negativo*");
		}
	}
	
	// 2- Sacar
	public void sacar(Double valor) {
		if(valor > 0 && this.getSaldo() >= valor) {
			setSaldo(getSaldo() - valor);
                        JOptionPane.showMessageDialog(null, "Seu saque foi realizado com sucesso!");
		}else {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar saque!\n\t*Saldo insuficiente*");
		}
	}
	
	// 3- Transferir
	public void transferir(Conta contaDeposito ,Double valor) {
		if(valor > 0 && this.getSaldo() >= valor) {
			setSaldo(getSaldo() - valor);
			contaDeposito.saldo = contaDeposito.getSaldo() + valor;
                        JOptionPane.showMessageDialog(null, "Sua transferência foi realizado com sucesso!");
		}else {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar transferência!\n\t*Saldo insuficiente*");
		}
	}
	
}
