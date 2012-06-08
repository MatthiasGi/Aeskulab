package de.gianfelice.aeskulab.system.authentication;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;

import de.gianfelice.aeskulab.system.SessionHandler;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * Utility class for authenticating, controlling and managing user-accounts.
 * 
 * @author  Kim (http://code.google.com/p/vaadin-appfoundation)
 * @author  Matthias Gianfelice
 * @version 2.4.2
 */
public class AuthenticationUtil implements Serializable {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A validator to check the email of a user. */
	public static final Validator VALIDATOR_EMAIL = new EmailValidator(
			"Bitte geben Sie eine gültige E-Mail-Adresse an!");
	
	/** A validator to check the first name of a user. */
	public static final Validator VALIDATOR_FIRSTNAME =
			new StringLengthValidator(
					"Der Name muss zwischen 3 und 25 Zeichen lang sein!",
					3, 25, false);
	
	/** A validator to check the last name of a user. */
	public static final Validator VALIDATOR_LASTNAME = VALIDATOR_FIRSTNAME;
	
	/** A validator to check the password of a user. */
	public static final Validator VALIDATOR_PASSWORD =
			new StringLengthValidator(
					"Das Passwort muss zwischen 5 und 25 Zeichen lang sein!",
					5, 25, false);
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Try to log in a user with the given user credentials.
	 *
	 * @param id the id
	 * @param password the password
	 * @return                              The object of the new, inlogged user
	 * @throws InvalidLoginDataException    The given data is wrong or invalid
	 * @throws TooManyFailedLoginsException When the password was wrong too many
	 * times, this exception will be thrown
	 */
	public static User authenticate(Long id, String password)
			throws InvalidLoginDataException, TooManyFailedLoginsException {		
		if (id == null || password == null)
			throw new InvalidLoginDataException();

		User user = getUser(id);
		if (user == null || !user.isPassword(password))
			throw new InvalidLoginDataException();

		SessionHandler.setUser(user);
		return user;
	}
	
	/**
	 * Generates a hash out of an password-string.
	 *
	 * @param text the text
	 * @return      The hash, created out of the given text
	 */
	protected static String getHash(String text) {
        StringBuffer hashedPassword = new StringBuffer();

        byte[] defaultBytes = (text + User.PASSWORD_SALT).getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                hashedPassword.append(Integer
                        .toHexString(0xFF & messageDigest[i]));
            }
        } catch (NoSuchAlgorithmException nsae) {
        	return text;
        }

        return hashedPassword.toString();
    }

	/**
	 * Gets a user by it's ID.
	 *
	 * @param id the id
	 * @return    The user with the given ID
	 */
	public static User getUser(Long id) {
		return DBManager.getCommunicator().find(User.class, id);
	}
	
	/**
	 * Gets a user by it's email.
	 *
	 * @param email the email
	 * @return       The user with the given email.
	 */
	public static User getUserByEmail(String email) {
		String query = "SELECT u FROM User u WHERE u.email = :email";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);
		return DBManager.getCommunicator().find(query, parameters);
	}

	/**
	 * Creates the first user of the system. If already a user is registered,
	 * the action will be aborted.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param email the email
	 * @param password the password
	 * @return           If there's another user registered, this method will
	 * return false and abort the operation, if the user was
	 * created, it'll return true
	 */
	public static boolean createFirstUser(String firstName, String lastName,
			String email, String password) {
		if (DBManager.getCommunicator().count(User.class) > 0) return false;
		new User(firstName, lastName, email, password, true);
		return true;
	}
	
	/**
	 * Adds a user to the application.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param email the email
	 * @param password the password
	 * @return           The newly created user
	 */
	public static User createUser(String firstName, String lastName,
			String email, String password) {
		if (!isCurrentUserAdmin()) return null;
		if (!isEmailAvailable(email)) return null;
		return new User(firstName, lastName, email, password);
	}
	
	/**
	 * Checks, whether the current inlogged user is an administrator.
	 * 
	 * @return True, if the current inlogged user is an administrator, otherwise
	 *         false
	 */
	public static boolean isCurrentUserAdmin() {
		return SessionHandler.get().isAdmin();
	}
	
	/**
	 * Checks, whether the given email is already in use.
	 *
	 * @param email the email
	 * @return       Whether the email is available
	 */
	public static boolean isEmailAvailable(String email) {
		return getUserByEmail(email) == null;
	}
	
}