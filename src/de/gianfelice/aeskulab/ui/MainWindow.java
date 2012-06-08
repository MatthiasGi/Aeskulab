package de.gianfelice.aeskulab.ui;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Window;

import de.gianfelice.aeskulab.system.SessionHandler;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.system.utils.PropertyUtil;
import de.gianfelice.aeskulab.ui.login.FirstUser;
import de.gianfelice.aeskulab.ui.login.LoginLayout;

/**
 * The main window of the whole application. It will decide, which layout (e.g.
 * login, intern) should be displayed.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
@SuppressWarnings("deprecation")
public class MainWindow extends Window {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the window and selects the layout.
	 */
	public MainWindow() {
		super("Aeskulab");
		setIcon(new ThemeResource("favicon.ico"));

		if (PropertyUtil.getProperty(PropertyUtil.SYSTEM_DIR).equals("")) {
			showNotification("Ohne, dass die Einstellungsdatei eingerichtet " +
					"ist, wird das Programm nicht lauffähig sein!<br>",
					"<br>Öffnen Sie bitte die <tt>aeskulab.properties</tt> " +
					"im Ordner <tt>WEB-INF</tt> und passen Sie die " +
					"Umgebungseinstellungen an! U.U. wird ein " +
					"Server-Neustart nötig sein, um die Änderung zu " +
					"registrieren.", Notification.TYPE_ERROR_MESSAGE, true);
		} else if (SessionHandler.get() != null) {
			setInternView();
		} else if (DBManager.getCommunicator().count(User.class) > 0) {
			setLoginView();
		} else {
			setContent(new FirstUser());
		}
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Switches to the login screen.
	 */
	public void setLoginView() {
		setContent(new LoginLayout());
	}
	
	/**
	 * Switches to the internal view.
	 */
	public void setInternView() {
		setContent(new MainLayout());
	}

}