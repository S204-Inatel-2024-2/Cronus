import ConexaoBD.ConnectionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        System.out.printf("usuário: ");
        String nome = input.nextLine();

        System.out.printf("email: ");
        String email = input.nextLine();

        System.out.printf("senha: ");
        String senha = input.nextLine();

        try
        {
            String query = "SELECT * FROM usuario WHERE email = ? AND password = ?";
            PreparedStatement statement = ConnectionBD.getConexao().prepareStatement(query);

            statement.setString(1, email);
            statement.setString(2, senha);

            ResultSet rs = statement.executeQuery();

            if(rs.next())
            {
                System.out.println("Conta já cadastrada");
            }
            else
            {
                String query2 = "INSERT INTO usuario(nickname, email, password) VALUES (?,?,?)";
                PreparedStatement statement2 = ConnectionBD.getConexao().prepareStatement(query2);

                statement2.setString(1, nome);
                statement2.setString(2, email);
                statement2.setString(3, senha);

                int rowsAffected = statement2.executeUpdate();

                if(rowsAffected > 0)
                {
                    System.out.println("Dados inseridos com sucesso!");
                }
                else
                {
                    System.out.println("Ocorreu uma falha ao inserir os dados na tabela!");
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}