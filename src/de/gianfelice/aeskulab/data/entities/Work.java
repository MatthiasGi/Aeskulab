package de.gianfelice.aeskulab.data.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.gianfelice.aeskulab.system.persistence.PersistentObject;

/**
 * An entity to collect all information about work.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 */
@Entity
@Table(name = "work")
public class Work extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The caption. */
	@Column(nullable = false)
	private String caption;
	
	/** The place. */
	private String place;

	/** The time. */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date time;
	
	/** Different remarks. */
	private String remarks;
	
	/** The number of involved persons. */
	private int numberOfPersons;
	
	/** The caller. */
	private String caller;
	
	/** The number of the assigned protocol. */
	private int numberOfProtocol;
	
	/** The list of assigned squads. */
	private List<Squad> squads;
	
	/** The list of assigned vehicles. */
	private List<Vehicle> vehicles;
	
	/** Top position in layout. */
	private Integer topPos;
	
	/** Left position in layout. */
	private Integer leftPos;
	
	/** Whether the work is done. */
	@Column(nullable = false)
	private boolean ready;
	
	/** An assigned log. */
	private Log log;
	
	// ----------------------------- Constructor(s) ----------------------------	
	/**
	 * An empty constructor which does nothing.
	 * 
	 * @see #Work(String)
	 */
	public Work() {}
	
	/**
	 * Creates the object and sets the caption.
	 * 
	 * @param caption The caption
	 */
	public Work(String caption) {
		this.caption = caption;
		this.ready = false;
		log = new Log("Einsatz " + caption + " wurde angelegt.");
		store();
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Gets the caption.
	 * 
	 * @return The caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Sets the caption.
	 * 
	 * @param caption The new caption
	 */
	public void setCaption(String caption) {
		if (this.caption != null && this.caption.equals(caption)) return;
		this.caption = caption;
		new Log("Einsatzbetreff wurde zu " + caption + " geändert.", log);
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
		if (this.place != null && this.place.equals(place)) return;
		this.place = place;
		new Log(place + " wurde als Ort festgelegt.", log);
		store();
	}

	/**
	 * Gets the remarks.
	 * 
	 * @return The remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * Sets the remarks.
	 * 
	 * @param remarks The new remarks
	 */
	public void setRemarks(String remarks) {
		if (this.remarks != null && this.remarks.equals(remarks)) return;
		this.remarks = remarks;
		new Log("Bemerkungen: " + remarks, log);
		store();
	}

	/**
	 * Gets the number of persons.
	 * 
	 * @return The number of persons
	 */
	public int getNumberOfPersons() {
		return numberOfPersons;
	}

	/**
	 * Sets the number of persons.
	 * 
	 * @param numberOfPersons The new number of persons
	 */
	public void setNumberOfPersons(int numberOfPersons) {
		if (this.numberOfPersons == numberOfPersons) return;
		this.numberOfPersons = numberOfPersons;
		new Log("Es sind " + numberOfPersons + " Personen betroffen.", log);
		store();
	}

	/**
	 * Gets the caller.
	 * 
	 * @return The caller
	 */
	public String getCaller() {
		return caller;
	}

	/**
	 * Sets the caller.
	 * 
	 * @param caller The new caller
	 */
	public void setCaller(String caller) {
		if (this.caller != null && this.caller.equals(caller)) return;
		this.caller = caller;
		new Log("Der Name des Anrufers lautet " + caller + ".", log);
		store();
	}

	/**
	 * Gets the number of the protocol.
	 * 
	 * @return The number of the protocol
	 */
	public int getNumberOfProtocol() {
		return numberOfProtocol;
	}

	/**
	 * Sets the number of the protocol.
	 * 
	 * @param numberOfProtocol The new number of the protocol
	 */
	public void setNumberOfProtocol(int numberOfProtocol) {
		if (this.numberOfProtocol == numberOfProtocol) return;
		this.numberOfProtocol = numberOfProtocol;
		new Log("Protokoll Nummer " + numberOfProtocol +
				" wurde dem Einsatz zugeordnet.", log);
		store();
	}

	/**
	 * Gets the time.
	 * 
	 * @return The time
	 */
	public Date getTime() {
		return time;
	}
	
	/**
	 * Sets the time.
	 * 
	 * @param time The new time
	 */
	public void setTime(Date time) {
		if (this.time != null && this.time.equals(time)) return;
		this.time = time;
		new Log("Einsatz fand um " + time + " statt.", log);
		store();
	}

	/**
	 * Gets the assigned squads.
	 * 
	 * @return The assigned squads
	 */
	public List<Squad> getSquads() {
		return squads;
	}

	/**
	 * Gets the assigned vehicles.
	 * 
	 * @return The assigned vehicles
	 */
	public List<Vehicle> getVehicles() {
		return vehicles;
	}
	
	/**
	 * Assigns a squad to the object.
	 * 
	 * @param squad The new squad
	 */
	public void addSquad(Squad squad) {
		if (squads.add(squad)) {
			store();
			new Log("Trupp " + squad.getName() + " (" + Log.getHelpers(squad) +
					") wurde dem Einsatz zugeteilt", log);
		}
	}
	
	/**
	 * Removes a squad from the object.
	 * 
	 * @param squad The squad
	 */
	public void removeSquad(Squad squad) {
		if (squads.remove(squad)) {
			store();
			new Log("Für Trupp " + squad.getName() + " (" +
					Log.getHelpers(squad) + ") wurde der Einsatz beendet.",
					log);
		}
	}
	
	/**
	 * Assigns a vehicle to the object.
	 * 
	 * @param vehicle The new vehicle
	 */
	public void addVehicle(Vehicle vehicle) {
		if (vehicles.add(vehicle)) {
			store();
			new Log("Fahrzeug " + vehicle.getName() + " (" +
					Log.getHelpers(vehicle) + ") wurde dem Einsatz zugeteilt.",
					log);
		}
	}
	
	/**
	 * Removes a vehicle from the object.
	 * 
	 * @param vehicle The vehicle
	 */
	public void removeVehicle(Vehicle vehicle) {
		if (vehicles.remove(vehicle)) {
			store();
			new Log("Für Fahrzeug " + vehicle.getName() + " (" +
					Log.getHelpers(vehicle) + ") wurde der Einsatz beendet.",
					log);
		}
	}

	/**
	 * Checks, if the work is done.
	 * 
	 * @return Whether the work is done
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * Sets the work to done or not done.
	 * 
	 * @param ready Whether the work is done
	 */
	public void setReady(boolean ready) {
		if (ready && !this.ready) {
			squads = new ArrayList<Squad>();
			vehicles = new ArrayList<Vehicle>();
			topPos = null;
			leftPos = null;
			new Log("Der Einsatz wurde beendet.", log);
		}

		this.ready = ready;
		store();
	}
	
	/**
	 * Gets the log.
	 * 
	 * @return The log
	 */
	public Log getLog() {
		return log;
	}
	
	/**
	 * Gets the top position.
	 * 
	 * @return The top position
	 */
	public Integer getTop() {
		return topPos;
	}
	
	/**
	 * Sets the top position.
	 * 
	 * @param top The new top position
	 */
	public void setTop(Integer top) {
		this.topPos = top;
		store();
	}
	
	/**
	 * Gets the left position.
	 * 
	 * @return The left position
	 */
	public Integer getLeft() {
		return leftPos;
	}
	
	/**
	 * Sets the left position.
	 * 
	 * @param left The new left position
	 */
	public void setLeft(Integer left) {
		this.leftPos = left;
		store();
	}

}