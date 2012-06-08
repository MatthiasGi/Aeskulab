package de.gianfelice.aeskulab.ui.tabs.user;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.UserContainer;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.ui.tabs.TabUser;

/**
 * This layout contains a switch to set the administrator-state of the selected
 * user.
 * 
 * @author  Matthias Gianfelice
 * @version 2.0.3
 * @see     TabUser
 */
public class AdministratorLayout extends VerticalLayout {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The only changeable component: A switch to select the admin-state. */
	private Switch swtAdmin;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout.
	 */
	public AdministratorLayout() {
		setMargin(true);
		setSpacing(true);
		setStyleName(Reindeer.LAYOUT_BLACK);
		Label lblInfoAdmin = new Label("Administration");
		lblInfoAdmin.setStyleName(Reindeer.LABEL_H2);
		addComponent(lblInfoAdmin);
		addComponent(new Label("Ein Administrator kann seinen eigenen " +
				"Administrationsstatus nicht bearbeiten. Dies soll " +
				"verhindern, dass für das System plötzlich keine " +
				"Administratoren mehr ausgewiesen sind."));
		swtAdmin = new Switch();
		swtAdmin.setImmediate(true);
		swtAdmin.setWriteThrough(true);
		addComponent(swtAdmin);
		setComponentAlignment(swtAdmin, Alignment.TOP_CENTER);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Updates the information about the current selected {@link User}.
	 * 
	 * @param item The item, that should be set as current user
	 */
	public void setItem(Item item) {
		swtAdmin.setEnabled(!UserContainer.isCurrentUser(item));
		swtAdmin.setPropertyDataSource(item.getItemProperty("admin"));
	}

}