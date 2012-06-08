package de.gianfelice.aeskulab.data.entities;

import java.io.File;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.gianfelice.aeskulab.system.persistence.PersistentObject;
import de.gianfelice.aeskulab.system.utils.FileUtil;

/**
 * Remarkable places, e.g. treatment tent.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 */
@Entity
@Table(name = "place")
public class Place extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** Name of the place. */
	@Column(nullable = false)
	private String name;
	
	/** The squads, that are stationed at the place. */
	@ManyToMany
	@JoinTable(name = "squad_place")
	private List<Squad> squads;
	
	/** The vehicles, that are stationed at the place. */
	@ManyToMany
	@JoinTable(name = "vehicle_place")
	private List<Vehicle> vehicles;
	
	/** The helpers, that are stationed at the place. */
	@ManyToMany
	@JoinTable(name = "helper_place")
	private List<Helper> helpers;
	
	/** An image of the place. */
	private String image;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * An empty constructor to create the object.
	 * 
	 * @see #Place(String)
	 */
	public Place() {}
	
	/**
	 * Instantiates a new place and sets the name. This constructor is
	 * preferred.
	 * 
	 * @param name The name
	 */
	public Place(String name) {
		setName(name);
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * Gets the name.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name The new name
	 */
	public void setName(String name) {
		this.name = name;
		store();
	}
	
	/**
	 * Adds a helper.
	 *
	 * @param helper The helper
	 */
	public void addHelper(Helper helper) {
		if (helper == null || helpers.contains(helper)) return;
		if (helpers.add(helper)) store();
	}
	
	/**
	 * Removes a helper.
	 *
	 * @param helper The helper
	 */
	public void removeHelper(Helper helper) {
		if (helpers.remove(helper)) store();
	}
	
	/**
	 * Gets the helpers.
	 *
	 * @return The helpers
	 */
	public List<Helper> getHelpers() {
		return helpers;
	}
	
	/**
	 * Adds a vehicle.
	 *
	 * @param vehicle The vehicle
	 */
	public void addVehicle(Vehicle vehicle) {
		if (vehicle == null || vehicles.contains(vehicle)) return;
		if (vehicles.add(vehicle)) store();
	}
	
	/**
	 * Removes a vehicle.
	 *
	 * @param vehicle The vehicle
	 */
	public void removeCar(Vehicle vehicle) {
		if (vehicles.remove(vehicle)) store();
	}
	
	/**
	 * Gets the vehicles.
	 *
	 * @return The vehicles
	 */
	public List<Vehicle> getCars() {
		return vehicles;
	}
	
	/**
	 * Adds a squad.
	 *
	 * @param squad The squad
	 */
	public void addSquad(Squad squad) {
		if (squad == null || squads.contains(squad)) return;
		if (squads.add(squad)) store();
	}
	
	/**
	 * Removes a squad.
	 *
	 * @param squad The squad
	 */
	public void removeSquad(Squad squad) {
		if (squads.remove(squad)) store();
	}
	
	/**
	 * Gets the squads.
	 *
	 * @return The squads
	 */
	public List<Squad> getSquads() {
		return squads;
	}

	/**
	 * Gets the image.
	 *
	 * @return The image
	 */
	public File getImage() {
		if (image == null || image.equals("")) return null;
		
		try {
			File file = FileUtil.getFile(FileUtil.IMAGE_PLACE, image);
			if (!file.isFile() || !file.exists()) return null;
			return file;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Sets the image.
	 *
	 * @param image the new image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Gets the vehicles.
	 * 
	 * @return The vehicles
	 */
	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	/**
	 * Sets the vehicles.
	 * 
	 * @param vehicles The new vehicles
	 */
	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
		store();
	}

	/**
	 * Sets the squads.
	 * 
	 * @param squads The new squads
	 */
	public void setSquads(List<Squad> squads) {
		this.squads = squads;
		store();
	}

	/**
	 * Sets the helpers.
	 * 
	 * @param helpers The new helpers
	 */
	public void setHelpers(List<Helper> helpers) {
		this.helpers = helpers;
		store();
	}

}