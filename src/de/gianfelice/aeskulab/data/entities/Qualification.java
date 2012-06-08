package de.gianfelice.aeskulab.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.gianfelice.aeskulab.system.persistence.PersistentObject;

/**
 * Represents qualifications for each helper.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 */
@Entity
@Table(
	name = "qualification",
	uniqueConstraints = { @UniqueConstraint(columnNames = {"name"}) }
)
public class Qualification extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The name. */
	@Column(nullable = false, unique = true)
	private String name;
	
	/** A list of helpers with this qualification. */
	@ManyToMany(mappedBy = "qualifications")
	private List<Helper> helpers = new ArrayList<Helper>();
	
	/** A list of qualifications, that are "better" than the actual one. */
	@ManyToMany(mappedBy="minorQualifications")
	private List<Qualification> majorQualifications =
			new ArrayList<Qualification>();
	
	/** A list of qualifications, that are "below" the actual one. */
	@ManyToMany
	@JoinTable(name = "qualification_qualification")
	private List<Qualification> minorQualifications =
			new ArrayList<Qualification>();
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * An empty constructor which does nothing.
	 * 
	 * @see #Qualification(String)
	 */
	public Qualification() {}
	
	/**
	 * Creates a qualification with the given name. This constructor is
	 * preferred.
	 * 
	 * @param name The name
	 */
	public Qualification(String name) {
		this.name = name;
		store();
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
	 * Adds a minor qualification.
	 * 
	 * @param qualification The minor qualification
	 */
	public void addMinorQualification(Qualification qualification) {
		if (qualification == null || qualification == this
				|| minorQualifications.contains(qualification)) return;
		if (minorQualifications.add(qualification)) store();
		qualification.addMajorQualification(this);
	}
	
	/**
	 * Removes a minor qualification.
	 * 
	 * @param qualification The minor qualification
	 */
	public void removeMinorQualification(Qualification qualification) {
		if (minorQualifications.remove(qualification)) store();
		qualification.removeMajorQualification(qualification);
	}
	
	/**
	 * Gets the minor qualifications.
	 * 
	 * @return The minor qualifications
	 */
	public List<Qualification> getMinorQualifications() {
		return minorQualifications;
	}
	
	/**
	 * Adds a major qualification.
	 * 
	 * @param qualification The major qualification.
	 */
	private void addMajorQualification(Qualification qualification) {
		if (qualification == null || qualification == this
				|| majorQualifications.contains(qualification)) return;
		if (majorQualifications.add(qualification)) store();
	}
	
	/**
	 * Removes a major qualification.
	 * 
	 * @param qualification The major qualification.
	 */
	private void removeMajorQualification(Qualification qualification) {
		if (majorQualifications.remove(qualification)) store();
	}
	
	/**
	 * Gets the major qualifications.
	 * 
	 * @return The major qualifications
	 */
	public List<Qualification> getMajorQualifications() {
		return majorQualifications;
	}
	
	/**
	 * Adds a helper with this qualification.
	 * 
	 * @param helper The helper
	 */
	public void addHelper(Helper helper) {
		if (helper == null || helpers.contains(helper)) return;
		if (helpers.add(helper)) store();
	}
	
	/**
	 * Removes a helper, which no longer has this qualification.
	 * 
	 * @param helper The helper
	 */
	public void removeHelper(Helper helper) {
		if (helpers.remove(helper)) store();
	}
	
	/**
	 * Gets the helpers with this qualification.
	 * 
	 * @return The helpers
	 */
	public List<Helper> getHelpers() {
		return helpers;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof Qualification)) return false;
		Qualification qualification = (Qualification) obj;
		return getName().equals(qualification.getName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Sets the minor qualifications.
	 * 
	 * @param minorQualifications The new minor qualifications
	 */
	public void setMinorQualifications(List<Qualification> minorQualifications) {
		this.minorQualifications.clear();
		for (Qualification qual : minorQualifications)
			addMinorQualification(qual);
		store();
	}

}