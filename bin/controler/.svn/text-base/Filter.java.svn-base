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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.ElementaryMode;
import model.Metabolite;
import model.Reaction;

//display the result with chosen metabolites and reactions
public class Filter {

	private Set<Reaction> reactions = new HashSet<Reaction>();
	private Set<Metabolite> metabolites = new HashSet<Metabolite>();

	// return true is the mode is accepted by the filter
	public boolean accepts(ElementaryMode mode) {
		Map<Reaction, Double> map = mode.getContent();

		for (Reaction filterReac : reactions) {
			boolean isPresent = false;

			for (Reaction modeReac : map.keySet()) {
				if (modeReac.equals(filterReac)) {
					isPresent = true;
					break;
				}

			}
			if (!isPresent)
				return false;

		}

		for (Metabolite filterMet : metabolites) {
			boolean isPresent = false;
			for (Metabolite modeMet : mode.getListMetabolite()) {
				if (modeMet.equals(filterMet)) {
					isPresent = true;
					break;
				}
			}
			if (!isPresent)
				return false;
		}

		return true;
	}

	public Set<Reaction> getReactions() {
		return reactions;
	}

	public Set<Metabolite> getMetabolites() {
		return metabolites;
	}

	public void addReaction(Reaction r) {
		reactions.add(r);
	}

	public void addMetabolite(Metabolite m) {
		metabolites.add(m);
	}

	public void reset() {
		reactions.clear();
		metabolites.clear();
	}
}
