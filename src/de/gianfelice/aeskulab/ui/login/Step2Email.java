package de.gianfelice.aeskulab.ui.login;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;

/**
 * A step for the wizard creating the first user. In this step, the
 * authentication data (i.e. email and password) are collected.
 * 
 * @author     Matthias Gianfelice
 * @version    2.2.3
 * @see        FirstUser
 * @deprecated Will be removed in the next version and replaced by a whole new
 *             one paged wizard
 */
public class Step2Email implements WizardStep {

	// ------------------------------ Attribute(s) -----------------------------
	/** The parent layout containing the wizard. */
	private FirstUser parent;
	
	/** A form to hold the textfields. */
	private Form form;
	
	/** The field containing the email. */
	private TextField txtEmail;
	
	/** The first field for the password. */
	private PasswordField txtPassword1;
	
	/** The second field for the password. */
	private PasswordField txtPassword2;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates this step and saves the {@link FirstUser}-object.
	 * 
	 * @param parent The parent layout containing the wizard
	 */
	public Step2Email(FirstUser parent) {
		this.parent = parent;
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public String getCaption() {
		return "Zugangsdaten";
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getContent() {
		
		// Creating the main layout
		VerticalLayout verContent = new VerticalLayout();
		verContent.setMargin(true);
		verContent.setSpacing(true);
		verContent.addComponent(new Label("Ihre <b>E-Mail-Adresse</b> und " +
				"Ihr <b>Passwort</b> sind zugleich Ihre Zugangsdaten. Bitte " +
				"geben Sie nun diese Daten im Folgenden ein:",
				Label.CONTENT_XHTML));
		
		// Creating the form
		form = new Form();
		verContent.addComponent(form);
		
		// Email
		txtEmail = new TextField("E-Mail-Adresse");
		txtEmail.setValue(parent.props.getProperty("email", ""));
		txtEmail.setRequired(true);
		txtEmail.addValidator(AuthenticationUtil.VALIDATOR_EMAIL);
		form.getLayout().addComponent(txtEmail);
		txtEmail.focus();
		
		// Password1
		txtPassword1 = new PasswordField("Passwort");
		txtPassword1.setValue(parent.props.getProperty("password", ""));
		txtPassword1.setRequired(true);
		txtPassword1.addValidator(AuthenticationUtil.VALIDATOR_PASSWORD);
		form.getLayout().addComponent(txtPassword1);
		
		// Password2
		txtPassword2 = new PasswordField("Passwort (wiederholen)");
		txtPassword2.setValue(parent.props.getProperty("password", ""));
		txtPassword2.setRequired(true);
		txtPassword2.addValidator(AuthenticationUtil.VALIDATOR_PASSWORD);
		form.getLayout().addComponent(txtPassword2);
		
		// Return
		return verContent;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean onAdvance() {
		form.setComponentError(null);
		
		try {
			txtEmail.validate();
		} catch (EmptyValueException e) {
			error("Bitte geben Sie eine E-Mail-Adresse an!");
			return false;
		} catch (Exception e) {
			return false;
		}
		
		try {
			txtPassword1.validate();
			txtPassword2.validate();
		} catch (EmptyValueException e) {
			error("Bitte geben Sie das gewünschte Passwort zweimal ein!");
			return false;
		} catch (Exception e) {
			return false;
		}
		
		String password1 = (String) txtPassword1.getValue();
		String password2 = (String) txtPassword2.getValue();
		if (!password1.equals(password2)) {
			error("Die beiden Passwörter stimmen nicht überein!");
			return false;
		}
		
		parent.props.setProperty("email", (String) txtEmail.getValue());
		parent.props.setProperty("password", password1);	
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean onBack() {
		return true;
	}
	
	/**
	 * Displays an error-message.
	 * 
	 * @param msg The message
	 */
	private void error(String msg) {
		form.setComponentError(new UserError(msg));
	}

}