package view;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import util.ConnectionST;

public class Telasssaaa {
	
	private JFrame f;
	private JPanel p;
	private JMenuBar menu;
	private JTable TGA, TGB, TGC, TGD;
	
	public Telasssaaa () {
		lookAndFeelSetup();
		jframeSetup();
		jMenuBarSetup();
		tablesSetup();
		
		f.add(p);
		f.setVisible(true);
	}
	
	private void lookAndFeelSetup () {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	private void jframeSetup () {
		p = new JPanel();
		p.setLayout(null);
		f = new JFrame("Campeonato Paulista");
		f.setResizable(false);
		f.setSize(600, 400);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void jMenuBarSetup () {
		JMenuItem gerarI = new JMenuItem ("Gerar");
		
		JMenu opc = new JMenu("Opções");
		opc.add(gerarI);
		
		menu = new JMenuBar();
		menu.add(opc);

		p.add(menu);
	}
	
	private void tablesSetup () {
		
		String[] columns = {"Time"};
		Object[][] rows = {{"ss"}};
		
		TGA = new JTable();
		TableModel m = new DefaultTableModel(rows, columns);
		Dimension d = TGA.getPreferredSize();
		TGA.setBounds(0, 0, d.width , d.height);
		
		p.add(TGA);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Telasssaaa frame = new Telasssaaa();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
