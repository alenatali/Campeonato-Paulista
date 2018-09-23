package controller;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
			switch(lg.getGrupo()) {
			case 'A': 			
				if(rowCount[0] < 5) {
					grupos[0][rowCount[0]] = lg.getTime();
					rowCount[0] += 1;
				}
				break;
			case 'B': 
				if(rowCount[1] < 5) {
					grupos[1][rowCount[1]] = lg.getTime();
					rowCount[1] += 1;	
				}
				break;
			case 'C': 
				if(rowCount[2] < 5) {
					grupos[2][rowCount[2]] = lg.getTime();
					rowCount[2] += 1;	
				}
				break;
			default: 
				if(rowCount[3] < 5) {
					grupos[3][rowCount[3]] = lg.getTime();
					rowCount[3] += 1;	
				}
				break;
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
