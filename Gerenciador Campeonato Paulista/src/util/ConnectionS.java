package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionS {

	private static Connection c;

	private ConnectionS () {
	}
	
	public static Connection getConnection() 
			throws ClassNotFoundException, SQLException {

		String hostName = "localhost";
		String dbName = "aulacall";
		String user = "eclipse";
		String senha = "";
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		c = DriverManager.getConnection(
				String.format(
				"jdbc:jtds:sqlserver://%s:1433;databaseName=%s;user=%s;password=%s;", hostName, dbName, user, senha)
		);
		return c;
	}
}

/* segurança integrada -- necessita de .jar e connfigurações adequadas
c = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=aulacall;integratedSecurtity=true;");
*/

/*
c = DriverManager.getConnection(
"jdbc:jtds:sqlserver://"+hostName+":1433;databaseName="+dbName+";namedPipe=false", user, senha);
*/