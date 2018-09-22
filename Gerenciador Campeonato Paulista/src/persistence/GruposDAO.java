package persistence;

import java.util.List;

import model.LinhaGrupo;
import util.GenericDAOException;

public interface GruposDAO {
	
	public List<LinhaGrupo> recuperar () throws GenericDAOException;

}
