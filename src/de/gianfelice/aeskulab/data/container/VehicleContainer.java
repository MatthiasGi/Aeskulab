package de.gianfelice.aeskulab.data.container;

import java.io.File;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.SquadType;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A container that holds all {@link Vehicle} objects and offers different
 * methods to handle them.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Vehicle
 * @see     SquadType
 * @see     SquadContainer
 */
public class VehicleContainer extends HierarchicalContainer {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** This columns should be visible. */
	public static final Object[] NATURAL_COLS = new Object[] {"name", "type",
		"requirements", "image"};

	/** The headers for the visible columns. */
	public static final String[] COL_HEADERS = new String[] {"Name", "Typ",
		"Besetzt", "Bild"};

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Instantiates a new container.
	 */
	public VehicleContainer() {
		super();
		addContainerProperty("id", Long.class, null);
		addContainerProperty("name", String.class, "");
		addContainerProperty("type", SquadType.class, null);
		addContainerProperty("state", Integer.class, 6);
		addContainerProperty("requirements", Boolean.class, false);
		addContainerProperty("image", File.class, null);
		
		List<Vehicle> vs = DBManager.getCommunicator().list(Vehicle.class);
		for (Vehicle v : vs) addVehicle(v);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Adds a new vehicle to the container.
	 * 
	 * @param  v The new vehicle
	 * @return   An item, that represents the vehicle or {@code null}, if it was
	 *           not created
	 */
	public Item addVehicle(Vehicle v) {
		Item item = addItem(v.getId());
		if (item == null) return null;
		item.getItemProperty("id").setValue(v.getId());
		item.getItemProperty("name").setValue(v.getName());
		item.getItemProperty("type").setValue(v.getType());
		item.getItemProperty("state").setValue(v.getState());
		item.getItemProperty("requirements").setValue(v.hasRequirements());
		item.getItemProperty("image").setValue(v.getImage());
		setChildrenAllowed(v.getId(), true);
		List<Helper> helpers = v.getHelpers();
		if (helpers != null) {
			for (Helper helper : helpers) addHelper(helper, v);
		}
		return item;
	}
	
	/**
	 * Returns the vehicle, that is represented by the given item.
	 * 
	 * @param  item The item, that should be checked
	 * @return      The vehicle or {@code null} if there is none
	 */
	public static Vehicle getVehicle(Item item) {
		return DBManager.getCommunicator().find(Vehicle.class,
				(Long) item.getItemProperty("id").getValue());
	}
	
	/**
	 * Adds a helper to the container and assigns him to a vehicle.
	 * 
	 * @param helper  The helper
	 * @param vehicle The vehicle
	 */
	public void addHelper(Helper helper, Vehicle vehicle) {
		if (vehicle == null || helper == null) return;
		Item item = addItem(helper.getId());
		if (item == null) return;
		item.getItemProperty("id").setValue(helper.getId());
		item.getItemProperty("name").setValue(helper.getFirstName() + " " +
				helper.getLastName());
		setChildrenAllowed(helper.getId(), false);
		setParent(helper.getId(), vehicle.getId());
		vehicle.addHelper(helper);
		sort();
	}
	
	/**
	 * Sorts the container.
	 */
	public void sort() {
		sort(new String[] {"name"}, new boolean[] {true});
	}

}