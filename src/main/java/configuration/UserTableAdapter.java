package configuration;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import domain.*;

public class UserTableAdapter extends AbstractTableModel {

	private String[] columnNames = {"Event","Question","EventDate", "Date"};
	Registered user;
	ArrayList<Apustua> apuestas;
	ArrayList<Double> dineros;
	private static final long serialVersionUID = 1L;

	@Override
	public int getRowCount() {
		return (apuestas==null) ? 0 : apuestas.size(); 
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String res;
		switch(columnIndex) {
			case 0:
				res = apuestas.get(rowIndex).getKuota().getQuestion().getEvent().getDescription();
				break;
			case 1:
				res = apuestas.get(rowIndex).getKuota().getQuestion().getQuestion();
				break;
			case 2:
				res = apuestas.get(rowIndex).getKuota().getQuestion().getEvent().getEventDate().toString();
				break;
			case 3:
				res = dineros.get(rowIndex).toString();
				break;
			default:
				res = "ERR";
		}
		return res;
	}
	
	public UserTableAdapter(Registered u) {
		this.user = u;
		this.apuestas = new ArrayList<>();
		this.dineros = new ArrayList<>();
		for (ApustuAnitza a : u.getApustuAnitzak()) {
			for (Apustua ap : a.getApustuak()) {
				this.apuestas.add(ap);
				this.dineros.add(a.getBalioa());
			}
		}
	}
	


}
