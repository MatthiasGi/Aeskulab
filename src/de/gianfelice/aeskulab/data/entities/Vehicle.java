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
 * @version 0.4.0
 */
@Entity
@Table(name = "vehicle")
public class Vehicle extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** An image of the vehicle. */
	private String image;
	
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
	
	/** Top position in layout. */
	private Integer topPos;
	
	/** Left position in layout. */
	private Integer leftPos;
	
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
		this.state = 6;
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
		if (this.state == state) return;
		this.state = state;
		new Log("Fahrzeug " + name + " (" + Log.getHelpers(this) +
				") meldet S" + state);
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
		if (this.name.equals(name)) return;
		new Log("Fahrzeug " + this.name + " (" + Log.getHelpers(this) +
				") ist nun unter " + name + " erreichbar.");
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
		if (this.type.getName().equals(type.getName())) return;
		this.type = type;
		new Log("Fahrzeug " + name + " (" + Log.getHelpers(this) +
				") hat nun eine Besatzung vom Typ " + type.getName() + ".");		
		store();
	}
	
	/**
	 * Assigns a new helper to this vehicle.
	 * 
	 * @param helper The new helper
	 */
	public void addHelper(Helper helper) {
		if (helper == null || helpers.contains(helper)) return;
		if (helpers.add(helper)) {
			new Log("Fahrzeug " + name + " wurde um " + helper.getFirstName() +
					" " + helper.getLastName() +
					" verstärkt. Neue Besatzung: " + Log.getHelpers(this));
			store();
		}
	}
	
	/**
	 * Removes a helper from this vehicle
	 * 
	 * @param helper The helper
	 */
	public void removeHelper(Helper helper) {
		if (helpers.remove(helper)) {
			new Log(helper.getFirstName() + " " + helper.getLastName() +
					" wurde aus Fahrzeug " + name +
					" entfernt. Neue Besatzung: " + Log.getHelpers(this));
			store();
		}
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
		new Log("Besatzung des Fahrzeugs " + name + " wurde verändert: " +
				Log.getHelpers(this));
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof Vehicle)) return false;
		Vehicle vehicle = (Vehicle) obj;
		return getId().equals(vehicle.getId());
	}

}