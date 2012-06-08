package de.gianfelice.aeskulab.ui.tabs.mainmenu;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

import de.gianfelice.aeskulab.ui.tabs.TabMainMenu;
import de.gianfelice.aeskulab.ui.tabs.mainmenu.ListButton.ButtonClickedListener;

/**
 * This layout contains global controls for the application that are displayed
 * on the main page.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     TabMainMenu
 */

@SuppressWarnings("deprecation")
public class GlobalLayout extends VerticalLayout
		implements ButtonClickedListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** Settings. */
	private ListButton btnSettings;
	
	/** Help. */
	private ListButton btnHelp;
	
	/** About the application. */
	private ListButton btnAbout;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and adds the buttons.
	 */
	public GlobalLayout() {
		setSizeFull();		
		btnSettings = new ListButton("Einstellungen", this);
		addComponent(btnSettings);
		btnHelp = new ListButton("Hilfe", this);
		addComponent(btnHelp);
		btnAbout = new ListButton("Über Aeskulab", this);
		addComponent(btnAbout);
		setEnabled(false);
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		Application app = getApplication();
		btnSettings.setSource(new ClassResource("res/30/settings.png", app));
		btnHelp.setSource(new ClassResource("res/30/help.png", app));
		btnAbout.setSource(new ClassResource("res/30/about.png", app));
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClicked(ListButton button) {
		getApplication().getMainWindow().showNotification(
				"Die Funktion ist leider (noch) nicht integriert!",
				Notification.TYPE_WARNING_MESSAGE);
	}

}