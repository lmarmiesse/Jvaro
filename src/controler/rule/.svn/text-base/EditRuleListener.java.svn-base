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

package controler.rule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import controler.MainControler;

import view.GeneralFrame;
import view.RulesPanel;
import view.popup.AddRuleFrame;

public class EditRuleListener implements ActionListener {
	private MainControler controler;
	private RulesPanel panel;

	public EditRuleListener(MainControler controler, RulesPanel panel) {
		this.controler = controler;
		this.panel = panel;
	}

	public void actionPerformed(ActionEvent e) {
		String oldRule = panel.getSelected();
		String newRule = null;
		if (oldRule.contains("THEN")) {
			AddRuleFrame f = new AddRuleFrame(
					GeneralFrame.getInstance(controler), controler, oldRule);
			if (f.okPressed()) {
				newRule = f.getRule();
				controler.editRule(oldRule, newRule);
				controler.notifyObservers();
			}
		} else {
			JFrame frame = new JFrame();
			newRule = JOptionPane.showInputDialog(frame, "rule :", oldRule);
			if (newRule != null && checkRule(newRule) == true) {
				System.out.println("YES");
				controler.editRule(oldRule, newRule);
				controler.notifyObservers();
			}
			if (newRule != null && checkRule(newRule) == false){
				JOptionPane.showMessageDialog(null, "Wrong syntax \n See the documentation for more informations", "", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	// checks the rules (from regEfmtool code )
	public boolean checkRule(String str) {
		// check if two operators follow each other
		if (str.contains("||") || str.contains("|&") || str.contains("|!")
				|| str.contains("&|") || str.contains("&&")
				|| str.contains("&!") || str.contains("!|")
				|| str.contains("|&") || str.contains("!!")) {
			return false;
		}
		  // check the number of opening and closing parenthesis
	      // and if the number of operators is equal to the number
	      // of pairs of parenthesis
		int nbOpen = countOccurrences(str, '(');
		int nbClose = countOccurrences(str, ')');
		int nbAnd = countOccurrences(str, '&');
		int nbOr = countOccurrences(str, '|');
		int nbNot = countOccurrences(str, '!');
		int nbOperator = nbAnd + nbOr + nbNot;
		
		//checks if number of parenthesis
		if (nbOpen!=nbClose){
			return false;
		}
		
		//check if number parenthesis = number operator
		if (nbOpen != nbOperator){
			return false;
		}
		
		// one equal sign
		int nbEqual = countOccurrences(str, '=');
		if (nbEqual!=1){
			return false;
		}
		
		//last char = )
		if (str.charAt(str.length()-1)!=')'){
			return false;
		}
		
		return true;
	}
	
	public static int countOccurrences(String str, char c)
	{
	    int count = 0;
	    for (int i=0; i < str.length(); i++)
	    {
	        if (str.charAt(i) == c)
	        {
	             count++;
	        }
	    }
	    return count;
	}

}
