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

package controler;

import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

//Update the tables when the searchField is modified 
public class MyDocumentListener implements DocumentListener {

	private JTextField searchField;
	private TableRowSorter<TableModel> sorter;
	private int col1;
	private int col2;

	public MyDocumentListener(JTextField field,
			TableRowSorter<TableModel> sorter,int col1,int  col2) {
		this.searchField = field;
		this.sorter = sorter;
		this.col1 = col1;
		this.col2=col2;
	}

	public void changedUpdate(DocumentEvent e) {

	}
	
	public void insertUpdate(DocumentEvent e) {
		String text = searchField.getText();
		if (sorter.getModelRowCount()!=0 && text.length() != 0){
			//case insensitive on column 0 and 2
			sorter.setRowFilter(RowFilter.regexFilter(
					"(?i)" + Pattern.quote(text), col1, col2));
		}
	}


	public void removeUpdate(DocumentEvent e) {
		String text = searchField.getText();
		if (sorter.getModelRowCount()!=0 ){
			sorter.setRowFilter(RowFilter.regexFilter(
					"(?i)" + Pattern.quote(text), col1, col2));
		}
	}
}
