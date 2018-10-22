package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import controller.TelaCtrl;
import model.GrupoTableModel;
import util.ErrorLogST;
import util.GenericDAOException;
import javax.swing.JScrollPane;

public class Tela extends JFrame implements ActionListener {

	//
	//main pane
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	
	//
	// tab grupos
	private JPanel pGrupos;
	private JLabel lblGrupoA, lblGrupoB, lblGrupoC, lblGrupoD;
	private JTable[] tg = new JTable[4];
	
	//
	// tab oitavas
	private JPanel pOitavas;
	
	private TelaCtrl controller;
	private JTable table;
	private JScrollPane scrollPane;

	public Tela() {
		try {
			controller = new TelaCtrl();
		} catch (GenericDAOException e) {
			errorAlert(e);
		}
		
		frameSetup();
		tabGruposSetup ();
		tabOitavasSetup();
	}
	
	private void frameSetup () {
		lookAndFeelSetup ();
		setResizable(false);
		setTitle("Campeonato Paulista");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		menuSetup();
		tabsSetup();
	}
	
	private void menuSetup () {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 594, 21);
		contentPane.add(menuBar);
		
		JMenu mnOpes = new JMenu("Op\u00E7\u00F5es");
		menuBar.add(mnOpes);
		
		JMenuItem mntmGerarGrupos = new JMenuItem("Gerar grupos");
		mntmGerarGrupos.setName("group generator");
		mntmGerarGrupos.addActionListener(this);
		mnOpes.add(mntmGerarGrupos);
	}
	
	private void tabsSetup () {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 21, 598, 350);
		contentPane.add(tabbedPane);
	}
	
	private void tabGruposSetup () {
		pGrupos = new JPanel();
		pGrupos.setLayout(null);
		
		lblGrupoA = new JLabel("Grupo A");
		lblGrupoA.setBounds(68, 80, 46, 14);
		pGrupos.add(lblGrupoA);
		
		lblGrupoB = new JLabel("Grupo B");
		lblGrupoB.setBounds(185, 80, 46, 14);
		pGrupos.add(lblGrupoB);
		
		lblGrupoC = new JLabel("Grupo C");
		lblGrupoC.setBounds(301, 80, 46, 14);
		pGrupos.add(lblGrupoC);
		
		lblGrupoD = new JLabel("Grupo D");
		lblGrupoD.setBounds(416, 80, 46, 14);
		pGrupos.add(lblGrupoD);
		
		limparTabelas();
	
		tabbedPane.addTab("Grupos", null, pGrupos, null);
	}
	
	private void tabOitavasSetup () {
		pOitavas = new JPanel();
		tabbedPane.addTab("Oitavas", null, pOitavas, null);
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

	private void limparTabelas () {
		Rectangle[] bounds = 
				{new Rectangle(68, 105, 75, 107),
				 new Rectangle(185, 105, 75, 107),
				 new Rectangle(301, 105, 75, 107),
				 new Rectangle(416, 105, 75, 107)
				};
		for (int x = 0;  x < tg.length ; x++) {
			tg[x] = new JTable(new GrupoTableModel());
			
			tg[x].setBounds(bounds[x]);
			tg[x].setRowSelectionAllowed(false);
			tg[x].getColumnModel().getColumn(0).setResizable(false);
			
			JScrollPane sp = new JScrollPane();
			sp.setBounds(bounds[x]);
			sp.setViewportView(tg[x]);
			
			pGrupos.add(sp);
		}
		pGrupos.repaint();
	}

	private void errorAlert (Exception e) {
		String mensage = "Ops, ocorreu um erro :(( \nPara mais detalhes consulte o log \ndeste software pasta temp.";
		JOptionPane.showMessageDialog(this, mensage, "Erro", JOptionPane.ERROR_MESSAGE);
		try {
			ErrorLogST.getErrorLog().addError(e);
		} catch (IOException e1) {
			String ioError = "Erro do tipo entrada e saida";
			JOptionPane.showMessageDialog(this, ioError, "Erro", JOptionPane.ERROR_MESSAGE);
		}
		System.exit(1);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent c = (JComponent) e.getSource();
		if(c.getName().equals("group generator")) {
			try {
				controller.prencherTabGrupos(tg);
				pGrupos.repaint();
			} catch (GenericDAOException e1) {
				errorAlert(e1);
			}
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela frame = new Tela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
