package de.gianfelice.aeskulab.data.entities;

import java.io.File;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.system.persistence.PersistentObject;
import de.gianfelice.aeskulab.system.utils.FileUtil;

/**
 * An entity to manage helpers, that can optionally be linked to a {@link User}
 * object.
 *  
 * @author  Matthias Gianfelice
 * @version 0.2.1
 * @see     User
 */
@Entity
@Table(name = "helper")
public class Helper extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The first name. */
	@Column(nullable = false)
	private String firstName;
	
	/** The last name. */
	@Column(nullable = false)
	private String lastName;
	
	/** The image of the user. */
	private String image;
	
	/** The telephone number. */
	private String telNumber;
	
	/** All qualifications of the user. */
	@ManyToMany
	@JoinTable(name = "qualification_helper")
	private List<Qualification> qualifications;
	
	/** Top position in layout. */
	private Integer topPos;
	
	/** Left position in layout. */
	private Integer leftPos;
	
	// ----------------------------- Constructor(s) ----------------------------	
	/**
	 * An empty constructor which does nothing.
	 * 
	 * @see #Helper(String, String)
	 */
	public Helper() {}
	
	/**
	 * Creates the object, sets the name and saves it.
	 * 
	 * @param firstName The first name
	 * @param lastName  The last name
	 */
	public Helper(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		store();
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * Gets the first name.
	 *
	 * @return The first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName The new first name
	 */
	public void setFirstName(String firstName) {		
		if (firstName == null || this.firstName.equals(firstName)) return;
		this.firstName = firstName;
		store();
	}

	/**
	 * Gets the last name.
	 *
	 * @return The last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName The new last name
	 */
	public void setLastName(String lastName) {
		if (lastName == null || this.lastName.equals(lastName)) return;
		this.lastName = lastName;
		store();
	}

	/**
	 * Gets the image.
	 *
	 * @return The image of the helper or null, if there is none.
	 * @see    FileUtil
	 */
	public File getImage() {
		if (image == null || image.equals("")) return null;
		
		try {
			File file = FileUtil.getFile(FileUtil.IMAGE_USER, image);
			if (!file.isFile() || !file.exists()) return null;
			return file;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Sets the image.
	 *
	 * @param image The new image
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
	 * Gets the telephone number.
	 *
	 * @return The telephone number
	 */
	public String getTelNumber() {
		return telNumber;
	}

	/**
	 * Sets the telephone number.
	 *
	 * @param telNumber The new telephone number
	 */
	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
		store();
	}
	
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
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof Helper)) return false;
		Helper helper = (Helper) obj;
		return getId().equals(helper.getId());
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