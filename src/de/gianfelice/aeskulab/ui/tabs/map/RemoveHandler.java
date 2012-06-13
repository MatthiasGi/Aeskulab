package de.gianfelice.aeskulab.ui.tabs.map;

import java.util.ArrayList;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Place;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.ui.components.ComPlace;
import de.gianfelice.aeskulab.ui.components.ComPlace.DDGrid;
import de.gianfelice.aeskulab.ui.components.Unit;

import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * A {@link DropHandler} that removes the dropped items from their source.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 */
public class RemoveHandler implements DropHandler {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drop(DragAndDropEvent event) {
		
		// Collect information
		LayoutBoundTransferable trans = (LayoutBoundTransferable)
				event.getTransferable();
		Component com = trans.getComponent();
		Component source = trans.getSourceComponent();
		if (!(source instanceof ComponentContainer)) return;
		if (!(com instanceof Unit || com instanceof ComPlace)) return;
		((ComponentContainer) source).removeComponent(com);
		
		if (source instanceof DDGrid) {
			
			// Out of ComPlace?
			DDGrid grid = (DDGrid) source;
			grid.recalculateLayout();
			Place p = grid.getPlace();
			Object o = ((Unit) com).getEntity();
			if (o instanceof Squad) {
				p.removeSquad((Squad) o);
			} else if (o instanceof Vehicle) {
				p.removeVehicle((Vehicle) o);
			} else if (o instanceof Helper) {
				p.removeHelper((Helper) o);
			}
			
		} else if (com instanceof Unit) {
			
			// A unit?
			Object o = ((Unit) com).getEntity();
			if (o instanceof Squad) {
				((Squad) o).setLeft(null);
				((Squad) o).setTop(null);
			} else if (o instanceof Vehicle) {
				((Vehicle) o).setLeft(null);
				((Vehicle) o).setTop(null);
			} else if (o instanceof Helper) {
				((Helper) o).setLeft(null);
				((Helper) o).setTop(null);
			} 
			
		} else if (com instanceof ComPlace) {
			
			// A ComPlace itself?
			Place p = ((ComPlace) com).getPlace();
			p.setSquads(new ArrayList<Squad>());
			p.setVehicles(new ArrayList<Vehicle>());
			p.setHelpers(new ArrayList<Helper>());
			p.setLeft(null);
			p.setTop(null);

		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

}