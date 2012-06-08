package de.gianfelice.aeskulab.system;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

import de.gianfelice.aeskulab.system.authentication.User;

/**
 * This class handles user sessions and is used to store the active user.
 * 
 * @author  Kim (http://code.google.com/p/vaadin-appfoundation) 
 * @author  Matthias Gianfelice
 * @version 1.1.2
 */
public class SessionHandler implements TransactionListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
    private static final long serialVersionUID = 1L;

    /** The application, that is assigned to this handler. */
    private final Application application;

    /** The active user. */
    private User user;

    /** Store the user object of the currently inlogged user. */
    private static ThreadLocal<SessionHandler> instance = 
    		new ThreadLocal<SessionHandler>();

    // ----------------------------- Constructor(s) ----------------------------
    /**
     * Creates the session handler, sets an instance and assigns the
     * application.
     * 
     * @param application The application, that should be assigned to this
     *                    handler
     */
    public SessionHandler(Application application) {
        this.application = application;
        instance.set(this);
    }

    // ------------------------------- Method(s) -------------------------------
    /**
     * {@inheritDoc}
     */
    public void transactionEnd(Application application,
    		Object transactionData) {
        if (this.application == application) instance.set(null);
    }

    /**
     * {@inheritDoc}
     */
    public void transactionStart(Application application,
    		Object transactionData) {
        if (this.application == application) instance.set(this);
    }

    /**
     * Set the User object for the currently inlogged user for this application
     * instance.
     * 
     * @param user The user, that should be set
     */
    public static void setUser(User user) {
        instance.get().user = user;
    }

    /**
     * Get the User object of the currently inlogged user for this application
     * instance.
     * 
     * @return The currently inlogged user
     */
    public static User get() {
        return instance.get().user;
    }

    /**
     * Method for logging out a user.
     */
    public static void logout() {
        setUser(null);
    }

    /**
     * Initialises the {@link SessionHandler} for the given {@link Application}.
     * 
     * @param application The application, which should be assigned to this
     *                    handler
     */
    public static void initialise(Application application) {
        if (application == null)
            throw new IllegalArgumentException("Application may not be null");
        SessionHandler handler = new SessionHandler(application);
        application.getContext().addTransactionListener(handler);
    }

}