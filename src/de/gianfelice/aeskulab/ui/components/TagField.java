package de.gianfelice.aeskulab.ui.components;

import org.vaadin.tokenfield.TokenField;

import com.vaadin.ui.Button;

/**
 * Extends the {@link TokenField} to better match the application and translate
 * it.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class TagField extends TokenField {
	
	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the field.
	 */
	public TagField() {
		super();
		setRememberNewTokens(false);
		setNewTokensAllowed(false);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureTokenButton(Object tokenId, Button button) {
		super.configureTokenButton(tokenId, button);
        button.setDescription("Klicken zum Entfernen");
    }

}