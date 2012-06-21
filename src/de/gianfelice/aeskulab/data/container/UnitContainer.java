package de.gianfelice.aeskulab.data.container;

import java.util.Arrays;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.data.entities.Work;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * This container holds all units that can be assigned to {@link Work} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Squad
 * @see     Vehicle
 * @see     Work
 */
public class UnitContainer extends IndexedContainer {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** The original order of the columns. */
	public static final Object[] NATURAL_COLS = new Object[] {"name", "sType",
		"helpers", "state"};

	/** The headers of the columns. */
	public static final String[] COL_HEADERS = new String[] {"Name", "Typ",
		"Anz.", "Status"};

	/** The type of the unit is squad. */
	public static final Integer TYPE_SQUAD = 1;
	
	/** The type of the unit is vehicle. */
	public static final Integer TYPE_VEHICLE = 2;
	
	/** A filter for squads. */
	private Filter filterSquad;
	
	/** A filter for vehicles. */
	private Filter filterVehicle;
	
	/** A filter for unavailable units. */
	private Filter filterUnavailable;
	
	/** Whether unavailable units are visible or not. */
	private boolean areUnavailableVisble;
	
	/** A list with all states, that display, that a unit is available. */
	public static final List<Integer> AVAILABLE_STATES =
			Arrays.asList(1, 2, 5, 9);

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Initialises the container and loads all units to it.
	 */
	public UnitContainer() {
		addContainerProperty("name", String.class, null);
		addContainerProperty("sType", String.class, null);
		addContainerProperty("helpers", Integer.class, null);
		addContainerProperty("state", Integer.class, 6);
		addContainerProperty("type", Integer.class, 0);
		
		List<Squad> squads = DBManager.getCommunicator().list(Squad.class);
		for (Squad squad : squads) {
			Item item = addItem(squad.getId());
			item.getItemProperty("name").setValue(squad.getName());
			item.getItemProperty("sType").setValue(squad.getType().getName());
			item.getItemProperty("helpers").setValue(squad.getHelpers().size());
			item.getItemProperty("state").setValue(squad.getState());
			item.getItemProperty("type").setValue(TYPE_SQUAD);
		}
		
		List<Vehicle> vehicles = DBManager.getCommunicator().list(
				Vehicle.class);
		for (Vehicle vehicle : vehicles) {
			Item item = addItem(vehicle.getId());
			item.getItemProperty("name").setValue(vehicle.getName());
			item.getItemProperty("sType").setValue(vehicle.getType().getName());
			item.getItemProperty("helpers").setValue(
					vehicle.getHelpers().size());
			item.getItemProperty("state").setValue(vehicle.getState());
			item.getItemProperty("type").setValue(TYPE_VEHICLE);
		}
		
		filterSquad = new Filter() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean passesFilter(Object itemId, Item item)
					throws UnsupportedOperationException {
				return !TYPE_SQUAD.equals(
						item.getItemProperty("type").getValue());
			}
			
			@Override
			public boolean appliesToProperty(Object propertyId) {
				return "type".equals(propertyId);
			}
		};
		filterVehicle = new Filter() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean passesFilter(Object itemId, Item item)
				throws UnsupportedOperationException {
				return !TYPE_VEHICLE.equals(
						item.getItemProperty("type").getValue());
			}
			
			@Override
			public boolean appliesToProperty(Object propertyId) {
				return "type".equals(propertyId);
			}
		};
		filterUnavailable = new Filter() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean passesFilter(Object itemId, Item item)
					throws UnsupportedOperationException {
				Object state = item.getItemProperty("state").getValue();
				return AVAILABLE_STATES.contains(state);
			}

			@Override
			public boolean appliesToProperty(Object propertyId) {
				return "state".equals(propertyId);
			}
		};

		sort();
		setUnavailableVisible(false);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Sorts the container by names.
	 */
	public void sort() {
		sort(new Object[] {"name"}, new boolean[] {true});
	}
	
	/**
	 * Sets the visibility of the squads.
	 * 
	 * @param visible Whether the squads should be visible.
	 */
	public void setSquadsVisible(boolean visible) {
		if (visible) {
			removeContainerFilter(filterSquad);
		} else {
			addContainerFilter(filterSquad);
		}
	}
	
	/**
	 * Sets the visibility of the vehicles.
	 * 
	 * @param visible Whether the vehicles should be visible.
	 */
	public void setVehicleVisible(boolean visible) {
		if (visible) {
			removeContainerFilter(filterVehicle);
		} else {
			addContainerFilter(filterVehicle);
		}
	}
	
	/**
	 * Sets the visibility of the unavailable units.
	 * 
	 * @param visible Whether the unavailable units should be visible.
	 */
	public void setUnavailableVisible(boolean visible) {
		if (visible) {
			removeContainerFilter(filterUnavailable);
		} else {
			addContainerFilter(filterUnavailable);
		}
		areUnavailableVisble = visible;
	}
	
	/**
	 * Checks, whether the unavailable units are visible or not.
	 * 
	 * @return Whether the unavailable units are visible
	 */
	public boolean areUnavailableVisible() {
		return areUnavailableVisble;
	}
	
	/**
	 * Returns the unit corresponding to the given id.
	 * 
	 * @param  itemId The id
	 * @return        The corresponding unit
	 */
	public static Object getUnit(Object itemId) {
		if (!(itemId instanceof Long)) return null;
		Long id = (Long) itemId;
		Squad squad = DBManager.getCommunicator().find(Squad.class, id);
		if (squad != null) return squad;
		return DBManager.getCommunicator().find(Vehicle.class, id);
	}
	
}