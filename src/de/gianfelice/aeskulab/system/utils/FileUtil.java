package de.gianfelice.aeskulab.system.utils;

import java.io.File;
import java.io.IOException;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;

import de.gianfelice.aeskulab.system.authentication.User;

/**
 * This utility is used to access files inside the system directory. It uses the
 * {@link PropertyUtil} to get the directory.
 *  
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class FileUtil {
	
	// ------------------------------ Attribute(s) -----------------------------
	/** Directory with images of the users. */
	public static final String IMAGE_USER = "img/users";
	
	/** Directory with images of the vehicles. */
	public static final String IMAGE_VEHICLE = "img/vehicles";
	
	/** Directory with tactical images. */
	public static final String IMAGE_TACTICAL = "img/tactical/";
	
	/** Directory with images of places. */
	public static final String IMAGE_PLACE = "img/places";

	// ------------------------------- Method(s) -------------------------------
	/**
	 * Returns the absolute path to the system directory.
	 * 
	 * @return             Absolute path to system directory
	 * @throws IOException The exception is thrown by the {@link PropertyUtil}
	 *                     if the Properties could not be read
	 * @see PropertyUtil#SYSTEM_DIR
	 */
	public static String getBasePath() throws IOException {
		return PropertyUtil.getProperty(PropertyUtil.SYSTEM_DIR);
	}
	
	/**
	 * Returns a file-object of a file inside the system directory out of the
	 * given path.
	 *
	 * @param file the file
	 * @return             A file-object of the given file
	 * @throws IOException The exception is thrown by the {@link PropertyUtil}
	 * if the Properties could not be read
	 * @see #getBasePath()
	 * @see PropertyUtil#SYSTEM_DIR
	 */
	public static File getFile(String file) throws IOException {
		File fileBase = new File(getBasePath());
		return new File(fileBase, file);
	}
	
	/**
	 * Returns a file-object of a file inside the path directory, witch is
	 * inside the system directory out of the given path.
	 *
	 * @param path the path
	 * @param file the file
	 * @return             A file-object out of the given parameters
	 * @throws IOException If the file could not be read, this exception will be
	 * thrown
	 */
	public static File getFile(String path, String file) throws IOException {
		File fileBase = new File(getBasePath());
		File filePath = new File(fileBase, path);
		if (!filePath.exists()) filePath.mkdir();
		return new File(filePath, file);
	}
	
	/**
	 * Returns a resource to display the given user's image. If no image was
	 * found, a placeholder will be loaded.
	 *
	 * @param user the user
	 * @param application the application
	 * @return             A resource to the image of the given user
	 */
	public static Resource getUserImage(User user, Application application) {
		File image = user.getImage();
		if (image == null) {
			return new ClassResource("res/150/transformer.png", application);
		} else {
			return new FileResource(image, application);
		}
	}

}