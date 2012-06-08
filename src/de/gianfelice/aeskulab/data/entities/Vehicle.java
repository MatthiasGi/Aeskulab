package de.gianfelice.aeskulab.data.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.gianfelice.aeskulab.system.persistence.PersistentObject;
import de.gianfelice.aeskulab.system.utils.FileUtil;

/**
 * An entity to store vehicles.
 * 
 * @author  Matthias Gianfelice
 * @version 0.2.0
 */
@Entity
@Table(name = "vehicle")
public class Vehicle extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** An image of the vehicle. */
	private String image;
	
	/** A tactical image of the vehicle. */
	private String tacticalImage;
	
	/** The current state (FMS) of the vehicle. */
	private int state;
	
	/** The name. */
	@Column(nullable = false, unique = true)
	private String name;
	
	/** The type. */
	@JoinColumn(nullable = false)
	private SquadType type;
	
	/** All helpers, that are assigned to this vehicle. */
	@ManyToMany
	@JoinTable(name = "helper_vehicle")
	private List<Helper> helpers = new ArrayList<Helper>();
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * An empty constructor which does nothing.
	 * 
	 * @see #Vehicle(String)
	 */
	public Vehicle() {}
	
	/**
	 * Initiates a new vehicle and sets the name. This constructor is
	 * preferred.
	 * 
	 * @param name The name for this vehicle
	 */
	public Vehicle(String name) {
		this.name = name;
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * Returns the image of the vehicle.
	 * 
	 * @return The image of the vehicle or null, if there is none.
	 * @see    FileUtil
	 */
	public File getImage() {
		if (image == null || image.equals("")) return null;
		
		try {
			File file = FileUtil.getFile(FileUtil.IMAGE_VEHICLE, image);
			if (!file.isFile() || !file.exists()) return null;
			return file;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Sets the image of the current vehicle.
	 * 
	 * @param image The new image of the vehicle
	 */
	public void setImage(File image) {
		if (image == null) {
			this.image = null;
		} else {
			this.image = image.getName();
		}
		store();
	}

	/**
	 * Returns the tactical image of the vehicle.
	 * 
	 * @return The name of the tactical image
	 */
	public File getTacticalImage() {
		if (tacticalImage == null || tacticalImage.equals("")) return null;
		
		try {
			File file = FileUtil.getFile(FileUtil.IMAGE_TACTICAL, tacticalImage);
			if (!file.isFile() || !file.exists()) return null;
			return file;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Sets the tactical image of the vehicle.
	 * 
	 * @param image The new tactical image
	 */
	public void setTacticalImage(File image) {
		if (image == null) {
			tacticalImage = null;
		} else {
			tacticalImage = image.getName();
		}
		store();
	}
	
	/**
	 * Gets the state.
	 * 
	 * @return The state
	 */
	public int getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * 
	 * @param state The new state
	 */
	public void setState(int state) {
		this.state = state;
		store();
	}

	/**
	 * Gets the name.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name The new name
	 */
	public void setName(String name) {
		this.name = name;
		store();
	}

	/**
	 * Gets the type.
	 * 
	 * @return The type
	 */
	public SquadType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type The new type
	 */
	public void setType(SquadType type) {
		this.type = type;
		store();
	}
	
	/**
	 * Assigns a new helper to this vehicle.
	 * 
	 * @param helper The new helper
	 */
	public void addHelper(Helper helper) {
		if (helper == null || helpers.contains(helper)) return;
		if (helpers.add(helper)) store();
	}
	
	/**
	 * Removes a helper from this vehicle
	 * 
	 * @param helper The helper
	 */
	public void removeHelper(Helper helper) {
		if (helpers.remove(helper)) store();
	}
	
	/**
	 * Gets the helpers, that are assigned to this vehicle.
	 * 
	 * @return The helpers
	 */
	public List<Helper> getHelpers() {
		return helpers;
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
	
	/**
	 * Checks, whether the requirements of the squad type are fulfilled. Note:
	 * Currently, it does simply return {@code true}.
	 * 
	 * @return Whether the squad type requirements are fulfilled
	 */
	public boolean hasRequirements() {
		return true;
	}

}