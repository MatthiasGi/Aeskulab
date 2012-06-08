package de.gianfelice.aeskulab.system.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * The PropertyUtil is used to read global properties set in the.
 *
 * {@code aeskulab.properties} inside the {@code WebContent/WEB-INF} folder.
 * @author  Matthias Gianfelice
 * @version 2.1.5
 */
public class PropertyUtil {

	// ------------------------------ Attribute(s) -----------------------------
	/** Property for accessing the MySQL-server. */
	public static final Property MYSQL_SERVER =
			new Property("mysql.server", "localhost");
	
	/** Property for accessing the MySQL-port. */
	public static final Property MYSQL_PORT =
			new Property("mysql.port", "3306");
	
	/** Property for accessing the MySQL-database. */
	public static final Property MYSQL_DATABASE =
			new Property("mysql.database", "aeskulab");
	
	/** Property for accessing the MySQL-user. */
	public static final Property MYSQL_USER =
			new Property("mysql.user", "root");
	
	/** Property for accessing the MySQL-password. */
	public static final Property MYSQL_PASSWORD =
			new Property("mysql.password", "");
	
	/** Property for accessing the system directory. */
	public static final Property SYSTEM_DIR = new Property("system.dir", "");
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Returns the value of the given property.
	 *
	 * @param prop the prop
	 * @return      The value of the given property or the default value of this
	 * property, if the setting could not be read
	 */
	public static String getProperty(Property prop) {
		Properties props = new Properties();
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream stream = classLoader
				.getResourceAsStream("../aeskulab.properties");

		try {
			props.load(stream);
		} catch (Exception e) {
			System.err.println("Could not load database.properties, will " +
					"continue with default values!");
		}
		
		return props.getProperty(prop.getName(), prop.getDefaultValue());
	}

}