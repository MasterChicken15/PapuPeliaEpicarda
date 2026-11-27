import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 */
public final class ConexionOracle {

    private static final String URL  = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private static final String USER = "ARENA";
    private static final String PASS = "arena123";

    private ConexionOracle() { }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
