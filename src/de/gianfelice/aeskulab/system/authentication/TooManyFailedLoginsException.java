package de.gianfelice.aeskulab.system.authentication;


/**
 * This Exception is thrown, if the password of the user was checked too many
 * times before.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.1
 * @see     User#MAX_FAILED_LOGINS
 */
public class TooManyFailedLoginsException extends Exception {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

}