package de.gianfelice.aeskulab.ui.tabs.user;

import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;

import de.gianfelice.aeskulab.data.container.UserContainer;
import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.ui.components.SubmitButton;
import de.gianfelice.aeskulab.ui.tabs.Tab;
import de.gianfelice.aeskulab.ui.tabs.TabUser;

/**
 * Editing of user-properties is done with this container.
 * 
 * @author  Matthias Gianfelice
 * @version 2.2.4
 * @see     TabUser
 * @see     DataLayoutFactory
 */
@SuppressWarnings("deprecation")
public class DataLayout extends VerticalLayout implements ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The {@link Tab} to call update-events. */
	private TabUser tab;
	
	/** The password of the user. */
	private PasswordField txtPassword1;
	
	/** The password (repetition) of the user. */
	private PasswordField txtPassword2;
	
	/** A button to cancel editing the properties. */
	private Button btnCancel;
	
	/** A button to save the changes. */
	private SubmitButton btnSave;
	
	/** The current user for this layout. */
	private User user;
	
	/** A form to hold all data. */
	private Form form;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and sets the tab.
	 * 
	 * @param tab The {@link Tab}, to call update events
	 */
	public DataLayout(TabUser tab) {
		
		// Global settings
		this.tab = tab;
		setSpacing(true);
		setMargin(false, false, true, false);
		btnSave = new SubmitButton("Speichern", this);
		
		// Form
		form = new Form();
		form.setWriteThrough(false);
		form.setInvalidCommitted(false);
		form.setFormFieldFactory(new DataLayoutFactory(btnSave));
		addComponent(form);

		// Password
		addComponent(new Label("Zum Ändern des Passwortes der " +
				"entsprechenden Person bitte in den folgenden beiden Feldern " +
				"ein Neues eingeben. So lange dies nicht geschieht, wird das " +
				"Passwort nicht geändert. Beim Ändern des Passworts wird ein " +
				"Nutzer automatisch entsperrt."));
		GridLayout grid = new GridLayout(2, 1);
		grid.setSpacing(true);
		grid.setMargin(true);
		txtPassword1 = new PasswordField("Passwort");
		txtPassword1.setImmediate(true);
		txtPassword1.addListener((TextChangeListener) btnSave);
		grid.addComponent(txtPassword1);
		txtPassword2 = new PasswordField("Passwort (wiederholen)");
		txtPassword2.setImmediate(true);
		txtPassword1.addListener((TextChangeListener) btnSave);
		grid.addComponent(txtPassword2);
		addComponent(grid);
		setComponentAlignment(grid, Alignment.TOP_CENTER);

		// Save / Cancel
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		addComponent(horLayout);
		setComponentAlignment(horLayout, Alignment.TOP_RIGHT);
		btnCancel = new Button("Zurücksetzen", this);
		horLayout.addComponent(btnCancel);
		horLayout.addComponent(btnSave);

	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Updates the information about the current selected {@link User}.
	 * 
	 * @param item The item, that should be set as current user
	 */
	public void setItem(Item item) {
		this.user = UserContainer.getUser(item);
		form.setItemDataSource(item);
		form.setVisibleItemProperties(new Object[] {
				"lastName", "firstName", "email" });
		txtPassword1.setValue("");
		txtPassword1.setComponentError(null);
		txtPassword2.setValue("");
		txtPassword2.setComponentError(null);
		btnSave.setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		
		if (event.getButton() == btnSave) {

			// Get data and Validator
			String password1 = (String) txtPassword1.getValue();
			String password2 = (String) txtPassword2.getValue();
			Validator val = AuthenticationUtil.VALIDATOR_PASSWORD;
			
			// Save "normal" data
			try {
				form.commit();
			} catch (Exception e) {}
			
			// Check password
			if (val.isValid(password1) || val.isValid(password2)) {
				
				// Remove user to avoid LockException
				tab.getContainer().removeItem(user.getId());
				DBManager.getCommunicator().refresh(user);
				
				// Set password, if equal
				if (!password1.equals(password2)) {
					UserError uError = new UserError(
							"Die beiden Passwörter müssen übereinstimmen!");
					txtPassword1.setComponentError(uError);
					txtPassword2.setComponentError(uError);
					txtPassword1.focus();
				} else if (!user.setPassword(password1)) {
					getApplication().getMainWindow().showNotification(
							"Das Passwort konnte nicht geändert werden!",
							"<br />Besitzen Sie die entsprechenden Rechte?",
							Notification.TYPE_ERROR_MESSAGE, true);
					txtPassword1.focus();
				} else {
					DBManager.getCommunicator().refresh(user);
					getApplication().getMainWindow().showNotification(
						"Das Passwort wurde geändert!");
					txtPassword1.setValue("");
					txtPassword2.setValue("");
				}
				
				// Readd user
				tab.getContainer().addBean(user);
				tab.setItem(tab.getContainer().getItem(user.getId()));
				tab.reorderTable();
			}
			
			tab.activeUserUpdated();
		
		} else {
			form.discard();
			txtPassword1.setValue("");
			txtPassword1.setComponentError(null);
			txtPassword2.setValue("");
			txtPassword2.setComponentError(null);
			btnSave.setEnabled(false);
		}
		
	}

}