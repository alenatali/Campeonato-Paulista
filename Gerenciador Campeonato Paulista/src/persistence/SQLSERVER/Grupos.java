package persistence.SQLSERVER;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.LinhaGrupo;
import persistence.GruposDAO;
import util.ConnectionST;
import util.GenericDAOException;

public class Grupos implements GruposDAO {
	
	private Connection c;
	
	public Grupos () throws GenericDAOException {
		c = ConnectionST.getConnection();
	}

	@Override
	public List<LinhaGrupo> recuperar() 
			throws GenericDAOException {
		List<LinhaGrupo> lista = null;
		
		String sql = "SELECT * FROM grupos";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ps.close();
			lista = new ArrayList<>();
			while(rs.next()) {
				LinhaGrupo n = new LinhaGrupo();
				n.setTime(rs.getString("time"));
				n.setOrdem(rs.getInt("ordem"));
				n.setGrupo((char) rs.getCharacterStream("grupo").read());
				lista.add(n);
			}
			rs.close();
		} catch (SQLException | IOException e) {
			throw new GenericDAOException(e);
		}
		
		return lista;
	}

}
