package de.gianfelice.aeskulab.ui.login;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;

/**
 * A step for the wizard creating the first user. In this step, the user should
 * provide his first and last name.
 * 
 * @author     Matthias Gianfelice
 * @version    2.2.4
 * @see        FirstUser
 * @deprecated Will be removed in the next version and replaced by a whole new
 *             one paged wizard
 */
public class Step1Name implements WizardStep {

	// ------------------------------ Attribute(s) -----------------------------	
	/** The parent layout containing the wizard. */
	private FirstUser parent;
	
	/** The field containing the first name. */
	private TextField txtFirstName;
	
	/** The field containing the last name. */
	private TextField txtLastName;
	
	/** A form do hold the textfields. */
	private Form form;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates this step and saves the {@link FirstUser}-object.
	 * 
	 * @param parent The parent layout containing the wizard
	 */
	public Step1Name(FirstUser parent) {
		this.parent = parent;
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public String getCaption() {
		return "Willkommen";
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getContent() {
		
		// Content
		VerticalLayout verContent = new VerticalLayout();
		verContent.setMargin(true);
		verContent.setSpacing(true);
		
		// Image
		Embedded embImage = new Embedded();
		embImage.setSource(new ClassResource("res/150/AeskulabBlack.png",
				parent.getApplication()));
		embImage.setHeight("150px");
		embImage.setWidth("150px");
		verContent.addComponent(embImage);
		verContent.setComponentAlignment(embImage, Alignment.TOP_CENTER);
		
		// Labels
		verContent.addComponent(new Label("<b>Willkommen</b> bei der " +
				"Software <b>Aeskulab</b>. Dieser Assistent wird Ihnen bei " +
				"der Erstellung eines ersten Benutzerkontos, das über volle " +
				"Administrationsrechte verfügt, behilflich sein.",
				Label.CONTENT_XHTML));
		verContent.addComponent(new Label("Bitte geben Sie dazu zunächst " +
				"Ihren <b>Vor- und Nachnamen</b> an:", Label.CONTENT_XHTML));
		
		form = new Form();
		verContent.addComponent(form);

		// First Name
		txtFirstName = new TextField("Vorname");
		txtFirstName.setValue(parent.props.getProperty("firstName", ""));
		txtFirstName.setRequired(true);
		txtFirstName.addValidator(AuthenticationUtil.VALIDATOR_FIRSTNAME);
		form.getLayout().addComponent(txtFirstName);
		txtFirstName.focus();
		
		// LastName
		txtLastName = new TextField("Nachname");
		txtLastName.setValue(parent.props.getProperty("lastName", ""));
		txtLastName.setRequired(true);
		txtLastName.addValidator(AuthenticationUtil.VALIDATOR_LASTNAME);
		form.getLayout().addComponent(txtLastName);

		// Return
		return verContent;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean onAdvance() {
		form.setComponentError(null);
		
		try {
			txtLastName.validate();
		} catch (EmptyValueException e) {
			error("Bitte geben Sie einen Nachnamen an!");
			return false;
		} catch (Exception e) {
			return false;
		}
		
		try {
			txtFirstName.validate();
		} catch (EmptyValueException e) {
			error("Bitte geben Sie einen Vornamen an!");
			return false;
		} catch (Exception e) {
			return false;
		}

		parent.props.setProperty("firstName", (String) txtFirstName.getValue());
		parent.props.setProperty("lastName", (String) txtLastName.getValue());
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean onBack() {
		return false;
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