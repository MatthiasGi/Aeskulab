package de.gianfelice.aeskulab.data.container;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.data.entities.Work;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * Contains all {@link Work} objects in hierarchical order with the assigned
 * units.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Work
 * @see     WorkItemContainer
 */
public class WorkContainer extends HierarchicalContainer {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** The original order of the columns. */
	public static final Object[] NATURAL_COLS = new Object[] {"caption",
		"state"};

	/** The headers of the columns. */
	public static final String[] COL_HEADERS = new String[] {"Betreff",
		"Status"};
	
	/** Whether the ready works are also visible. */
	private boolean isReadyVisible;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Initialises the container and loads all {@link Work} objects to it.
	 */
	public WorkContainer() {		
		addContainerProperty("caption", String.class, null);
		addContainerProperty("state", Integer.class, 6);
		addContainerProperty("ready", Boolean.class, false);
		List<Work> works = DBManager.getCommunicator().list(Work.class);
		for (Work work : works) addWork(work);
		setDoneWorkVisible(false);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Adds a {@link Work} object to the container.
	 * 
	 * @param work The new object
	 */
	public void addWork(Work work) {
		Item item = addItem(work.getId());
		if (item == null) return;
		item.getItemProperty("caption").setValue(work.getCaption());
		item.getItemProperty("state").setValue(null);
		item.getItemProperty("ready").setValue(work.isReady());
		List<Squad> squads = work.getSquads();
		for (Squad squad : squads) addUnit(squad, work);
		List<Vehicle> vehicles = work.getVehicles();
		for (Vehicle vehicle : vehicles) addUnit(vehicle, work);
	}
	
	/**
	 * Adds a unit to the container and assigns it to a {@link Work} object.
	 * 
	 * @param  unit The unit
	 * @param  work The {@link Work} object
	 * @return      Whether the unit was added
	 */
	public boolean addUnit(Object unit, Work work) {
		if (unit == null || work == null) return false;
		Long id;
		String name;
		int state;
		
		if (unit instanceof Squad) {
			Squad squad = (Squad) unit;
			id = squad.getId();
			name = squad.getName();
			state = squad.getState();
		} else if (unit instanceof Vehicle) {
			Vehicle vehicle = (Vehicle) unit;
			id = vehicle.getId();
			name = vehicle.getName();
			state = vehicle.getState();
		} else {
			return false;
		}
		
		if (!containsId(work.getId())) return false;		
		Item item = addItem(id);
		if (item == null) return false;
		item.getItemProperty("caption").setValue(name);
		item.getItemProperty("state").setValue(state);
		item.getItemProperty("ready").setValue(work.isReady());
		setChildrenAllowed(id, false);
		setParent(id, work.getId());
		return true;
	}
	
	/**
	 * Returns a {@link Work} object corresponding to the given id.
	 * 
	 * @param  itemId The id
	 * @return        The corresponding object
	 */
	public static Work getWork(Object itemId) {
		if (!(itemId instanceof Long)) return null;
		return DBManager.getCommunicator().find(Work.class, (Long) itemId);
	}
	
	/**
	 * Sets the visibility of the work, that is already done.
	 * 
	 * @param visible The visibility
	 */
	public void setDoneWorkVisible(boolean visible) {
		removeAllContainerFilters();
		isReadyVisible = visible;
		if (!visible) {
			addContainerFilter(new Filter() {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean passesFilter(Object itemId, Item item)
						throws UnsupportedOperationException {
					Object val = item.getItemProperty("ready").getValue();
					return !((Boolean) val);
				}
				
				@Override
				public boolean appliesToProperty(Object propertyId) {
					return "ready".equals(propertyId);
				}
			});
		}
	}
	
	/**
	 * Checks, whether the done work is visible.
	 * 
	 * @return Whether the ready work is visible
	 */
	public boolean isReadyVisible() {
		return isReadyVisible;
	}

}