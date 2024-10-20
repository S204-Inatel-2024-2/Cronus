import ConexaoBD.ConnectionBD;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CadastroUsuario
{
    public void cadastrar(String nome, String email, String senha)
    {
        try
        {
            String query = "SELECT * FROM usuario WHERE nickname = ? AND email = ?";
            PreparedStatement statement = ConnectionBD.getConexao().prepareStatement(query);

            statement.setString(1, nome);
            statement.setString(2, email);

            ResultSet rs = statement.executeQuery();

            if(!rs.next())
            {
                String salt = BCrypt.gensalt();
                String hashedPassword = BCrypt.hashpw(senha, salt);

                String userId = UUID.randomUUID().toString();
                //-------------------------------------------------------------------------------------------------

                String query2 = "INSERT INTO usuario(id, nickname, email, password) VALUES (?,?,?,?)";
                PreparedStatement statement2 = ConnectionBD.getConexao().prepareStatement(query2);
                statement2.setString(1, userId);
                statement2.setString(2, nome);
                statement2.setString(3, email);
                statement2.setString(4, hashedPassword);

                int rowsAffected = statement2.executeUpdate();

                if(rowsAffected > 0)
                {
                    System.out.println("Cadastro feito com sucesso!");
                }
                else
                {
                    System.out.println("Ocorreu uma falha ao inserir os dados na tabela!");
                }
            }
            else
            {
                System.out.println("Email já cadastrado!");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}