package de.gianfelice.aeskulab.system.authentication;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.gianfelice.aeskulab.system.SessionHandler;
import de.gianfelice.aeskulab.system.persistence.PersistentObject;
import de.gianfelice.aeskulab.system.utils.FileUtil;

/**
 * Entity class for users. This class keeps information about registered users.
 * 
 * @author  Kim (http://code.google.com/p/vaadin-appfoundation)
 * @author  Matthias Gianfelice
 * @version 3.6.0
 * @see     User
 */
@Entity
@Table(
	name = "user",
	uniqueConstraints = { @UniqueConstraint(columnNames = {"email"}) }
)
public final class User extends PersistentObject {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The maximal number of allowed failed password-check-attempts. */
	public static final int MAX_FAILED_LOGINS = 5;
	
	/** A unique salt to generate the hash of a password. */
	protected static final String PASSWORD_SALT = "=H_2v=!-N_d£@er4qNh&~\\";

	/** The first name of the user. */
	private String firstName;
	
	/** The last name of the user. */
	private String lastName;
	
	/** The user's email-address, which is also used to perform a login. */
	private String email;
	
	/** Whether the user is an administrator. */ 
	private boolean admin;
	
	/** The password of the user as hash. */
	private String password;
	
	/** The number of failed password check attempts. */
	private int failedLogins;
	
	/** The name of an image of this user. */
	private String image;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * An empty constructor which is accessible by all classes in the project.
	 */
	public User() {}
	
	/**
	 * A protected constructor which is used to create new users.
	 * 
	 * @param firstName The first name of the user
	 * @param lastName  The last name of the user
	 * @param email     The email of the new user
	 * @param password  The password of the new user (the hash will be 
	 *                  automatically generated)
	 */
	protected User(String firstName, String lastName, String email,
			String password) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = AuthenticationUtil.getHash(password);
		store();
	}
	
	/**
	 * A protected constructor which is used to create the first user.
	 * 
	 * @param firstName The first name of the user
	 * @param lastName  The last name of the user
	 * @param email     The email of the new user
	 * @param password  The password of the new user (the hash will be 
	 *                  automatically generated)
	 * @param admin     Whether the new user should be an administrator or not
	 */
	protected User(String firstName, String lastName, String email,
			String password, boolean admin) {
		this(firstName, lastName, email, password);
		this.admin = admin;
		store();
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * Returns the first name of the current user.
	 * 
	 * @return The first name of the user
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the current user.
	 * 
	 * @param firstName The first name of the user
	 */
	public void setFirstName(String firstName) {
		if (firstName == null || this.firstName.equals(firstName)) return;
		this.firstName = firstName;
		store();
	}

	/**
	 * Returns the last name of the current user.
	 * 
	 * @return The last name of the user
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the current user.
	 * 
	 * @param lastName The last name of the user
	 */
	public void setLastName(String lastName) {
		if (lastName == null || this.lastName.equals(lastName)) return;
		this.lastName = lastName;
		store();
	}
	
	/**
	 * Returns the email of the current user.
	 * 
	 * @return The email of the user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the current user.
	 * 
	 * @param email The email of the user
	 */
	public void setEmail(String email) {
		this.email = email;
		store();
	}
	
	/**
	 * Returns whether the given password matches the real one. This method is
	 * only accessible by package-members to ensure cracking the password is not
	 * too easy.
	 *
	 * @param password the password
	 * @return                              Whether the password matches the
	 * saved one
	 * @throws TooManyFailedLoginsException When the password was wrong too many
	 * times, this exception will be thrown
	 * @see    #MAX_FAILED_LOGINS
	 */
	protected boolean isPassword(String password)
			throws TooManyFailedLoginsException {
		if (isBlocked()) throw new TooManyFailedLoginsException();
		String hash = AuthenticationUtil.getHash(password);

		if (this.password.equals(hash)) {
			failedLogins = 0;
			store();
			return true;
		} else {
			failedLogins ++;
			store();
			return false;
		}
	}
	
	/**
	 * Sets the password for the user.
	 *
	 * @param password the password
	 * @param oldPassword the old password
	 * @return                              Whether the inlogged user is
	 * privileged, the old password was
	 * correct and the given new password
	 * is valid
	 * @throws TooManyFailedLoginsException Will be thrown, if there were too
	 * many attempts to check the password
	 * @see    #MAX_FAILED_LOGINS
	 */
	public boolean setPassword(String password, String oldPassword)
			throws TooManyFailedLoginsException {
		if (!(isUserPrivileged() && isPassword(oldPassword)
				&& AuthenticationUtil.VALIDATOR_PASSWORD.isValid(password)))
			return false;

		this.password = AuthenticationUtil.getHash(password);
		failedLogins = 0;
		store();
		return true;
	}
	
	/**
	 * Sets the password for the user by an administrator.
	 *
	 * @param password the password
	 * @return          Whether the inlogged user is an administrator and
	 * therefore privileged to change the password without
	 * providing the old password and the given password is
	 * valid
	 */
	public boolean setPassword(String password) {
		if (!SessionHandler.get().isAdmin()
				|| !AuthenticationUtil.VALIDATOR_PASSWORD.isValid(password))
			return false;

		this.password = AuthenticationUtil.getHash(password);
		failedLogins = 0;
		store();
		return true;
	}

	/**
	 * Returns {@code true}, if the user is an administrator, otherwise it
	 * returns {@code false}.
	 * 
	 * @return Whether the user is an administrator
	 */
	public boolean isAdmin() {
		return admin;
	}
	
	/**
	 * Tries to set the administrator-flag for the user. The user can't change
	 * it's own status to prevent a situation without any administrators.
	 *
	 * @param admin the new admin
	 */
	public void setAdmin(boolean admin) {
		if (!SessionHandler.get().equals(this)) {
			this.admin = admin;
			store();
		}
	}

	/**
	 * Returns the image of the user.
	 * 
	 * @return The image of the user or null, if there is none.
	 * @see    FileUtil
	 */
	public File getImage() {
		if (image == null || image.equals("")) return null;
		
		try {
			File file = FileUtil.getFile(FileUtil.IMAGE_USER, image);
			if (!file.isFile() || !file.exists()) return null;
			return file;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Sets the image of the current user.
	 * 
	 * @param image The image of the user
	 */
	public void setImage(File image) {
		if (image == null) {
			if (this.image == null) return;
			this.image = null;
		} else {
			this.image = image.getName();
		}
		store();
	}
	
	/**
	 * Checks, whether the user is blocked because of too many login attempts.
	 * 
	 * @return True, if the user is blocked, otherwise false
	 */
	public boolean isBlocked() {
		return failedLogins >= MAX_FAILED_LOGINS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof User)) return false;
		User user = (User) obj;		
		return getEmail().equals(user.getEmail());
	}
	
	/**
	 * Checks, whether the current user is privileged to change a value in this
	 * object.
	 * 
	 * @return Whether the current inlogged user is equal to this user or the
	 *         inlogged user is an administrator 
	 */
	private boolean isUserPrivileged() {
		return SessionHandler.get().equals(this)
				|| SessionHandler.get().isAdmin();
	}

}