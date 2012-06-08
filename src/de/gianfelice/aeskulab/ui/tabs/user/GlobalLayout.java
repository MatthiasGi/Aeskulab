package de.gianfelice.aeskulab.ui.tabs.user;

import java.io.File;

import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.UserContainer;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow.ButtonOkListener;
import de.gianfelice.aeskulab.ui.tabs.Tab;
import de.gianfelice.aeskulab.ui.tabs.TabUser;

/**
 * This layout contains buttons to add or remove a user.
 * 
 * @author  Matthias Gianfelice
 * @version 2.0.0
 * @see     TabUser
 */
public class GlobalLayout extends HorizontalLayout implements ClickListener,
		ButtonOkListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The {@link Tab} to call update-events. */
	private TabUser tab;
	
	/** Button to add users. */
	private Button btnAdd;
	
	/** Button to remove users. */
	private Button btnDelete;
	
	/** The current activated item. */
	private Item item;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and sets the tab.
	 * 
	 * @param tab The {@link Tab}, to call update events
	 */
	public GlobalLayout(TabUser tab) {
		this.tab = tab;
		setWidth("100%");
		setMargin(true);
		setStyleName(Reindeer.LAYOUT_BLUE);
		
		btnAdd = new Button("Neuer Benutzer");
		btnAdd.addListener(this);
		addComponent(btnAdd);
		
		btnDelete = new Button("Benutzer löschen");
		btnDelete.setStyleName(Reindeer.BUTTON_SMALL);
		btnDelete.addListener(this);
		addComponent(btnDelete);
		setComponentAlignment(btnDelete, Alignment.MIDDLE_RIGHT);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Updates the information about the current selected {@link User}.
	 * 
	 * @param item The item, that should be set as current user
	 */
	public void setItem(Item item) {
		this.item = item;
		btnDelete.setEnabled(!UserContainer.isCurrentUser(item));
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {		
		if (event.getButton() == btnAdd) {
			getApplication().getMainWindow().addWindow(new NewUserWindow(tab));		
		} else if (event.getButton() == btnDelete) {
			if (UserContainer.isCurrentUser(item)) return;
			String firstName = (String)
					item.getItemProperty("firstName").getValue();
			String lastName = (String)
					item.getItemProperty("lastName").getValue();
			ConfirmationWindow confirmation = new ConfirmationWindow(
					"Wirklich löschen?", "Soll " + firstName + " " + lastName +
							" wirklich unwiderruflich gelöscht werden?", this);
			getApplication().getMainWindow().addWindow(confirmation);
		}	
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean okButtonClicked() {
		if (UserContainer.isCurrentUser(item)) return true;
		User user = UserContainer.getUser(item);
		tab.getContainer().removeItem(user.getId());
		File fileImg = user.getImage();
		if (fileImg != null && !fileImg.delete()) {
			getApplication().getMainWindow().showNotification("Das Bild des " +
					"Benutzers konnte leider nicht gelöscht werden!",
					Notification.TYPE_ERROR_MESSAGE);
		}
		DBManager.getCommunicator().delete(user);
		tab.setItem(tab.getContainer().getCurrentUser());
		return true;
	}

}