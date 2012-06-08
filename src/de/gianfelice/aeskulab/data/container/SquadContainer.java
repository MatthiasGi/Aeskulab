package de.gianfelice.aeskulab.data.container;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.SquadType;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A container that holds all {@link Squad} objects and offers different methods
 * to handle them.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Squad
 * @see     SquadType
 */
public class SquadContainer extends HierarchicalContainer {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** This columns should be visible. */
	public static final Object[] NATURAL_COLS = new Object[] {"name", "type",
		"requirements"};
	
	/** The headers for the visible columns. */
	public static final String[] COL_HEADERS = new String[] {"Name", "Typ",
		"Besetzt"};

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Instantiates a new container.
	 */
	public SquadContainer() {
		super();
		addContainerProperty("id", Long.class, null);
		addContainerProperty("name", String.class, "");
		addContainerProperty("type", SquadType.class, null);
		addContainerProperty("state", Integer.class, 6);
		addContainerProperty("requirements", Boolean.class, false);
		
		List<Squad> squads = DBManager.getCommunicator().list(Squad.class);
		for (Squad squad : squads) addSquad(squad);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Adds a new squad to the container.
	 * 
	 * @param  squad The new squad
	 * @return       An item, that represents the squad or {@code null}, if it
	 *               was not created.
	 */
	public Item addSquad(Squad squad) {
		Item item = addItem(squad.getId());
		if (item == null) return null;
		item.getItemProperty("id").setValue(squad.getId());
		item.getItemProperty("name").setValue(squad.getName());
		item.getItemProperty("type").setValue(squad.getType());
		item.getItemProperty("state").setValue(squad.getState());
		item.getItemProperty("requirements").setValue(squad.hasRequirements());
		setChildrenAllowed(squad.getId(), true);
		List<Helper> helpers = squad.getHelpers();
		if (helpers != null) {
			for (Helper helper : helpers) addHelper(helper, squad);
		}
		return item;
	}
	
	/**
	 * Returns the squad, that is represented by the given item.
	 * 
	 * @param  item The item, that should be checked
	 * @return      The squad or {@code null}, if there is none
	 */
	public static Squad getSquad(Item item) {
		return DBManager.getCommunicator().find(Squad.class,
				(Long) item.getItemProperty("id").getValue());
	}
	
	/**
	 * Adds a helper to the container and assigns him to a squad.
	 * 
	 * @param helper The helper
	 * @param squad  The squad
	 */
	public void addHelper(Helper helper, Squad squad) {
		if (squad == null || helper == null) return;
		Item item = addItem(helper.getId());
		if (item == null) return;
		item.getItemProperty("id").setValue(helper.getId());
		item.getItemProperty("name").setValue(helper.getFirstName() + " " +
				helper.getLastName());
		setChildrenAllowed(helper.getId(), false);
		setParent(helper.getId(), squad.getId());
		squad.addHelper(helper);
		sort();
	}
	
	/**
	 * Sorts the container.
	 */
	public void sort() {
		sort(new String[] {"name"}, new boolean[] {true});
	}

}