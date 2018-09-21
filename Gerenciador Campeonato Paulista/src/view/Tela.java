package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import java.awt.FlowLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import controller.TelaCtrl;

import javax.swing.JLabel;

public class Tela extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTable tGA;
	private JTable tGB;
	private JTable tGC;
	private JTable tGD;
	private TelaCtrl controller;

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

	public Tela() {
		controller = new TelaCtrl();
		
		setResizable(false);
		setTitle("Campeonato Paulista");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
		//setBounds(100, 100, 485, 328);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 594, 21);
		contentPane.add(menuBar);
		
		JMenu mnOpes = new JMenu("Op\u00E7\u00F5es");
		menuBar.add(mnOpes);
		
		JMenuItem mntmGerarGrupos = new JMenuItem("Gerar grupos");
		mntmGerarGrupos.setName("group generator");
		mntmGerarGrupos.addActionListener(this);
		mnOpes.add(mntmGerarGrupos);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 21, 594, 350);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 574, 328);
		panel.add(panel_1);
		
		tGA = new JTable();
		tGB = new JTable();
		tGC = new JTable();
		tGD = new JTable();
		limparTabelas();
		
		tGA.setBounds(68, 147, 75, 80);
		tGA.setRowSelectionAllowed(false);
		tGA.getColumnModel().getColumn(0).setResizable(false);
		panel_1.setLayout(null);
		panel_1.add(tGA);
		
		tGB.setRowSelectionAllowed(false);
		tGB.setBounds(185, 147, 75, 80);
		tGB.getColumnModel().getColumn(0).setResizable(false);
		panel_1.add(tGB);
		
		tGC.setBounds(301, 147, 75, 80);
		tGC.setRowSelectionAllowed(false);
		tGC.getColumnModel().getColumn(0).setResizable(false);
		panel_1.add(tGC);
		
		tGD.setRowSelectionAllowed(false);
		tGD.setBounds(416, 147, 75, 80);
		tGD.getColumnModel().getColumn(0).setResizable(false);
		panel_1.add(tGD);
		
		JLabel lblGrupoA = new JLabel("Grupo A");
		lblGrupoA.setBounds(68, 106, 46, 14);
		panel_1.add(lblGrupoA);
		
		JLabel lblGrupoB = new JLabel("Grupo B");
		lblGrupoB.setBounds(185, 106, 46, 14);
		panel_1.add(lblGrupoB);
		
		JLabel lblGrupoC = new JLabel("Grupo C");
		lblGrupoC.setBounds(301, 106, 46, 14);
		panel_1.add(lblGrupoC);
		
		JLabel lblGrupoD = new JLabel("Grupo D");
		lblGrupoD.setBounds(416, 106, 46, 14);
		panel_1.add(lblGrupoD);
	}
	
	private void limparTabelas () {
		TableModel tm = new DefaultTableModel(new String[] {"Time"}, 5);

		tGA.setModel(tm);
		tGB.setModel(tm);
		tGC.setModel(tm);
		tGD.setModel(tm);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent c = (JComponent) e.getSource();
		if(c.getName().equals("group generator")) {
			controller.prencherTabGrupos(new JTable[] {tGA, tGB, tGC, tGD});
		}
	}
}
