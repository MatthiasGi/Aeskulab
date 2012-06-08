package de.gianfelice.aeskulab.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.system.persistence.PersistentObject;

/**
 * An entity to store the events, that will be managed by this application.
 * 
 * @author  Matthias Gianfelice
 * @version 0.3.0
 * @see     EventGroup
 */
@Entity
@Table(name = "evententity")
public class EventEntity extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The title of the event. */
	@Column(nullable = false)
	private String title;
	
	/** The start date of the event. */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dateStart;
	
	/** The end date of the event. */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dateEnd;
	
	/** The place, where the event takes place. */
	private String place;
	
	/** The contact, that organises the event. */
	private String contact;
	
	/** The event group. */
	private EventGroup eventGroup;
	
	// ----------------------------- Constructor(s) ----------------------------	
	/**
	 * An empty constructor which does nothing.
	 * 
	 * @see #EventEntity(String)
	 */
	public EventEntity() {}
	
	/**
	 * Instantiates a new event entity and sets the title. This constructor is
	 * preferred.
	 *
	 * @param title The title for this event
	 */
	public EventEntity(String title) {
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
	 * Gets the start date.
	 *
	 * @return The start date
	 */
	public Date getDateStart() {
		return dateStart;
	}

	/**
	 * Sets the start date.
	 *
	 * @param dateStart The new start date
	 */
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
		store();
	}

	/**
	 * Gets the end date.
	 *
	 * @return The end date
	 */
	public Date getDateEnd() {
		return dateEnd;
	}

	/**
	 * Sets the end date.
	 *
	 * @param dateEnd The new end date
	 */
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
		store();
	}

	/**
	 * Gets the event group.
	 *
	 * @return The event group
	 */
	public EventGroup getEventGroup() {
		return eventGroup;
	}

	/**
	 * Sets the event group.
	 *
	 * @param eventGroup The new event group
	 */
	public void setEventGroup(EventGroup eventGroup) {
		this.eventGroup = eventGroup;
		store();
	}

	/**
	 * Gets the place.
	 *
	 * @return The place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * Sets the place.
	 *
	 * @param place The new place
	 */
	public void setPlace(String place) {
		this.place = place;
		store();
	}

	/**
	 * Gets the contact.
	 *
	 * @return The contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * Sets the contact.
	 *
	 * @param contact The new contact
	 */
	public void setContact(String contact) {
		this.contact = contact;
		store();
	}
	
	/**
	 * Destroys the object and removes all data assigned to it.
	 * TODO Remove data fragments
	 */
	public void destroy() {
		DBManager.getCommunicator().delete(this);
	}

}