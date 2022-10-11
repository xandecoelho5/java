package main;

import java.util.Scanner;

public class App {
    public static String nomeArquivo = "output/conta.db";

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 31; i++) {
            Crud.writeAccount(Crud.createAccount());
        }
        Crud.listAccouts();

//        Scanner scanner = new Scanner(System.in);
        // System.out.println(Crud.listAccouts());
        // int opcao = -1;
        // while (opcao!=0) {
        //     menu();
        //     opcao = Integer.parseInt(scanner.nextLine());
        // }
    }

    public static void menu() {
        System.out.println("\nMENU:");
        System.out.println("1- Criar conta");
        System.out.println("2- Realizar uma transferencia");
        System.out.println("3- Ler um registro por ID");
        System.out.println("4- Atualizar um registro");
        System.out.println("5- Deletar um registro");
    }
}
