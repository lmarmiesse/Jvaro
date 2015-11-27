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

package controler.visu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import model.Reaction;

import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;

import view.VisualizationPanel;

import controler.MainControler;

public class MySvgCanvas extends JSVGCanvas implements Cloneable, Serializable {

	private MainControler controler;
	private File svgFile;
	private Document doc;
	private VisualizationPanel vp;

	// elemement selected when clicked
	// list of associate node edge and label
	private List<Element> selectedElementList;
	private String selectedElementID;
	private List<Element> selectedList;
	// listeners
	private EventListener mouseDownListener = new MouseDownListener();
	private EventListener mouseUpListener = new MouseUpListener();
	private EventListener mouseOverListener = new MouseOverListener();
	private EventListener mouseOutListener = new MouseOutListener();
	private EventListener mouseMoveListener = new MouseMoveListener();

	private JPopupMenu delMenu = new JPopupMenu();
	private JMenuItem delete = new JMenuItem("Delete");

	public MySvgCanvas(File svgFile, final MainControler controler,
			VisualizationPanel vp) {
		this.vp = vp;
		this.controler = controler;
		this.svgFile = svgFile;

		try {
			String parser = XMLResourceDescriptor.getXMLParserClassName();
			SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
			String uri = svgFile.toURI().toString();
			doc = f.createDocument(uri);
		} catch (IOException ex) {
			// ...
		}

		this.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		this.setSVGDocument((SVGDocument) doc);

		initListeners();

		delMenu.add(delete);

	}

	// Constructor for clone
	public MySvgCanvas(File svgFile, MainControler controler,
			SVGDocument document) {

		this.controler = controler;
		this.svgFile = svgFile;

		this.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		this.setSVGDocument(document);

		initListeners();

	}

