package de.gianfelice.aeskulab.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.gianfelice.aeskulab.system.persistence.PersistentObject;

/**
 * A helper "draft" that should be used in squad "drafts".
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 * @see     SquadType
 */
@Entity
@Table(name = "helperType")
public class HelperType extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** Minimal qualifications for the helper. */
	@ManyToMany
	@JoinTable(name = "qualification_helpertype")
	private List<Qualification> qualifications = new ArrayList<Qualification>();
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * An empty constructor to create a new object.
	 */
	public HelperType() {}
	
	// ------------------------------- Method(s) -------------------------------	
	/**
	 * Gets the qualifications.
	 * 
	 * @return The qualifications
	 */
	public List<Qualification> getQualifications() {
		return qualifications;
	}

	/**
	 * Sets the qualifications.
	 * 
	 * @param qualifications The new qualifications
	 */
	public void setQualifications(List<Qualification> qualifications) {
		this.qualifications = qualifications;
		store();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void store() {
		super.store();
	}

}