package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import configuration.UserTableAdapter;
import domain.Registered;

public class UserBetsGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private Registered user;
	private JTable tabla;

	public UserBetsGUI(Registered usuario) {
		super("Apuestas realizadas por "+usuario.getUsername());
		this.user = usuario;
		this.setBounds(100, 100, 700, 200);
		// Adapter (AbstractTableModel -> new UserTableAdapter(usuario))
		tabla = new JTable(new UserTableAdapter(usuario));
		tabla.setPreferredScrollableViewportSize(new Dimension(500, 70));
		JScrollPane sc = new JScrollPane(tabla);
		getContentPane().add(sc, BorderLayout.CENTER);
	}
	
}
