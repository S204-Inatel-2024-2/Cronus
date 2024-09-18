package ConexaoBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD
{
    private static final String url = "jdbc:mysql://localhost:3306/cronus"; // URL do banco de dados
    private static final String user = "root";  // Usuário do banco de dados
    private static final String password = "root";  // Senha do banco de dados

    private static Connection connection = null;

    // Método para obter a conexão
    public static Connection getConexao()
    {
        if (connection == null)
        {
            try
            {
                // Cria a conexão
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Conexão com o banco de dados estabelecida com sucesso.");

            }
            catch (SQLException e)
            {
                e.printStackTrace();
                System.out.println("Falha ao conectar com o banco de dados.");
            }
        }
        return connection;
    }
}
