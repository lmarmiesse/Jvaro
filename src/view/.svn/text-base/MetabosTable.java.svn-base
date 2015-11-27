/*
    This file is part of J-Varo.
    Authors: Elodie Cassan, Christophe Djemiel, Thomas Faux, Aurélie Lelièvre, Lucas Marmiesse

    J-Varo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    J-Varo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with J-Varo.  If not, see <http://www.gnu.org/licenses/>.
 */

package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controler.MainControler;

public class MetabosTable extends JTable {

	private static final long serialVersionUID = 1L;
	private static MetabosTable instance = null;
	private MainControler controler;
	private String col1;
	private String col2;
	private String col3;

	private MetabosTable(MainControler controler, String c1, String c2,
			String c3) {

		super(0, 3);
		this.col1 = c1;
		this.col2 = c2;
		this.col3 = c3;

		MyTableModel model = new MyTableModel();

		this.setModel(model);
		model.addColumn(col1);
		model.addColumn(col2);
		model.addColumn(col3);

		TableColumn column = getColumnModel().getColumn(1);
		column.setPreferredWidth(90);
		column.setMaxWidth(90);

		setAutoCreateRowSorter(true);
		getTableHeader().setReorderingAllowed(false);


		this.controler = controler;

	}

	// Display checkbox in tables
	class MyTableModel extends DefaultTableModel {
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int iRowIndex, int iColumnIndex) {

			if (getColumnName(iColumnIndex) == col2) {
				return true;
			}

			return false;
		}
	}

	public static MetabosTable getInstance(MainControler controler, String c1,
			String c2, String c3) {
		if (instance == null) {
			return instance = new MetabosTable(controler, c1, c2, c3);
		}
		return instance;
	}

	public void update() {
		// Lines are deleted and reloaded
		while (getModel().getRowCount() > 0) {
			((MyTableModel) this.getModel()).removeRow(0);
		}

		// controller gives the states of metabolites
		List<Object[]> listeMetab = controler.getMetabos();

		for (Object[] metab : listeMetab) {
			((MyTableModel) this.getModel()).addRow(metab);
		}

	}

}
