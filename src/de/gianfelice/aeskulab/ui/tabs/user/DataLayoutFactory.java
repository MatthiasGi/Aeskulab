package de.gianfelice.aeskulab.ui.tabs.user;

import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;
import de.gianfelice.aeskulab.ui.components.SubmitButton;

/**
 * Creates the relevant fields for the {@link DataLayout}.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.1
 * @see     DataLayout
 */
@SuppressWarnings("deprecation")
public class DataLayoutFactory extends DefaultFieldFactory {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The button, that should be enabled on change. */
	private SubmitButton btnSubmit;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the factory and sets the button.
	 * 
	 * @param btnSubmit The button, that should be enabled, if any field changes
	 */
	public DataLayoutFactory(SubmitButton btnSubmit) {
		this.btnSubmit = btnSubmit;
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field createField(Item item, Object propertyId,
			Component uiContext) {
		TextField txt = new TextField();
		txt.setRequired(true);
		txt.addListener((TextChangeListener) btnSubmit);
		txt.setWidth("100%");
		
		if ("firstName".equals(propertyId)) {
			txt.setCaption("Vorname");
			txt.setRequiredError("Bitte geben Sie einen Vornamen an!");
			txt.addValidator(AuthenticationUtil.VALIDATOR_FIRSTNAME);
		} else if ("lastName".equals(propertyId)) {
			txt.setCaption("Nachname");
			txt.setRequiredError("Bitte geben Sie einen Nachnamen an!");
			txt.addValidator(AuthenticationUtil.VALIDATOR_LASTNAME);
		} else if ("email".equals(propertyId)) {
			txt.setCaption("E-Mail-Adresse");
			txt.setRequiredError("Bitte geben Sie eine E-Mail-Adresse an!");
			txt.addValidator(AuthenticationUtil.VALIDATOR_EMAIL);
		} else {
			return null;
		}
		
		return txt;
	}

}