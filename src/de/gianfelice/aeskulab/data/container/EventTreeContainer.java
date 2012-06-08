package de.gianfelice.aeskulab.data.container;

import java.util.Date;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.HierarchicalContainer;

import de.gianfelice.aeskulab.data.entities.EventEntity;
import de.gianfelice.aeskulab.data.entities.EventGroup;
import de.gianfelice.aeskulab.system.persistence.PersistentObject;
import de.gianfelice.aeskulab.ui.tabs.mainmenu.EventTree;

/**
 * Creates a basic container for the {@link EventTree}, which holds eventgroups
 * and their events.
 * 
 * @author     Matthias Gianfelice
 * @version    1.1.0
 * @see        EventTree
 * @see        EventGroup
 * @see        EventEntity
 * @deprecated Will be replaced in the next version
 */
public class EventTreeContainer extends HierarchicalContainer {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** List of all groups. */
	private List<EventGroup> groups;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Instantiates a new container.
	 *
	 * @param groups   The groups, that should be stored inside this container
	 * @param entities The actual events, that should be stored
	 */
	public EventTreeContainer(List<EventGroup> groups,
			List<EventEntity> entities) {
		addContainerProperty("title", String.class, null);
		addContainerProperty("dateStart", Date.class, null);
		this.groups = groups;
		for (EventGroup group : groups) addEventGroup(group);
		for (EventEntity entity : entities) addEvent(entity);
		setItemSorter(new DefaultItemSorter());
	}

	// ------------------------------- Method(s) -------------------------------	
	/**
	 * Adds an {@link EventGroup} object.
	 *
	 * @param group A group to add
	 */
	public void addEventGroup(EventGroup group) {
		if (!groups.contains(group)) groups.add(group);
		Item item = addItem(group);
		if (item == null) return;
		item.getItemProperty("title").setValue(group.getTitle());
		if (group.getParent() != null) addToParent(group, group.getParent());
	}
	
	/**
	 * Adds an {@link EventEntity} object;
	 *
	 * @param event An entity to add
	 */
	public void addEvent(EventEntity event) {
		Item item = addItem(event);
		if (item == null) return;
		item.getItemProperty("title").setValue(event.getTitle());
		item.getItemProperty("dateStart").setValue(event.getDateStart());
		if (event.getEventGroup() != null) 
			addToParent(event, event.getEventGroup());
		setChildrenAllowed(event, false);
	}
	
	/**
	 * This method builds a tree of parents recursively.
	 *
	 * @param obj    The actual object, that should be added
	 * @param parent The parent-item, that, or whose parents, will be created,
	 *               if they don't exist
	 */
	private void addToParent(PersistentObject obj, EventGroup parent) {
		int index = groups.indexOf(parent);
		EventGroup group = groups.get(index);
		if (!containsId(group)) addEventGroup(group);
		setParent(obj, group);
	}
	
}