package de.gianfelice.aeskulab;

import com.vaadin.Application;

import de.gianfelice.aeskulab.system.SessionHandler;
import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;
import de.gianfelice.aeskulab.ui.MainWindow;

/**
 * TODO: Description.
 *
 * @author  Matthias Gianfelice
 * @version 0.4.0-SNAPSHOT
 */
public class Aeskulab extends Application {
	
	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The current version of the application. */
	public static final String VERSION = "0.4.0-SNAPSHOT";

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		SessionHandler.initialise(this);
		setTheme("aeskulab");
		
		// TODO: Temporary
		SessionHandler.setUser(AuthenticationUtil.getUserByEmail("matthias@gianfelice.de"));

		MainWindow mainWindow = new MainWindow();
		mainWindow.executeJavaScript(
				"window.oncontextmenu=function(){return false;}");
		setMainWindow(mainWindow);
	}

}