package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionST {

	private static Connection c;

	private ConnectionST () {
	}
	
	public static Connection getConnection() 
			throws GenericDAOException {
		if(c == null) {
			instantiate ();
		}
		return c;
	}
	
	private static void instantiate () 
			throws GenericDAOException {
		String hostName = "localhost";
		String dbName = "av1";
		String user = "eclipse";
		String senha = "";
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			c = DriverManager.getConnection(
					String.format(
					"jdbc:jtds:sqlserver://%s:1433;databaseName=%s;user=%s;password=%s;", hostName, dbName, user, senha)
			);
		} catch (ClassNotFoundException | SQLException e) {
			throw new GenericDAOException(e);
		}
	}
}

/* segurança integrada -- necessita de .jar e connfigurações adequadas
c = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=aulacall;integratedSecurtity=true;");
*/

/*
c = DriverManager.getConnection(
"jdbc:jtds:sqlserver://"+hostName+":1433;databaseName="+dbName+";namedPipe=false", user, senha);
*/