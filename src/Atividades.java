import ConexaoBD.ConnectionBD;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Atividades
{
    public void adicionarAtividade(String userId, String titulo, String descricao, String data, String hora)
    {
        try
        {
            // Formatar a data para o formato esperado pelo MySQL
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(data);
            String formattedDate = outputFormat.format(date);

            String query = "SELECT * FROM atividades WHERE titulo_atividade = ? AND data_atividade = ? AND hora_atividade = ?";
            PreparedStatement statement = ConnectionBD.getConexao().prepareStatement(query);

            statement.setString(1, titulo);
            statement.setString(2, formattedDate);
            statement.setString(3, hora);

            ResultSet rs = statement.executeQuery();

            if(rs.next())
            {
                System.out.println("Essa atividade já foi inserida. Caso queira editá-la, fique à vontade.");
            }
            else
            {

                String query2 = "INSERT INTO atividades (id_usuario, titulo_atividade, descricao_atividade, data_atividade, hora_atividade) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement2 = ConnectionBD.getConexao().prepareStatement(query2);

                statement2.setString(1, userId);
                statement2.setString(2, titulo);
                statement2.setString(3, descricao);
                statement2.setString(4, formattedDate);
                statement2.setString(5, hora);

                int rowsAffected = statement2.executeUpdate();

                if(rowsAffected > 0)
                {
                    System.out.println("Atividade inserida com sucesso. \n");
                }
                else
                {
                    System.out.println("Erro ao inserir atividade. \n");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void editarAtividade(String titulo, String data, String hora)
    {
        try
        {
            // Formatar a data para o formato esperado pelo MySQL
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(data);
            String formattedDate = outputFormat.format(date);

            String query = "SELECT * FROM atividades WHERE titulo_atividade = ? AND data_atividade = ? AND hora_atividade = ?";
            PreparedStatement statement = ConnectionBD.getConexao().prepareStatement(query);

            statement.setString(1, titulo);
            statement.setString(2, formattedDate);
            statement.setString(3, hora);

            ResultSet rs = statement.executeQuery();

            if(rs.next())
            {
                Scanner input = new Scanner(System.in);
                // Solicita que o usuário insira os detalhes da atividade a ser editada
                System.out.printf("\nDigite o nome de atividade desejado: ");
                String editarTitulo = input.nextLine();

                System.out.printf("Digite a descrição desejada: ");
                String editarDescricao = input.nextLine();

                System.out.printf("Digite a data desejada: ");
                String editarData = input.nextLine();

                System.out.printf("Digite a hora desejada: ");
                String editarHora = input.nextLine();

                // Formatar a data para o formato esperado pelo MySQL
                SimpleDateFormat inputFormatNew = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat outputFormatNew = new SimpleDateFormat("yyyy-MM-dd");
                Date dateNew = inputFormatNew.parse(editarData);
                String formattedDateNew = outputFormatNew.format(dateNew);

                String query2 = "UPDATE atividades SET titulo_atividade = ?, descricao_atividade = ?, data_atividade = ?, hora_atividade = ? WHERE id_atividade = ?";
                PreparedStatement statement2 = ConnectionBD.getConexao().prepareStatement(query2);
                statement2.setString(1, editarTitulo);
                statement2.setString(2, editarDescricao);
                statement2.setString(3, formattedDateNew);
                statement2.setString(4, editarHora);
                statement2.setInt(5, rs.getInt("id_atividade"));

                int rowsAffected = statement2.executeUpdate();

                if(rowsAffected > 0)
                {
                    System.out.println("Atividade atualizada com sucesso.\n");
                }
                else
                {
                    System.out.println("Erro ao atualizar atividade. \n");
                }
            }
            else
            {
                System.out.println("Atividade não encontrada! \n");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void removerAtividade(String nomeAtividade, String data, String hora)
    {
        try
        {
            // Formatar a data para o formato esperado pelo MySQL
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(data);
            String formattedDate = outputFormat.format(date);

            String query = "DELETE FROM atividades WHERE titulo_atividade = ? AND data_atividade = ? AND hora_atividade = ?";
            PreparedStatement statement = ConnectionBD.getConexao().prepareStatement(query);
            statement.setString(1, nomeAtividade);
            statement.setString(2, formattedDate);
            statement.setString(3, hora);

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("\nAtividade removida com sucesso. \n");
            }
            else
            {
                System.out.println("\nErro ao remover atividade. \n");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
