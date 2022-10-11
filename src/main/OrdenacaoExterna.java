package main;

import java.io.RandomAccessFile;
import java.util.*;

public class OrdenacaoExterna {
    static final String nomeArquivo = "output/conta.db";
    static final String PREFIXO = ".db";
    static final int ramSize = 4;
    static long ptrControl = 4;

    public static void main(String[] args) {
        int tamanho = 4;
        int caminhos = 2;
        dist(tamanho, caminhos);
//        System.out.print("tmp0.db -> ");
//        listAccouts("output/tmp0.db");
//        System.out.println("\n---------------");
//        System.out.print("tmp1.db -> ");
//        listAccouts("output/tmp1.db");
//        System.out.println("\n---------------");
        int limite = (int) Math.pow(caminhos, tamanho);
        boolean isBase = true;
        while (tamanho <= limite) {
            intercalacao(tamanho, caminhos, isBase);
            System.out.print("tmp0.db -> ");
            listAccouts("output/tmp0.db");
            System.out.println("\n---------------");
            System.out.print("tmp1.db -> ");
            listAccouts("output/tmp1.db");
            System.out.println("\n---------------");
            System.out.print("tmp2.db -> ");
            listAccouts("output/tmp2.db");
            System.out.println("\n---------------");
            System.out.print("tmp3.db -> ");
            listAccouts("output/tmp3.db");
            System.out.println("\n---------------");
            tamanho *= caminhos;
            isBase = !isBase;
        }
    }

    public static void dist(int tam, int caminhos) {
        try {
            List<Conta> contas = new ArrayList<>();
            RandomAccessFile[] temp = new RandomAccessFile[caminhos];
            for (int i = 0; i < caminhos; i++) {
                temp[i] = new RandomAccessFile("output/tmp" + i + PREFIXO, "rw");
            }
            while (ptrControl != -1) {
                for (int i = 0; i < caminhos; i++) {
                    for (int j = 0; j < tam; j++) {
                        var conta = readFile(nomeArquivo);
//                        System.out.println(conta);
                        if (conta != null) {
                            contas.add(conta);
                        }
                    }
                    Collections.sort(contas);
                    for (Conta conta : contas) {
                        byte[] ba = conta.converteContaEmByte();
                        temp[i].writeChar(' ');
                        temp[i].writeInt(ba.length);
                        temp[i].write(ba);
                    }
                    contas.clear();
                }
            }
            for (var t : temp) {
                t.close();
            }
        } catch (Exception e) {
            System.out.println("Erro dist. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void intercalacao(int tam, int caminhos, boolean isBase) {
        CustomFile[] temp1 = new CustomFile[caminhos];
        for (int i = 0; i < caminhos; i++) {
            temp1[i] = new CustomFile("output/tmp" + (isBase ? i : i + caminhos) + PREFIXO);
        }

        CustomFile[] temp2 = new CustomFile[caminhos];
        for (int i = 0; i < caminhos; i++) {
            temp2[i] = new CustomFile("output/tmp" + (isBase ? i + caminhos : i) + PREFIXO);
        }

        Map<CustomFile, Conta> map = new HashMap<>();
        int tempPos = 0;
        try {
            while (true) {
                for (int i = 0; i < caminhos; i++) {
                    if (map.get(temp1[i]) == null && temp1[i].readRegisterSize < tam) {
                        Conta conta = temp1[i].readNext();
                        if (conta != null) {
                            map.put(temp1[i], conta);
                        }
                    }
                }

                if (map.isEmpty()) break;

                var ordered = map.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue()).toList();
                var firstConta = ordered.get(0);

                temp2[tempPos].writeConta(firstConta.getValue());
                if (temp2[tempPos].size % (tam * 2) == 0) {
                    tempPos++;
                    if (tempPos == caminhos) {
                        tempPos = 0;
                    }
                    for (var t : temp1) { // limpa a quantidade de registros lidos
                        t.readRegisterSize = 0;
                    }
                }
                map.remove(firstConta.getKey());
            }
        } catch (Exception e) {
            System.out.println("Erro intercalação. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Conta readFile(String fileName) {
        char lapide;
        int tamanho;
        byte[] ba;
        try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
            if (ptrControl < file.length() && ptrControl != -1) {
                file.seek(ptrControl);
                lapide = file.readChar();
                tamanho = file.readInt();
                ba = new byte[tamanho];
                file.read(ba);
                ptrControl = file.getFilePointer();
                if (lapide != '*') {
                    return Conta.fromByteArray(ba);
                }
            } else {
                ptrControl = -1;
            }
        } catch (Exception e) {
            System.out.println("Erro readFile. " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static List<Conta> listAccouts(String nome) {
        ArrayList<Conta> contas = new ArrayList<>();
        byte[] array;
        char lapide;

        try (RandomAccessFile arquivo = new RandomAccessFile(nome, "rw")) {
            while (arquivo.getFilePointer() < arquivo.length()) {
                lapide = arquivo.readChar();
                array = new byte[arquivo.readInt()];
                arquivo.read(array);
                if (lapide != '*') {
                    Conta conta = Conta.fromByteArray(array);
                    System.out.print(conta.idConta + " ");
                    contas.add(conta);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar contas. " + e.getMessage());
        }
        return contas;
    }

    public void readRegister(RandomAccessFile file) {
        char lapide;
        int tamanho;
        byte ba[];
        try {
            while (file.getFilePointer() != -1) {
                lapide = file.readChar();
                tamanho = file.readInt();
                ba = new byte[tamanho];
                Conta conta = new Conta();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
