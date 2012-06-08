package de.gianfelice.aeskulab.ui.tabs.mainmenu;

import java.util.List;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeTable;

import de.gianfelice.aeskulab.data.container.EventTreeContainer;
import de.gianfelice.aeskulab.data.entities.EventEntity;
import de.gianfelice.aeskulab.data.entities.EventGroup;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.ui.tabs.TabMainMenu;

/**
 * Creates a tree with all groups and events in it.
 * 
 * @author     Matthias Gianfelice
 * @version    1.1.0
 * @see        EventTreeDropHandler
 * @see        EventTreeContainer
 * @see        TabMainMenu
 * @deprecated Will be replaced by a {@link TreeTable}
 */
public class EventTree extends Tree implements ItemClickListener {
	
	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A list of all groups. */
	private List<EventGroup> lstGroups;
	
	/** A list of all entities. */
	private List<EventEntity> lstEntities;
	
	/** The main tab. */
	private TabMainMenu tab;
	
	/** The data-container. */
	private EventTreeContainer container;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the tree, prepares it and adds all data to it.
	 *
	 * @param tab The main tab
	 */
	public EventTree(TabMainMenu tab) {
		this.tab = tab;
		readData();

		setItemCaptionPropertyId("title");
		addListener((ItemClickListener) this);
		setDragMode(TreeDragMode.NODE);
		setDropHandler(new EventTreeDropHandler(this));
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Reads the data and thus can be used to refresh the whole container.
	 */
	public void readData() {
		lstGroups = DBManager.getCommunicator().list(EventGroup.class);
		lstEntities = DBManager.getCommunicator().list(EventEntity.class);
		container = new EventTreeContainer(lstGroups, lstEntities);
		setContainerDataSource(container);
		sort();
	}
	
	/**
	 * Adds an entity to the tree.
	 *
	 * @param entity The entity
	 */
	public void addEntity(EventEntity entity) {
		if (lstEntities.contains(entity)) return;
		lstEntities.add(entity);
		container.addEvent(entity);
	}
	
	/**
	 * Adds a group to the tree.
	 *
	 * @param group The group
	 */
	public void addEventGroup(EventGroup group) {
		if (lstGroups.contains(group)) return;
		lstGroups.add(group);
		container.addEventGroup(group);
	}

	/**
	 * {@inheritDoc}
	 */
	public void itemClick(ItemClickEvent event) {
		Object item = event.getItemId();
		if (lstGroups.contains(item)) {
			tab.setEventGroup(lstGroups.get(lstGroups.indexOf(item)));
		} else if (lstEntities.contains(item)) {
			tab.setEventEntity(lstEntities.get(lstEntities.indexOf(item)));
		}
	}
	
	/**
	 * Sorts the container.
	 */
	public void sort() {
		container.sort(new String[]{"dateStart", "title"},
				new boolean[]{true, true});
	}

}