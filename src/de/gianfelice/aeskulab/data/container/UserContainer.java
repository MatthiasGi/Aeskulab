package de.gianfelice.aeskulab.data.container;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;

import de.gianfelice.aeskulab.system.SessionHandler;
import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * This simple container organises all {@link User} objects of this project.
 * 
 * @author  Matthias Gianfelice
 * @version 2.3.4
 */
public class UserContainer extends BeanContainer<Long, User> {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** The original order of the columns. */
	public static final Object[] NATURAL_COLS = new Object[] {"lastName",
		"firstName", "email", "image", "admin", "blocked"};
	
	/** The headers of the columns. */
	public static final String[] COL_HEADERS = new String[] {"Nachname",
		"Vorname", "E-Mail-Adresse", "Bild", "Admin", "Blockiert"};
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Initialises the container and loads all users to it.
	 */
	public UserContainer() {
		super(User.class);
		setBeanIdProperty("id");
		addAll(DBManager.getCommunicator().list(User.class));
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Gets the user, that is linked to the given item.
	 *
	 * @param item the item
	 * @return       The user, that is linked to the given item
	 */
	public static User getUser(Item item) {
		Long id = (Long) item.getItemProperty("id").getValue();
		return AuthenticationUtil.getUser(id);
	}
	
	/**
	 * Checks, whether the current user equals the given item.
	 *
	 * @param item the item
	 * @return      Whether the users are equal or not
	 */
	public static boolean isCurrentUser(Item item) {
		return SessionHandler.get().equals(getUser(item));
	}
	
	/**
	 * Returns the current inlogged user as item.
	 * 
	 * @return The current user
	 */
	public Item getCurrentUser() {
		return getItem(SessionHandler.get().getId()); 
	}

}