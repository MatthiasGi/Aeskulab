package de.gianfelice.aeskulab.data.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.gianfelice.aeskulab.system.persistence.PersistentObject;

/**
 * Contains information about events, that happened in the past.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 */
@Entity
@Table(name = "log")
public class Log extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** The time of the event. */
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date;
	
	/** The description. */
	@Column(nullable = false)
	private String text;

	/** An optional parent. */
	private Log parent;

	// ----------------------------- Constructor(s) ----------------------------	
	/**
	 * An empty constructor which does nothing.
	 * 
	 * @see #Log(String)
	 * @see #Log(String, Log)
	 */
	public Log() {}
	
	/**
	 * Creates the object and sets a description.
	 * 
	 * @param text The description
	 */
	public Log(String text) {
		this.date = new Date();
		this.text = text;
		store();
	}
	
	/**
	 * Creates the object and sets a description and a parent.
	 * 
	 * @param text   The description
	 * @param parent The parent
	 */
	public Log(String text, Log parent) {
		this(text);
		setParent(parent);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Gets the date.
	 * 
	 * @return The date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Gets the description.
	 * 
	 * @return The description
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the parent.
	 * 
	 * @return The parent
	 */
	public Log getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 *
	 * @param parent The new parent
	 */
	public void setParent(Log parent) {
		this.parent = parent;
		store();
	}
	
	/**
	 * Assembles a string, which enumerates all helpers contained in the given
	 * unit.
	 * 
	 * @param  squad The unit
	 * @return       The string
	 */
	public static String getHelpers(Squad squad) {
		List<Helper> helpers = squad.getHelpers();
		String list = "";
		for (int i = 1; i < helpers.size(); i++) {
			Helper h = helpers.get(i - 1);
			list += h.getFirstName() + " " + h.getLastName() + ", ";
		}
		Helper h = helpers.get(helpers.size() - 1);
		return list + h.getFirstName() + " " + h.getLastName();
	}
	
	/**
	 * Assembles a string, which enumerates all helpers contained in the given
	 * unit.
	 * 
	 * @param  vehicle The unit
	 * @return         The string
	 */
	public static String getHelpers(Vehicle vehicle) {
		List<Helper> helpers = vehicle.getHelpers();
		String list = "";
		for (int i = 1; i < helpers.size(); i++) {
			Helper h = helpers.get(i - 1);
			list += h.getFirstName() + " " + h.getLastName() + ", ";
		}
		Helper h = helpers.get(helpers.size() - 1);
		return list + h.getFirstName() + " " + h.getLastName();
	}

}