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

package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementaryMode {

	private Map<Reaction,Double> content;
	
	
	
	public ElementaryMode(){
		
		content=new HashMap<Reaction,Double>();
		
	}
	
	public void addReaction(Reaction reac, double stoe){
		
		content.put(reac, stoe);
	}
	
	public Map<Reaction,Double> getContent(){
		
		return content;
		
	}

	public List<Metabolite> getListMetabolite() {
		
		List<Metabolite> metabs = new ArrayList<Metabolite>();
		
		for (Reaction reac : content.keySet()) {
			for (Metabolite m : reac.getListMetabolite()){
				metabs.add(m);
			}
		}
		
		return metabs;
	}

}
