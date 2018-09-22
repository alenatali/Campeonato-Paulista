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
		// Instanciação do vetor de List e atribuindo o ti
		ArrayList[] grupos = new ArrayList[4];
		for(int x = 0; x < 4; x++) {
			grupos[x] = new ArrayList<String>();
		}
		//
		// Separação da variavel lista por grupos
		for(LinhaGrupo x : lista) {
			switch(x.getGrupo()) {
				case 'A': grupos[0].add(x.getTime()); break;
				case 'B': grupos[1].add(x.getTime()); break;
				case 'C': grupos[2].add(x.getTime()); break;
				default : grupos[3].add(x.getTime()); break;
			}
		}
		//
		//Atribuição dos grupos as tabelas
		for(int x = 0; x < 4; x++) {
			Object[] header = new Object[] {new String("Time")};
			Object[][] data = new Object[][] {
				{ grupos[x].toArray() }
				};
			// THE PROBLEM IS HERE
			TableModel tm = new DefaultTableModel(data, header);
			System.out.println(tm.getValueAt(0, 0).toString());
			t[x].setModel(tm);
		}
	}
	
	
	

}
