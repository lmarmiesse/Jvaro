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

package view.popup;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import controler.Var;

public class AboutFrame extends PopUpAbs implements HyperlinkListener {

	public AboutFrame(JFrame parent) {
		super(parent);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("About J-VARO");

		JPanel up = new JPanel();
		
		up.add(new JLabel(Var.logo));
		
		up.add(new JLabel(" 1.0"));

		JTabbedPane jtp = new JTabbedPane();


		JEditorPane editorInfos = new JEditorPane();
		editorInfos.setContentType("text/html");
		editorInfos
				.setText("<html><h2 align='center'> User interface for RegEfmTool </h2>    "
						+ "<p align='center'>2013</p>"
						+ "<br/>"
						+ "<p align='center'><a  href='http://christophe.djemiel.emi.u-bordeaux1.fr/'>J-VARO web site</a></p>"
						+ "<br/>"
						+ "<p align='center'><a  href='http://www.youtube.com/user/projectjvaro?feature=watch/'>YouTube channel</a></p>"
						+ "<br/>"
						+ "<p align='center'>Élodie Cassan<br/>Christophe Djemiel<br/>Thomas Faux<br/>Aurélie Lelièvre<br/>Lucas Marmiesse</p> "
						+ "</html>");

		editorInfos.addHyperlinkListener(this);
		editorInfos.setEditable(false);


		JEditorPane editorLicence = new JEditorPane();
		editorLicence.setContentType("text");

		
		String licenceContent="";
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(new File("licence.txt")));
			String input = in.readLine();

			while (input != null) {
				licenceContent+=input+"\n";
				input = in.readLine();
			}
			
			
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		editorLicence.setText(licenceContent);
		editorLicence.setEditable(false);
		editorLicence.setCaretPosition(0);
		
		jtp.add("Infos", new JScrollPane(editorInfos));
		JScrollPane scrollLicence = new JScrollPane(editorLicence);
		jtp.add("Licence", scrollLicence);

		add(up, BorderLayout.PAGE_START);
		add(jtp, BorderLayout.CENTER);
		this.pack();
		this.setSize(550, 400);
		setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				Desktop d = Desktop.getDesktop();
				d.browse(new URI(e.getURL().toString()));
			} catch (IOException e1) {

			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}

		}

	}

}
