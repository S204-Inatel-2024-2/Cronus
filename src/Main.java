import java.sql.SQLException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException
    {
        Scanner input = new Scanner(System.in);
        boolean running = true; // para manter o menu rodando

        while (running)
        {
            System.out.println("\n--- Bem-vindo! ---");
            System.out.println("1. Cadastro");
            System.out.println("2. Login");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = input.nextInt();
            input.nextLine();  // Consome o newline após o número\

            switch (opcao)
            {
                case 1:
                    // Cadastro
                    System.out.println("\n--- Cadastro de Usuário ---");
                    System.out.printf("Usuário: ");
                    String nomeCadastro = input.nextLine();

                    System.out.printf("Email: ");
                    String emailCadastro = input.nextLine();

                    System.out.printf("Senha: ");
                    String senhaCadastro = input.nextLine();

                    // Realiza o cadastro
                    CadastroUsuario cadastro = new CadastroUsuario();
                    cadastro.cadastrar(nomeCadastro, emailCadastro, senhaCadastro);
                    break;

                case 2:
                    // Login
                    System.out.println("\n--- Login de Usuário ---");

                    System.out.printf("Usuário: ");
                    String nomeLogin = input.nextLine();

                    System.out.printf("Email: ");
                    String emailLogin = input.nextLine();

                    System.out.printf("Senha: ");
                    String senhaLogin = input.nextLine();

                    // Realiza o login
                    LoginUsuario login = new LoginUsuario();
                    login.login(nomeLogin, emailLogin, senhaLogin);

                    break;

                case 3:
                    // Sair
                    System.out.println("Saindo...");
                    running = false;
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
