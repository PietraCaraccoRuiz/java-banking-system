package agenciabancaria;

public class Pessoa {

    private int id;
    private String nome;
    private String cpf;
    private String email;

    public Pessoa() {
    }

// Construtor parametrizado
    public Pessoa(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "\nId:" + this.getId()
                + "\nNome: " + this.getNome()
                + "\nCPF: " + this.getCpf()
                + "\nEmail: " + this.getEmail();
    }

}
