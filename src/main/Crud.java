package main;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crud {
    static final String nomeArquivo = "output/conta.db";
    static int pos = 0;

    private Crud() {
    }

    public static void writeAccount(Conta conta) {
        try (RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw")) {
            byte[] array;
            array = conta.converteContaEmByte();
            arquivo.seek(0);
            arquivo.writeInt(conta.idConta);
            arquivo.seek(arquivo.length());
            arquivo.writeChar(' ');
            arquivo.writeInt(array.length);
            arquivo.write(array);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao criar conta!");
        }
    }

    public static int getLastId() {
        int id;
        File f = new File(nomeArquivo);
        if (f.exists() && !f.isDirectory()) {
            try (RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw")) {
                arquivo.seek(0);
                id = arquivo.readInt();
                return ++id;
            } catch (Exception e) {
                System.out.println("Erro ao obter ultimo ID.");
            }
        }
        return 0;
    }

    public static List<Conta> listAccouts() {
        ArrayList<Conta> contas = new ArrayList<>();
        byte[] array;
        char lapide;
        try (RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw")) {
            arquivo.seek(4);
            while (arquivo.getFilePointer() < arquivo.length()) {
                lapide = arquivo.readChar();
                array = new byte[arquivo.readInt()];
                arquivo.read(array);
                if (lapide != '*') {
                    Conta conta = Conta.fromByteArray(array);
                    contas.add(conta);
                    System.out.print(conta.idConta + ",");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listAccouts." + e.getMessage());
            e.printStackTrace();
        }
        return contas;
    }

    public static Conta readById(int id) {
        var contas = listAccouts();
        for (Conta conta : contas) {
            if (conta.idConta == id) {
                return conta;
            }
        }
        System.out.println("Conta não encontrada!");
        return null;
    }

    public static boolean update(Conta conta) {
        try (RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw")) {
            char lapide;
            long posRegistro;
            int tamanhoReg;
            byte[] buffer;
            byte[] novoRegBuf;
            arquivo.seek(4);
            while (arquivo.getFilePointer() != -1) {
                posRegistro = arquivo.getFilePointer();
                lapide = arquivo.readChar();
                tamanhoReg = arquivo.readInt();
                buffer = new byte[tamanhoReg];
                arquivo.read(buffer);
                if (lapide != '*') {
                    Conta conta2 = new Conta();
                    conta2.decodificaByteArray(buffer);
                    if (conta.idConta == conta2.idConta) {
                        novoRegBuf = conta.converteContaEmByte();
                        if (novoRegBuf.length <= buffer.length) {
                            arquivo.seek(posRegistro + 6);
                            arquivo.write(novoRegBuf);
                        } else {
                            arquivo.seek(posRegistro);
                            arquivo.writeChar('*');
                            arquivo.seek(arquivo.length());
                            arquivo.writeChar(' ');
                            arquivo.write(novoRegBuf);
                        }
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("Erro desgraca!");
        }
        return false;

    }

    public static boolean delete(int id) {
        try (RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw")) {
            long pos;
            char lapide;
            int tamanho;
            byte[] array;
            Conta conta;
            arquivo.seek(4);
            while (arquivo.getFilePointer() != -1) {
                pos = arquivo.getFilePointer();
                lapide = arquivo.readChar();
                tamanho = arquivo.readInt();
                array = new byte[tamanho];
                arquivo.read(array);
                if (lapide != '*') {
                    conta = new Conta();
                    conta.decodificaByteArray(array);
                    if (conta.idConta == id) {
                        arquivo.seek(pos);
                        arquivo.writeChar('*');
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Erro ao deletar");
            return false;
        }
    }

    public static boolean transferencia(Conta contaOrigem, Conta contaDestino, Float valor) {
        contaOrigem.transferenciasRealizadas++;
        contaOrigem.saldoConta = contaDestino.saldoConta - valor;
        contaDestino.transferenciasRealizadas++;
        contaDestino.saldoConta = contaDestino.saldoConta + valor;
        return update(contaOrigem) && update(contaDestino);
    }

    public static Conta createAccount() {
        int id = (int) (Math.random() * 100);
        var l = List.of(5, 28, 10, 40, 35, 7, 12, 2, 21, 11, 29, 27, 9, 38, 8, 49, 3, 15, 13, 30, 17, 46, 18, 36, 1, 4, 34, 16, 19, 22, 20);

        return new Conta(l.get(pos++), "andrei", "mail", "nomeUser", "senha", "123", "ita", 0, 2f);
    }

    public static Conta createAccount2() {
        return new Conta(1, "andreiCaralho", "mail", "nomeUser", "senha", "123", "ita", 0, 2f);
    }
}
