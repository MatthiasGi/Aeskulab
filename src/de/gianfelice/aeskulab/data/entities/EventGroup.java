package de.gianfelice.aeskulab.data.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.system.persistence.PersistentObject;

/**
 * A simple entity to group {@link EventEntity} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     EventEntity
 */
@Entity
@Table(name = "eventGroup")
public class EventGroup extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The title. */
	@Column(nullable = false)
	private String title;

	/** The parent. */
	private EventGroup parent;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * An empty constructor which does simply nothing.
	 * 
	 * @see #EventGroup(String)
	 */
	public EventGroup() {}
	
	/**
	 * Instantiates a new event group and sets the title.
	 *
	 * @param title The title
	 */
	public EventGroup(String title) {
		setTitle(title);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Gets the title.
	 *
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title.
	 *
	 * @param title The new title
	 */
	public void setTitle(String title) {
		this.title = title;
		store();
	}

	/**
	 * Gets the parent.
	 *
	 * @return The parent
	 */
	public EventGroup getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 *
	 * @param parent The new parent
	 */
	public void setParent(EventGroup parent) {
		this.parent = parent;
		store();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof EventGroup)) return false;
		EventGroup group = (EventGroup) obj;		
		return getId().equals(group.getId());
	}
	
	/**
	 * Destroys the object and assigns all child-objects to its own parent.
	 */
	public void destroy() {
		
		// Groups
		String query = "SELECT e FROM EventGroup e WHERE e.parent = :group";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("group", this);
		List<EventGroup> groups = DBManager.getCommunicator().list(query,
				parameters);
		for (EventGroup group : groups) group.setParent(parent);
		
		// Entities
		query = "SELECT e FROM EventEntity e WHERE e.eventGroup = :group";
		parameters = new HashMap<String, Object>();
		parameters.put("group", this);
		List<EventEntity> entities = DBManager.getCommunicator().list(query,
				parameters);
		for (EventEntity entity : entities) entity.setEventGroup(parent);
		
		// Delete
		DBManager.getCommunicator().delete(this);
		
	}

}