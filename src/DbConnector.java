import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    private static final String username="root";
    private static final String password="Ocean@5612!";
    public Connection connection;

    public void dbConnector(String db_name) throws ClassNotFoundException, SQLException {
        String url="jdbc:mysql://127.0.0.1:3306/?user=root"+db_name;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            connection= DriverManager.getConnection(url,username,password);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}