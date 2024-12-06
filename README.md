# Sistema de Agência Bancária

Um sistema bancário simples desenvolvido em Java que permite realizar operações bancárias básicas com interface gráfica usando JOptionPane e persistência de dados em MySQL. O projeto foi criado com base na [video-aula](https://youtu.be/AExKQiCqwGs?si=fT03BU5TVp7NKgL1) e adaptado para incluir novas funcionalidades e melhorias.

## 📋 Funcionalidades

- Criar conta bancária
- Realizar depósitos
- Efetuar saques
- Fazer transferências entre contas
- Excluir contas
- Listar todas as contas cadastradas
- Persistência de dados em banco MySQL

## 🔧 Pré-requisitos

- Java JDK 8 ou superior
- MySQL 5.7 ou superior
- Connector/J (MySQL JDBC Driver)
- IDE Java de sua preferência (Eclipse, NetBeans, IntelliJ)

## 🛠️ Configuração do Banco de Dados

1. Crie um banco de dados chamado `agenciabancaria`
2. Execute os seguintes comandos SQL:

```sql
CREATE TABLE pessoas (
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
```

3. Configure as credenciais do banco no arquivo `ConexaoBD.java`:
```java
conexao = java.sql.DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/agenciabancaria", 
    "seu_usuario", 
    "sua_senha"
);
```

## 📦 Estrutura do Projeto

- `AgenciaBancaria.java` - Classe principal com o menu de operações
- `Conta.java` - Classe que representa uma conta bancária
- `Pessoa.java` - Classe que representa um cliente
- `ConexaoBD.java` - Classe responsável pela conexão e operações com o banco de dados

## 🚀 Como Executar

1. Clone o repositório:
```bash
git clone [https://github.com/PietraCaraccoRuiz/java-banking-system.git]
```

2. Importe o projeto em sua IDE

3. Adicione o MySQL Connector/J ao classpath do projeto

4. Configure o banco de dados conforme instruções acima

5. Execute a classe `AgenciaBancaria.java`

## 💰 Funcionalidades Detalhadas

### Criar Conta
- Solicita nome, CPF e email do cliente
- Gera número de conta automaticamente
- Salva informações no banco de dados

### Depósito
- Requer número da conta
- Valida se a conta existe
- Não permite valores negativos
- Atualiza saldo no banco de dados

### Saque
- Requer número da conta
- Verifica se há saldo suficiente
- Não permite valores negativos ou maiores que o saldo
- Atualiza saldo no banco de dados

### Transferência
- Requer conta de origem e destino
- Verifica existência das contas
- Valida saldo suficiente
- Atualiza saldos no banco de dados

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## ✨ Melhorias Futuras

- Implementar autenticação de usuários
- Adicionar histórico de transações
- Implementar diferentes tipos de contas (poupança, corrente)
- Adicionar validação de CPF
- Melhorar a interface gráfica com Swing ou JavaFX

## ✒️ Autor

Pietra Caracco Ruiz - [GitHub](https://github.com/PietraCaraccoRuiz)