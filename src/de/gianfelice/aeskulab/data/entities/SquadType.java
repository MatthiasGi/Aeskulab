package de.gianfelice.aeskulab.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.gianfelice.aeskulab.system.persistence.PersistentObject;

/**
 * A squad "draft".
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Squad
 * @see     HelperType
 */
@Entity
@Table(name = "squadType")
public class SquadType extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The name of the draft. */
	@Column(nullable = false, unique = true)
	private String name;
	
	/** A list of all required helper types. */
	@ManyToMany
	@JoinTable(name = "helpertype_squadtype")
	private List<HelperType> helperTypes = new ArrayList<HelperType>();
	
	// ----------------------------- Constructor(s) ----------------------------	
	/**
	 * An empty constructor which does nothing.
	 * 
	 * @see #SquadType(String)
	 */
	public SquadType() {}
	
	/**
	 * Instantiates a new squad type and sets the name. This constructor is
	 * preferred.
	 *
	 * @param name The name for this type
	 */
	public SquadType(String name) {
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
	 * Adds a helper type.
	 * 
	 * @param type The new helper type
	 */
	public void addHelperType(HelperType type) {
		if (type == null || helperTypes.contains(type)) return;
		if (helperTypes.add(type)) store();
	}
	
	/**
	 * Removes a helper type.
	 * 
	 * @param type The helper type
	 */
	public void removeHelperType(HelperType type) {
		if (helperTypes.remove(type)) store();
	}
	
	/**
	 * Gets all helper types.
	 * 
	 * @return The helper types
	 */
	public List<HelperType> getHelperTypes() {
		return helperTypes;
	}
	
	/**
	 * Sets the helper types.
	 * 
	 * @param helperTypes The new helper types
	 */
	public void setHelperTypes(List<HelperType> helperTypes) {
		this.helperTypes = helperTypes;
		store();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}

}