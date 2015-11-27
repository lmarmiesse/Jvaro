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

import java.awt.Color;

import javax.swing.ImageIcon;


//contains all the variables re used 
public class Var {
	
	//icons
	public static ImageIcon iconnew = new ImageIcon("src/icons/new.png");
	public static ImageIcon iconfile = new ImageIcon("src/icons/file.png");
	public static ImageIcon iconsave = new ImageIcon("src/icons/save.png");
	public static ImageIcon iconimport = new ImageIcon("src/icons/import.png");
	public static ImageIcon iconrun = new ImageIcon("src/icons/run.png");
	public static ImageIcon iconvisu = new ImageIcon("src/icons/reseau.png");
	public static ImageIcon iconajout = new ImageIcon("src/icons/add.png");
	public static ImageIcon iconmodif = new ImageIcon("src/icons/modif.png");
	public static ImageIcon iconsupp = new ImageIcon("src/icons/supp.png");
	public static ImageIcon iconsearch = new ImageIcon("src/icons/search.png");
	public static ImageIcon iconaddtoproject = new ImageIcon("src/icons/attach.png");
	public static ImageIcon iconremoveproject = new ImageIcon("src/icons/dettach.png");
	public static ImageIcon undo = new ImageIcon("src/icons/undo.png");
	public static ImageIcon redo = new ImageIcon("src/icons/redo.png");
	public static ImageIcon download = new ImageIcon("src/icons/download.png");
	public static ImageIcon upload = new ImageIcon("src/icons/upload.png");
	public static ImageIcon savelittle = new ImageIcon("src/icons/savelittle.png");
	public static ImageIcon logo = new ImageIcon("src/icons/logo_j-varo.png");
	public static ImageIcon suplittle = new ImageIcon("src/icons/supplittle.png");
	public static ImageIcon iconleft = new ImageIcon("src/icons/left.png");
	public static ImageIcon iconright = new ImageIcon("src/icons/right.png");
	public static ImageIcon logolittle = new ImageIcon("src/icons/logo_icon.png");
	public static ImageIcon iconfilter = new ImageIcon("src/icons/filter.png");
	public static ImageIcon iconhistogram = new ImageIcon("src/icons/graph.png");
	public static ImageIcon iconcmd = new ImageIcon("src/icons/cmd.png");

	//colors for visualization
	public static Color reacRevColor = new Color(153,153,204,255);
	public static Color reacIrrevColor = new Color(51,204,255,255);
	public static Color metabIntColor = new Color(255,204,102,255);
	public static Color metabExtColor = new Color(255,102,0,255);
	public static Color textColor = new Color(0,0,0,255);
	
	//extension
	public static String extension = ".jvr";
	
}
