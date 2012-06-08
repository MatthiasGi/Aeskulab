package de.gianfelice.aeskulab.ui.login;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.Aeskulab;
import de.gianfelice.aeskulab.data.container.UserContainer;
import de.gianfelice.aeskulab.data.table.AdminFilter;
import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;
import de.gianfelice.aeskulab.system.authentication.InvalidLoginDataException;
import de.gianfelice.aeskulab.system.authentication.TooManyFailedLoginsException;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.ui.MainWindow;

/**
 * This layout provides the login mask of the application. After the user is
 * logged in, it will call a method on the {@link MainWindow} to load the
 * internal view.
 * 
 * @author  Matthias Gianfelice
 * @version 2.0.2
 */
public class LoginLayout extends VerticalLayout implements ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A field for the email. */
	private ComboBox comboEmail;
	
	/** A field for the password. */
	private PasswordField txtPassword;
	
	/** A label with errors. */
	private Label lblError;
	
	/** A login-button. */
	private Button btnLogin;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * This will create the login window.
	 */
	public LoginLayout() {
		super();
		setMargin(true);
		setSizeFull();	
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	// Content panel
	public void attach() {
		Panel panelLogin = new Panel();
		panelLogin.setSizeUndefined();
		panelLogin.setStyleName("a-panel-login");
		addComponent(panelLogin);
		setComponentAlignment(panelLogin, Alignment.MIDDLE_CENTER);

		// Component layout
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		verLayout.setMargin(true);
		panelLogin.setContent(verLayout);

		// Image
		Embedded embImage = new Embedded();
		embImage.setSource(new ClassResource("res/150/AeskulabBlack.png",
				getApplication()));
		embImage.setHeight("150px");
		embImage.setWidth("150px");
		verLayout.addComponent(embImage);
		verLayout.setComponentAlignment(embImage, Alignment.TOP_CENTER);

		// Email
		UserContainer data = new UserContainer();
		data.addContainerFilter(new AdminFilter());
		comboEmail = new ComboBox();
		comboEmail.setWidth("250px");
		comboEmail.setContainerDataSource(data);
		comboEmail.setItemCaptionMode(
				AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
		comboEmail.setItemCaptionPropertyId("email");
		comboEmail.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		comboEmail.setInputPrompt("E-Mail-Adresse");
		comboEmail.setNullSelectionAllowed(false);
		verLayout.addComponent(comboEmail);
			
		// Password
		txtPassword = new PasswordField();
		txtPassword.setInputPrompt("Passwort");
		txtPassword.setWidth("250px");
		verLayout.addComponent(txtPassword);
		
		// Button
		btnLogin = new Button("Einloggen");
		btnLogin.setWidth("250px");
		btnLogin.setDisableOnClick(true);
		btnLogin.setStyleName(Reindeer.BUTTON_DEFAULT);
		btnLogin.setClickShortcut(KeyCode.ENTER);
		btnLogin.addListener(this);
		verLayout.addComponent(btnLogin);
		
		// Error-Label
		lblError = new Label();
		lblError.setWidth("250px");
		verLayout.addComponent(lblError);
		
		// Settings
		setExpandRatio(panelLogin, 1.0f);
		addComponent(new Label("Version " + Aeskulab.VERSION));
		reset();
	}

	/**
	 * Resets the form.
	 */
	private void reset() {
		comboEmail.setValue("");
		comboEmail.focus();
		txtPassword.setValue("");
		btnLogin.setEnabled(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		Long id = (Long) comboEmail.getValue();
		String password = (String) txtPassword.getValue();
		
		try {
			User user = AuthenticationUtil.authenticate(id, password);
			
			if (user == null) {
				error("Der Benutzer konnte aufgrund eines technischen " +
						"Defekts nicht authentifiziert werden! Bitte wenden " +
						"Sie sich an den Programmierer.");
			} else if (!user.isAdmin()) {
				error("Leider ist die Desktop-Version nur der Einsatzleitung " +
						"zugänglich!");
			} else {
				((MainWindow) getApplication().getMainWindow()).setInternView();
			}
			
		} catch (InvalidLoginDataException e) {
			error("Die angegeben Daten sind nicht hinterlegt! Wenn Sie Ihr " +
					"Passwort vergessen haben, wenden Sie sich bitte an " +
					"einen Administrator.");
		} catch (TooManyFailedLoginsException e) {
			error("Der Benutzer ist aufgrund zu vieler Login-Versuche " +
					"geblockt. Ein Administrator kann ihn durch " +
					"Passwortänderung wieder entsperren.");
		}
	}
	
	/**
	 * Displays an error-message.
	 * 
	 * @param msg A message
	 */
	private void error(String msg) {
		lblError.setValue(msg);
		reset();
	}

}