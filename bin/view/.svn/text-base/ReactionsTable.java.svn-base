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

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controler.MainControler;

public class ReactionsTable extends JTable {

	private static ReactionsTable instance = null;
	private MainControler controler;
	private String col2;

	private ReactionsTable(MainControler controler, String col1, String col2,
			String col3) {
		super(0, 3);

		this.col2 = col2;

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

	// Diplay checkboxes in the tables
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

	public static ReactionsTable getInstance(MainControler controler,
			String c1, String c2, String c3) {
		if (instance == null) {
			return instance = new ReactionsTable(controler, c1, c2, c3);
		}
		return instance;
	}

	public void update() {

		// rows are deleted and reloaded
		while (getModel().getRowCount() > 0) {
			((MyTableModel) this.getModel()).removeRow(0);
		}

		// Controller give the new states of the reactions
		List<Object[]> listeReac = controler.getReactions();

		for (Object[] reac : listeReac) {
			((MyTableModel) this.getModel()).addRow(reac);
		}

	}

}
