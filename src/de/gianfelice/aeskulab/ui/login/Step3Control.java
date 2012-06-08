package de.gianfelice.aeskulab.ui.login;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;

/**
 * A step for the wizard creating the first user. In this step, the user should
 * control the 
 * 
 * @author     Matthias Gianfelice
 * @version    2.0.4
 * @see        FirstUser
 * @deprecated Will be removed in the next version and replaced by a whole new
 *             one paged wizard
 */
public class Step3Control implements WizardStep {

	// ------------------------------ Attribute(s) -----------------------------
	/** The parent layout containing the wizard. */
	private FirstUser parent;

	/** The content layout. */
	private VerticalLayout verContent;
	
	/** Contains the first name of the user. */
	private String firstName;
	
	/** Contains the last name of the user. */
	private String lastName;
	
	/** Contains the email of the user. */
	private String email;
	
	/** Contains the password of the user. */
	private String password;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates this step and saves the {@link FirstUser}-object.
	 * 
	 * @param parent The parent layout containing the wizard
	 */
	public Step3Control(FirstUser parent) {
		this.parent = parent;
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public String getCaption() {
		return "Kontrolle";
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getContent() {
		
		// Content
		verContent = new VerticalLayout();
		verContent.setMargin(true);
		verContent.setSpacing(true);
		verContent.addComponent(new Label("<b>Kontrollieren</b> Sie im " +
				"Folgenden bitte die von Ihnen gemachten Angeben und " +
				"korrigieren Sie diese bei Bedarf durch einen Klick auf den " +
				"<tt>Zurück</tt>-Knopf unten.", Label.CONTENT_XHTML));
		
		// Collect information
		firstName = parent.props.getProperty("firstName");
		lastName = parent.props.getProperty("lastName");
		email = parent.props.getProperty("email");
		password = parent.props.getProperty("password");
		
		// Grid for layout
		GridLayout grid = new GridLayout(2, 4);
		grid.setMargin(true);
		grid.setSpacing(true);
		verContent.addComponent(grid);
		verContent.setComponentAlignment(grid, Alignment.TOP_CENTER);
		
		// Display information
		grid.addComponent(new Label("<b>Vorname:</b>", Label.CONTENT_XHTML));
		grid.addComponent(new Label(firstName));
		grid.addComponent(new Label("<b>Nachname:</b>", Label.CONTENT_XHTML));
		grid.addComponent(new Label(lastName));
		grid.addComponent(new Label("<b>E-Mail:</b>", Label.CONTENT_XHTML));
		grid.addComponent(new Label(email));
		grid.addComponent(new Label("<b>Passwort:</b>", Label.CONTENT_XHTML));
		String passwordReplace = "";
		for (int i = 0; i < password.length(); i ++) {
			passwordReplace += "&bull;";
		}
		grid.addComponent(new Label(passwordReplace, Label.CONTENT_XHTML));
		
		
		// Return
		return verContent;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean onAdvance() {
		
		// Create first user
		if (!AuthenticationUtil.createFirstUser(firstName, lastName, email,
				password)) {
			verContent.getWindow().showNotification("Bei der Erstellung des" +
					"Nutzers ist ein Fehler aufgetreten. Wurde in der " +
					"Zwischenzeit auf einem anderen Rechner ein Nutzer " +
					"registriert?");
			return false;
		}
		
		// Return
		return true;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean onBack() {
		return true;
	}

}