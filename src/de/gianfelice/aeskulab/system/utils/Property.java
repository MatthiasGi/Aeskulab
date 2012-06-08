package de.gianfelice.aeskulab.system.utils;

import java.io.Serializable;

/**
 * A simple container to easily handle standardised global properties of the
 * application.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.2
 * @see     PropertyUtil
 */
public class Property implements Serializable {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The name of the current property. */
	private String name;
	
	/** The default value of the current property. */
	private String defaultValue;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates a new Property with the given options.
	 * 
	 * @param name         The name of the newly created property
	 * @param defaultValue The default value to return
	 */
	public Property(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Returns the name of the current property.
	 * 
	 * @return The name of the property
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the default value of the current property.
	 * 
	 * @return The default value of the property
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

}