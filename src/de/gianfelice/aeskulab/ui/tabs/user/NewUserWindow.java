package de.gianfelice.aeskulab.ui.tabs.user;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.ui.tabs.Tab;
import de.gianfelice.aeskulab.ui.tabs.TabUser;

/**
 * Creates a window witch is used to create new users.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.1
 * @see     GlobalLayout
 */
public class NewUserWindow extends Window implements ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The first name of the new user. */
	private TextField txtFirstName;
	
	/** The last name of the new user. */
	private TextField txtLastName;
	
	/** The email-address of the new user. */
	private TextField txtEmail;
	
	/** A button to cancel the operation. */
	private Button btnCancel;
	
	/** A button to save the new user. */
	private Button btnSubmit;
	
	/** The password of the new user. */
	private PasswordField txtPassword1;
	
	/** A repetition of the password of the new user. */
	private PasswordField txtPassword2;
	
	/** A form to hold all data. */
	private Form form;
	
	/** The {@link Tab} to call update-events. */
	private TabUser tab;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the window and sets the tab.
	 * 
	 * @param tab The {@link Tab}, to call update events
	 */
	public NewUserWindow(TabUser tab) {
		
		// General settings
		super("Neuen Benutzer anlegen");
		this.tab = tab;
		setModal(true);
		setWidth("600px");
		
		// Panel
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		setContent(panel);
		
		// Layout
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		verLayout.setSizeFull();
		panel.setContent(verLayout);
		
		// Form
		verLayout.addComponent(new Label("Bitte geben Sie im Folgenden die " +
				"nötigsten Daten an, damit der neue Nutzer angelegt werden " +
				"kann:"));
		form = new Form();
		verLayout.addComponent(form);
		
		// Textfields
		txtFirstName = new TextField("Vorname");
		txtFirstName.setWidth("100%");
		txtFirstName.setRequired(true);
		txtFirstName.addValidator(AuthenticationUtil.VALIDATOR_FIRSTNAME);
		txtFirstName.focus();
		form.getLayout().addComponent(txtFirstName);
		
		txtLastName = new TextField("Nachname");
		txtLastName.setWidth("100%");
		txtLastName.addValidator(AuthenticationUtil.VALIDATOR_LASTNAME);
		txtLastName.setRequired(true);
		form.getLayout().addComponent(txtLastName);
		
		txtEmail = new TextField("E-Mail-Adresse");
		txtEmail.setWidth("100%");
		txtEmail.addValidator(AuthenticationUtil.VALIDATOR_EMAIL);
		txtEmail.setRequired(true);
		form.getLayout().addComponent(txtEmail);
		
		txtPassword1 = new PasswordField("Passwort");
		txtPassword1.setWidth("100%");
		txtPassword1.addValidator(AuthenticationUtil.VALIDATOR_PASSWORD);
		txtPassword1.setRequired(true);
		form.getLayout().addComponent(txtPassword1);
		
		txtPassword2 = new PasswordField("Passwort (wiederholen)");
		txtPassword2.setWidth("100%");
		txtPassword2.addValidator(AuthenticationUtil.VALIDATOR_PASSWORD);
		txtPassword2.setRequired(true);
		form.getLayout().addComponent(txtPassword2);
		
		// Layout with buttons
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.setWidth("100%");
		form.setFooter(horLayout);
		
		// Abort operation
		btnCancel = new Button("Abbrechen");
		btnCancel.setStyleName(Reindeer.BUTTON_LINK);
		btnCancel.addListener(this);
		horLayout.addComponent(btnCancel);
		horLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_RIGHT);
		horLayout.setExpandRatio(btnCancel, 1.0f);
		
		// Save user
		btnSubmit = new Button("Speichern");
		btnSubmit.setStyleName(Reindeer.BUTTON_DEFAULT);
		btnSubmit.setClickShortcut(KeyCode.ENTER);
		btnSubmit.addListener(this);
		horLayout.addComponent(btnSubmit);
		horLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_RIGHT);

	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		
		if (event.getButton() == btnCancel) {
			close();
		} else if (event.getButton() == btnSubmit) {
			
			// Validation
			try {
				txtFirstName.validate();
			} catch (Exception e) {
				error("Bitte geben Sie einen gültigen Vornamen an!");
				return;
			}
			try {
				txtLastName.validate();
			} catch (Exception e) {
				error("Bitte geben Sie einen gültigen Nachnamen an!");
				return;
			}
			try {
				txtEmail.validate();
			} catch (Exception e) {
				error("Bitte geben Sie eine gültige E-Mail-Adresse an!");
				return;
			}
			try {
				txtPassword1.validate();
				txtPassword2.validate();
			} catch (Exception e) {
				error("Bitte geben Sie ein gültiges Passwort zweimal an!");
				return;
			}
			
			// Collect data
			String firstName = (String) txtFirstName.getValue();
			String lastName = (String) txtLastName.getValue();
			String email = (String) txtEmail.getValue();
			String password1 = (String) txtPassword1.getValue();
			String password2 = (String) txtPassword2.getValue();
			
			// Passwords equal?
			if (!password1.equals(password2)) {
				error("Die beiden Passwörter stimmen nicht überein!");
				return;
			}
			
			// Register user
			if (!AuthenticationUtil.isEmailAvailable(email)) {
				error("Die angegebene E-Mail-Adresse ist bereits vergeben!");
				return;
			}
			User user = AuthenticationUtil.createUser(
					firstName, lastName, email, password1);
			if (user == null) {
				error("Der Benutzer konnte nicht erstellt werden: Wurden die " +
						"erforderlichen Rechte entzogen?");
				return;
			}
			tab.getContainer().addBean(user);
			tab.reorderTable();
			close();

		}

	}
	
	/**
	 * Displays an error message.
	 * 
	 * @param msg The message
	 */
	private void error(String msg) {
		form.setComponentError(new UserError(msg));
	}
	
}