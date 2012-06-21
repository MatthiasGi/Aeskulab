package de.gianfelice.aeskulab.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.gianfelice.aeskulab.system.persistence.PersistentObject;

/**
 * An entity to group {@link Helper} objects inside a squad.
 * 
 * @author  Matthias Gianfelice
 * @version 0.3.0
 * @see     Helper
 */
@Entity
@Table(name = "squad")
public class Squad extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The current state (FMS) of the squad. */
	private int state;
	
	/** The name. */
	@Column(nullable = false, unique = true)
	private String name;
	
	/** The type. */
	@JoinColumn(nullable = false)
	private SquadType type;
	
	/** All helpers, that are assigned to this squad. */
	@ManyToMany
	@JoinTable(name = "helper_squad")
	private List<Helper> helpers = new ArrayList<Helper>();
	
	/** Top position in layout. */
	private Integer topPos;
	
	/** Left position in layout. */
	private Integer leftPos;

	// ----------------------------- Constructor(s) ----------------------------	
	/**
	 * An empty constructor which does nothing.
	 * 
	 * @see #Squad(String, SquadType)
	 */
	public Squad() {}
	
	/**
	 * Instantiates a new squad and sets the name. This constructor is
	 * preferred.
	 *
	 * @param name The name for this squad
	 * @param type The type of this squad
	 */
	public Squad(String name, SquadType type) {
		this.name = name;
		this.type = type;
		this.state = 6;
		store();
	}

	// ------------------------------- Method(s) -------------------------------
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
		new Log("Trupp " + name + " (" + Log.getHelpers(this) + ") meldet S" +
				state);
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
	 * Sets the name.
	 * 
	 * @param name The new name
	 */
	public void setName(String name) {
		if (this.name.equals(name)) return;
		new Log("Trupp " + this.name + " (" + Log.getHelpers(this) +
				") ist nun unter " + name + " erreichbar.");
		this.name = name;
		store();
	}

	/**
	 * Gets the squad type.
	 * 
	 * @return The type
	 */
	public SquadType getType() {
		return type;
	}

	/**
	 * Sets the squad type.
	 * 
	 * @param type The new type
	 */
	public void setType(SquadType type) {
		if (this.type.getName().equals(type.getName())) return;
		this.type = type;
		new Log("Trupp " + name + " (" + Log.getHelpers(this) +
				") ist nun vom Typ " + type.getName() + ".");
		store();
	}
	
	/**
	 * Adds a helper to this squad.
	 * 
	 * @param helper The helper
	 */
	public void addHelper(Helper helper) {
		if (helper == null || helpers.contains(helper)) return;
		if (helpers.add(helper)) {
			new Log("Trupp " + name + " wurde um " + helper.getFirstName() +
					" " + helper.getLastName() +
					" erweitert. Neue Besatzung: " + Log.getHelpers(this));
			store();
		}
	}
	
	/**
	 * Removes a helper from this squad.
	 * 
	 * @param helper The helper
	 */
	public void removeHelper(Helper helper) {
		if (helpers.remove(helper)) {
			new Log(helper.getFirstName() + " " + helper.getLastName() +
					" wurde aus Trupp " + name + " entfernt. Neue Besatzung: " +
					Log.getHelpers(this));
			store();
		}
	}
	
	/**
	 * Gets the helpers, that are assigned to this squad.
	 * 
	 * @return The helpers
	 */
	public List<Helper> getHelpers() {
		return helpers;
	}
	
	/**
	 * Sets the helpers, that are assigned to this squad.
	 * 
	 * @param helpers The new helpers
	 */
	public void setHelpers(List<Helper> helpers) {
		this.helpers = helpers;
		new Log("Besatzung des Trupps " + name + " wurde verändert: " +
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
		if (obj == null || !(obj instanceof Squad)) return false;
		Squad squad = (Squad) obj;
		return getId().equals(squad.getId());
	}

}