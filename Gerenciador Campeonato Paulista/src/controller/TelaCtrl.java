package controller;

import java.util.ArrayList;
import javax.swing.JTable;
import model.LinhaGrupo;
import persistence.GruposDAO;
import persistence.SQLSERVER.Grupos;
import util.GenericDAOException;

public class TelaCtrl {
	
	private GruposDAO persistence;
	
	public TelaCtrl () 
			throws GenericDAOException {
		persistence = new Grupos();
	}

	public void prencherTabGrupos(JTable[] t)
			throws GenericDAOException {
		ArrayList<LinhaGrupo> lista = (ArrayList<LinhaGrupo>) persistence.recuperar();
		//
		// agrupando
		String[][] grupos = new String[4][5];
		int[] rowCount = {0,0,0,0};
		for(LinhaGrupo lg : lista) {
			char c = lg.getGrupo();
			// seletor
			int num = 0; // A case
			if(c == 'B') {
				num = 1;
			} else if (c == 'C') {
				num = 2;
			} else if (c == 'D') {
				num = 3;
			}
			// distribui para os grupos
			if(rowCount[num] < 5) {
				grupos[num][rowCount[num]] = lg.getTime();
				rowCount[num] += 1;
			}
		}
		for(int x = 0; x < 4; x++) {
			t[x].getColumnModel().getColumn(0).setHeaderValue("Time");
			for(int y = 0; y < 5; y++) {
				t[x].getModel().setValueAt(grupos[x][y], y, 0);
			}
		}
	}
}
