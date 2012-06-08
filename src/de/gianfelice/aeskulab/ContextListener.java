package de.gianfelice.aeskulab;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * This listener is called on server-startup and initialises the
 * {@link DBManager}.
 *  
 * @author  Matthias Gianfelice
 * @version 3.0.2
 */
public class ContextListener implements ServletContextListener {

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public void contextDestroyed(ServletContextEvent arg0) {}

	/**
	 * {@inheritDoc}
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		
		try {
			DBManager.registerCommunicator("mysql", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}