	public void initListeners() {
		// for nodes reactions and glasspane
		NodeList enzNodes = getSVGDocument().getElementsByTagName("rect");

		for (int i = 0; i < enzNodes.getLength(); i++) {
			Element el = (Element) enzNodes.item(i);
			EventTarget t = (EventTarget) el;
			// we don't want glasspane for this one
			if (!el.getAttribute("id").equals("glasspane")) {

				t.addEventListener(SVGConstants.SVG_EVENT_MOUSEOVER,
						mouseOverListener, false);
				t.addEventListener(SVGConstants.SVG_EVENT_MOUSEOUT,
						mouseOutListener, false);
			}
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEDOWN,
					mouseDownListener, false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEUP, mouseUpListener,
					false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEMOVE,
					mouseMoveListener, false);

		}
		// metabolites
		NodeList metNodes = getSVGDocument().getElementsByTagName("circle");
		for (int i = 0; i < metNodes.getLength(); i++) {
			Element el = (Element) metNodes.item(i);

			EventTarget t = (EventTarget) el;
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEOVER,
					mouseOverListener, false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEOUT,
					mouseOutListener, false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEDOWN,
					mouseDownListener, false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEUP, mouseUpListener,
					false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEMOVE,
					mouseMoveListener, false);
		}
		// texts
		NodeList layoutNodes = getSVGDocument().getElementsByTagName("text");
		for (int i = 0; i < layoutNodes.getLength(); i++) {
			Element el = (Element) layoutNodes.item(i);

			EventTarget t = (EventTarget) el;
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEDOWN,
					mouseDownListener, false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEUP, mouseUpListener,
					false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEMOVE,
					mouseMoveListener, false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEOVER,
					mouseOverListener, false);
			t.addEventListener(SVGConstants.SVG_EVENT_MOUSEOUT,
					mouseOutListener, false);

		}

	}

	public class MouseDownListener implements EventListener {

		public void handleEvent(Event evt) {

			if (((DOMMouseEvent) evt).getButton() == 0) {
				setToolTipText(null);
				setSeletecElements((Element) evt.getTarget());
				selectedElementID = (String) ((Element) evt.getTarget())
						.getAttribute("id");
				if (selectedElementID.equals("glasspane")
						&& delMenu.isVisible() == true) {
					delMenu.setVisible(false);
				}
			}

			// right click
			if (((DOMMouseEvent) evt).getButton() == 2) {

				setSeletecElements((Element) evt.getTarget());
				selectedElementID = (String) ((Element) evt.getTarget())
						.getAttribute("id");

				if (!(selectedElementID.equals("glasspane"))) {
					selectedList = null;
					// keep the selected elements after MouseUpEvent
					selectedList = new ArrayList<Element>(selectedElementList);
					while (delete.getActionListeners().length > 0) {
						delete.removeActionListener(delete.getActionListeners()[0]);
					}
					delete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							delMenu.setVisible(false);
							if (JOptionPane.showConfirmDialog(null, "Delete "
									+ selectedElementID + " ?", "",
									JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								if (selectedList != null) {
									for (Element el : selectedList) {
										doc.getDocumentElement()
												.removeChild(el);
									}
								}
								repaint();
								// vp.svgChange();

							}
						}
					});
					DOMMouseEvent elEvt = (DOMMouseEvent) evt;
					int x = elEvt.getScreenX();
					int y = elEvt.getScreenY();

					delMenu.show(null, x, y);
					delMenu.setVisible(true);
				}
			}

		}
	}

	public class MouseUpListener implements EventListener {

		public void handleEvent(Event evt) {
			selectedElementList = null;
		}
	}

	public class MouseMoveListener implements EventListener {

		public void handleEvent(Event evt) {
			if (selectedElementList != null) {
				DOMMouseEvent elEvt = (DOMMouseEvent) evt;
				int x = elEvt.getClientX();
				int y = elEvt.getClientY();

				// Convert Screen coordinates to Document Coordinates.
				SVGOMPoint pt = new SVGOMPoint(x, y);

				// elem --> screen
				SVGMatrix mat = ((SVGLocatable) evt.getTarget()).getScreenCTM();
				mat = mat.inverse(); // screen -> elem
				SVGOMPoint dragpt = (SVGOMPoint) pt.matrixTransform(mat);

				for (Element el : selectedElementList) {
					if (el.getTagName().equals("text")) {
						el.setAttribute("x",
								new Double(dragpt.getX()).toString());
						el.setAttribute("y",
								new Double(dragpt.getY()).toString());
					} else if (el.getTagName().equals("line")) {
						if (el.getAttribute("source").equals(selectedElementID)) {
							el.setAttribute("x1",
									new Double(dragpt.getX()).toString());
							el.setAttribute("y1",
									new Double(dragpt.getY()).toString());
						} else {
							el.setAttribute("x2",
									new Double(dragpt.getX()).toString());
							el.setAttribute("y2",
									new Double(dragpt.getY()).toString());
						}

					} else if (el.getTagName().equals("rect")) {

						double size = Double.parseDouble(el
								.getAttribute("height"));

						el.setAttribute("x", new Double(dragpt.getX()
								- (size / 2)).toString());
						el.setAttribute("y", new Double(dragpt.getY()
								- (size / 2)).toString());
					}
					// for circles
					else {
						el.setAttribute("cx",
								new Double(dragpt.getX()).toString());
						el.setAttribute("cy",
								new Double(dragpt.getY()).toString());
					}
				}

			}
		}
	}

	// displays informations
	public class MouseOverListener implements EventListener {

		public void handleEvent(Event evt) {
			setInfoText(evt);
		}
	}

	public class MouseOutListener implements EventListener {

		public void handleEvent(Event evt) {
			setToolTipText(null);
		}
	}

	public void setSeletecElements(Element el) {
		List<Element> list = new ArrayList<Element>();
		// addition if clicked element
		list.add(el);
		// collect rows
		NodeList lines = this.getSVGDocument().getElementsByTagName("line");
		for (int i = 0; i < lines.getLength(); i++) {
			Element node = (Element) lines.item(i);
			if ((node.getAttribute("source").equals(el.getAttribute("id")))
					|| (node.getAttribute("target").equals(el
							.getAttribute("id")))) {
				list.add(node);
			}
		}

		// text
		if (el.getTagName().equals("text")) {
			NodeList nodes = this.getSVGDocument().getElementsByTagName("rect");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element node = (Element) nodes.item(i);
				if (node.getAttribute("id").equals(el.getAttribute("id"))) {
					list.add(node);
				}
			}

			nodes = this.getSVGDocument().getElementsByTagName("circle");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element node = (Element) nodes.item(i);
				if (node.getAttribute("id").equals(el.getAttribute("id"))) {
					list.add(node);
				}
			}

		} else {

			NodeList textes = this.getSVGDocument()
					.getElementsByTagName("text");
			for (int i = 0; i < textes.getLength(); i++) {
				Element node = (Element) textes.item(i);
				if (node.getAttribute("id").equals(el.getAttribute("id"))) {
					list.add(node);
				}
			}

		}
		selectedElementList = list;
	}

	public void setInfoText(Event evt) {

		Element el = (Element) evt.getTarget();
		if (el.getTagName().equals("rect")) {
			setToolTipText("<html>Reaction : " + el.getAttribute("id"));

			setToolTipText(getToolTipText()
					+ "<br/>"
					+ controler.getProject().getNetwork()
							.getReaction(el.getAttribute("id"))
							.getStringReaction());

			String rev = "reversible";
			if (!controler.getProject().getNetwork()
					.getReaction(el.getAttribute("id")).isReversible()) {
				rev = "irreversible";
			}

			setToolTipText(getToolTipText() + "<br/>" + rev + "<html>");
		} else if (el.getTagName().equals("circle")) {
			setToolTipText("<html>Metabolite : " + el.getAttribute("id"));

			String internal = "internal";
			
			if (!controler.getProject().getNetwork()
					.getMetabolite(el.getAttribute("id")).isInternal()) {
				
				internal = "external";
			}
			setToolTipText(getToolTipText() + "<br/>" + internal + "<html>");
		} else if (el.getTagName().equals("text")) {

			// if it's a reaction
			Reaction reac = controler.getProject().getNetwork()
					.getReaction(el.getAttribute("id"));
			if (reac != null) {
				setToolTipText("<html>Reaction : " + el.getAttribute("id"));

				setToolTipText(getToolTipText()
						+ "<br/>"
						+ controler.getProject().getNetwork()
								.getReaction(el.getAttribute("id"))
								.getStringReaction());

				String rev = "reversible";
				if (!controler.getProject().getNetwork()
						.getReaction(el.getAttribute("id")).isReversible()) {
					rev = "irreversible";
				}

				setToolTipText(getToolTipText() + "<br/>" + rev + "<html>");

			}
			// if it's a metabolite
			else {
				setToolTipText("<html>Metabolite : " + el.getAttribute("id"));

				String internal = "internal";
				if (!controler.getProject().getNetwork()
						.getMetabolite(el.getAttribute("id")).isInternal()) {
					internal = "external";
				}
				setToolTipText(getToolTipText() + "<br/>" + internal + "<html>");
			}

		}
	}

	public void reDraw(Color reacRevColor, Color reacIrrevColor,
			Color metabIntColor, Color metabExtColor, Color textColor,
			int textSize) {

		String stringReacRevColor = "fill:rgb(" + reacRevColor.getRed() + ","
				+ reacRevColor.getGreen() + "," + reacRevColor.getBlue()
				+ "); ";

		String stringReacIrrevColor = "fill:rgb(" + reacIrrevColor.getRed()
				+ "," + reacIrrevColor.getGreen() + ","
				+ reacIrrevColor.getBlue() + "); ";

		String stringMetabIntColor = "fill:rgb(" + metabIntColor.getRed() + ","
				+ metabIntColor.getGreen() + "," + metabIntColor.getBlue()
				+ "); ";

		String stringMetabExtColor = "fill:rgb(" + metabExtColor.getRed() + ","
				+ metabExtColor.getGreen() + "," + metabExtColor.getBlue()
				+ "); ";

		String stringTextColor = "fill:rgb(" + textColor.getRed() + ","
				+ textColor.getGreen() + "," + textColor.getBlue() + "); ";

		// we get the reactions
		NodeList reacList = getSVGDocument().getElementsByTagName("rect");
		for (int i = 0; i < reacList.getLength(); i++) {
			Element el = (Element) reacList.item(i);
			// we don't want the glasspane
			if (!el.getAttribute("id").equals("glasspane")) {
				// we change the color
				if (controler.getProject().getNetwork()
						.getReaction(el.getAttribute("id")).isReversible()) {
					el.setAttribute(
							"style",
							el.getAttribute("style").substring(
									0,
									el.getAttribute("style").indexOf(
											"fill:rgb("))
									+ stringReacRevColor);
				} else {
					el.setAttribute(
							"style",
							el.getAttribute("style").substring(
									0,
									el.getAttribute("style").indexOf(
											"fill:rgb("))
									+ stringReacIrrevColor);
				}

			}
		}

		// and for the metabolites
		NodeList metabList = getSVGDocument().getElementsByTagName("circle");
		for (int i = 0; i < metabList.getLength(); i++) {
			Element el = (Element) metabList.item(i);
			if (controler.getProject().getNetwork()
					.getMetabolite(el.getAttribute("id")).isInternal()) {
				el.setAttribute(
						"style",
						el.getAttribute("style").substring(0,
								el.getAttribute("style").indexOf("fill:rgb("))
								+ stringMetabIntColor);
			} else {
				el.setAttribute(
						"style",
						el.getAttribute("style").substring(0,
								el.getAttribute("style").indexOf("fill:rgb("))
								+ stringMetabExtColor);
			}
		}

		// for the text
		NodeList textList = getSVGDocument().getElementsByTagName("text");
		for (int i = 0; i < textList.getLength(); i++) {
			Element el = (Element) textList.item(i);
			// change color
			el.setAttribute(
					"style",
					el.getAttribute("style").substring(0,
							el.getAttribute("style").indexOf("fill:rgb("))
							+ stringTextColor);

			// and size
			el.setAttribute(
					"style",
					"font-size:"
							+ textSize
							+ el.getAttribute("style").substring(
									el.getAttribute("style").indexOf(";")));
		}

	}

	public MySvgCanvas getClone() {
		MySvgCanvas copy = new MySvgCanvas(svgFile, controler,
				(SVGDocument) ((getSVGDocument()).cloneNode(true)));

		return copy;
	}

}
