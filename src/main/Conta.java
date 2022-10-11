package main;

import java.io.*;

public class Conta implements Comparable<Conta> {
    protected int idConta;
    protected String nomePessoa;
    protected String email;
    protected String nomeUsuario;
    protected String senha;
    protected String cpf;
    protected String cidade;
    protected int transferenciasRealizadas;
    protected float saldoConta;

    public Conta() {
    }

    public Conta(int idConta, String nomePessoa, String email, String nomeUsuario, String senha,
                 String cpf, String cidade, int transferenciasRealizadas, float saldoConta) {
        this.idConta = idConta;
        this.nomePessoa = nomePessoa;
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.cpf = cpf;
        this.cidade = cidade;
        this.transferenciasRealizadas = transferenciasRealizadas;
        this.saldoConta = saldoConta;
    }

    public byte[] converteContaEmByte() throws IOException {
        //Este metodo converte a conta instanciada em um vetor de byte.
        ByteArrayOutputStream vetorByte = new ByteArrayOutputStream();//Escrita
        DataOutputStream buffer = new DataOutputStream(vetorByte);//Escrita|buffer?
        buffer.writeInt(idConta);
        buffer.writeUTF(nomePessoa);
        buffer.writeUTF(email);
        buffer.writeUTF(nomeUsuario);
        buffer.writeUTF(senha);
        buffer.writeUTF(cpf);
        buffer.writeUTF(cidade);
        buffer.writeInt(transferenciasRealizadas);
        buffer.writeFloat(saldoConta);
        return vetorByte.toByteArray();
    }

    public void decodificaByteArray(byte[] vetorByte) throws IOException {
        //Este metodo recebe um vetor de byte do arquivo e converte o mesmo em um objeto do tipo Conta.
        ByteArrayInputStream bufferParaLeitura = new ByteArrayInputStream(vetorByte);
        DataInputStream leitura = new DataInputStream(bufferParaLeitura);
        idConta = leitura.readInt();
        nomePessoa = leitura.readUTF();
        email = leitura.readUTF();
        nomeUsuario = leitura.readUTF();
        senha = leitura.readUTF();
        cpf = leitura.readUTF();
        cidade = leitura.readUTF();
        transferenciasRealizadas = leitura.readInt();
        saldoConta = leitura.readFloat();
    }

    public static Conta fromByteArray(byte[] byteArray) throws IOException {
        Conta conta = new Conta();
        DataInputStream leitura = new DataInputStream(new ByteArrayInputStream(byteArray));
        conta.idConta = leitura.readInt();
        conta.nomePessoa = leitura.readUTF();
        conta.email = leitura.readUTF();
        conta.nomeUsuario = leitura.readUTF();
        conta.senha = leitura.readUTF();
        conta.cpf = leitura.readUTF();
        conta.cidade = leitura.readUTF();
        conta.transferenciasRealizadas = leitura.readInt();
        conta.saldoConta = leitura.readFloat();
        return conta;
    }

//    @Override
//    public String toString() {
//        return "Conta [idConta=" + idConta + ", nomePessoa=" + nomePessoa + ", email=" + email
//                + ", nomeUsuario=" + nomeUsuario + ", senha=" + senha + ", cpf=" + cpf + ", cidade="
//                + cidade + ", transferenciasRealizadas=" + transferenciasRealizadas
//                + ", saldoConta=" + saldoConta + "]";
//    }

    @Override
    public String toString() {
        return "Conta{" +
                "idConta=" + idConta +
                '}';
    }

    @Override
    public int compareTo(Conta o) {
        return this.idConta - o.idConta;
    }
}
