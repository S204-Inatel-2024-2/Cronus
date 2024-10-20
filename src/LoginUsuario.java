import ConexaoBD.ConnectionBD;
import jdk.jshell.spi.SPIResolutionException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class LoginUsuario
{
    public void login(String nome, String email, String senha)
    {
        try
        {
            String query = "SELECT id, password from usuario WHERE nickname = ? AND email = ?";
            PreparedStatement statement = ConnectionBD.getConexao().prepareStatement(query);

            statement.setString(1, nome);
            statement.setString(2, email);

            ResultSet rs = statement.executeQuery();

            if(rs.next())
            {
                String password = rs.getString("password");

                if(checkPassword(senha, password))
                {
                    System.out.println("Login feito com sucesso!");

                    //---------------------------------------------------------------------------------
                    Scanner input =  new Scanner(System.in);
                    System.out.println("//---------------------------------------");
                    System.out.println("--- Vamos criar seu cronograma ---");

                    boolean flag = true;
                    while(flag)
                    {
                        System.out.println("1. Adicionar Atividade");
                        System.out.println("2. Editar Atividade");
                        System.out.println("3. Excluir Atividade");
                        System.out.println("4. Sair");
                        System.out.print("Escolha uma opção: ");
                        int opcao = input.nextInt();
                        input.nextLine();


                        switch(opcao)
                        {
                            case 1:
                                System.out.printf("\nTítulo da atividade: ");
                                String titulo = input.nextLine();

                                System.out.printf("Descrição da atividade: ");
                                String descricao = input.nextLine();

                                System.out.printf("Data: ");
                                String data = input.nextLine();

                                System.out.printf("Hora (HH:mm): ");
                                String hora = input.nextLine();

                                String getUserId = rs.getString("id");
                                // Chamar o método para adicionar atividade
                                Atividades atividade = new Atividades();
                                atividade.adicionarAtividade(getUserId, titulo, descricao, data, hora);
                                break;

                            case 2:

                                     String idUsuarioAtividadesUpdate = rs.getString("id");
                                     System.out.println("\n");
                                    try
                                    {
                                        String query1 = "SELECT * FROM atividades WHERE id_usuario = ?";
                                        PreparedStatement statement1 = ConnectionBD.getConexao().prepareStatement(query1);

                                        statement1.setString(1, idUsuarioAtividadesUpdate);

                                        ResultSet rs1 = statement1.executeQuery();

                                        while(rs1.next())
                                        {
                                            String getTitulo = rs1.getString("titulo_atividade");
                                            String getDescricao = rs1.getString("descricao_atividade");
                                            String getData = rs1.getString("data_atividade");
                                            String getHora = rs1.getString("hora_atividade");

                                            // Exibe as informações das atividades
                                            System.out.println("- Título: " + getTitulo + ", Descrição: " + getDescricao + ", Data: " + getData + ", Hora: " + getHora);
                                        }
                                    }
                                    catch(SQLException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    // Solicita que o usuário insira os detalhes da atividade a ser editada
                                    System.out.printf("\nDigite o nome exato da atividade que deseja editar: ");
                                    String editarTitulo = input.nextLine();

                                    System.out.printf("Digite a data da atividade que deseja editar (formato: dd/MM/yyyy): ");
                                    String editarData = input.nextLine();

                                    System.out.printf("Digite a hora da atividade que deseja editar: ");
                                    String editarHora = input.nextLine();


                                    // Chama o método para editar a atividade, caso encontrada
                                    Atividades atividadeEditar = new Atividades();
                                    atividadeEditar.editarAtividade(editarTitulo, editarData, editarHora);

                                break;

                            case 3:

                                String idUsuarioAtividadesRemover = rs.getString("id");
                                System.out.println("\n");
                                try {
                                    String query1 = "SELECT * FROM atividades WHERE id_usuario = ?";
                                    PreparedStatement statement1 = ConnectionBD.getConexao().prepareStatement(query1);
                                    statement1.setString(1, idUsuarioAtividadesRemover);

                                    ResultSet rs1 = statement1.executeQuery();

                                    // Lista as atividades disponíveis
                                    while (rs1.next())
                                    {
                                        String getTitulo = rs1.getString("titulo_atividade");
                                        String getDescricao = rs1.getString("descricao_atividade");
                                        String getData = rs1.getString("data_atividade");
                                        String getHora = rs1.getString("hora_atividade");

                                        // Exibe as informações das atividades
                                        System.out.println("- Título: " + getTitulo + ", Descrição: " + getDescricao + ", Data: " + getData + ", Hora: " + getHora);
                                    }

                                    // Solicita que o usuário insira os detalhes da atividade a ser excluída
                                    System.out.printf("\nDigite o nome exato da atividade que deseja excluir: ");
                                    String excluirTitulo = input.nextLine();

                                    System.out.printf("Digite a data da atividade que deseja excluir (formato: dd/MM/yyyy): ");
                                    String excluirData = input.nextLine();

                                    System.out.printf("Digite a hora da atividade que deseja excluir (formato: HH:MM:SS): ");
                                    String excluirHora = input.nextLine();

                                    // Chama o método para excluir a atividade
                                    Atividades atividadeRemover = new Atividades();
                                    atividadeRemover.removerAtividade(excluirTitulo, excluirData, excluirHora);
                                }
                                catch (SQLException e)
                                {
                                    e.printStackTrace();
                                }

                                break;

                            case 4:
                                flag = false;
                                System.out.println("Saindo do menu");
                                break;

                            default:
                                System.out.println("Opção inválida");
                                break;
                        }
                    }
                }
                else
                {
                    System.out.println("Senha incorreta!");
                }
            }
            else
            {
                System.out.println("Conta não existente ou credenciais incorretas");
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean checkPassword(String password, String hashArmazenado)
    {
        boolean senhaVerificada = false;

        if(null == hashArmazenado || !hashArmazenado.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("O hash informado é inválido para comparação");

        senhaVerificada = BCrypt.checkpw(password, hashArmazenado);

        return senhaVerificada;
    }
}
