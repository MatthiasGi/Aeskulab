package de.gianfelice.aeskulab.ui.tabs.user;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.UserContainer;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.system.utils.FileUtil;
import de.gianfelice.aeskulab.system.utils.ImageScaler;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow.ButtonOkListener;
import de.gianfelice.aeskulab.ui.tabs.Tab;
import de.gianfelice.aeskulab.ui.tabs.TabUser;

/**
 * This layout organises the image of the user. It offers an upload
 * functionality and is able to delete images.
 * 
 * @author  Matthias Gianfelice
 * @version 2.1.1
 * @see     TabUser
 */
public class ImageLayout extends VerticalLayout implements ClickListener,
		ButtonOkListener, FailedListener, FinishedListener, ProgressListener,
		StartedListener, SucceededListener, Receiver {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The image of the user. */
	private Embedded embImage;
	
	/** A progressbar to display the upload-state. */
	private ProgressIndicator progressUpload;
	
	/** A button to cancel the upload-progress. */
	private Button btnCancel;
	
	/** An upload-element. */
	private Upload upload;
	
	/** A button to delete the current image of the user. */
	private Button btnDelete;
	
	/** The {@link Tab} to call update-events. */
	private TabUser tab;
	
	/** Label with the first name of the user. */
	private Label lblFirstName;
	
	/** Label with the last name of the user. */
	private Label lblLastName;
	
	/** Current item. */
	private Item item;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and sets the tab.
	 * 
	 * @param tab The {@link Tab}, to call update events
	 */
	public ImageLayout(TabUser tab) {
		
		// Initialisation
		this.tab = tab;
		setSpacing(true);
		setStyleName(Reindeer.LAYOUT_BLACK);
		setMargin(true);
		
		// Username
		HorizontalLayout horName = new HorizontalLayout();
		addComponent(horName);
		setComponentAlignment(horName, Alignment.TOP_CENTER);
		horName.setSpacing(true);
		lblFirstName = new Label();
		lblFirstName.setStyleName(Reindeer.LABEL_H1);
		lblFirstName.setSizeUndefined();
		horName.addComponent(lblFirstName);
		lblLastName = new Label();
		lblLastName.setStyleName(Reindeer.LABEL_H1);
		lblLastName.setSizeUndefined();
		horName.addComponent(lblLastName);
		
		// Image of the user
		embImage = new Embedded();
		embImage.setWidth("150px");
		addComponent(embImage);
		setComponentAlignment(embImage, Alignment.TOP_CENTER);
		
		// Progress and Cancelbutton
		progressUpload = new ProgressIndicator();
		progressUpload.setWidth("150px");
		progressUpload.setPollingInterval(500);
		addComponent(progressUpload);
		setComponentAlignment(progressUpload, Alignment.TOP_CENTER);
		btnCancel = new Button("Abbrechen");
		btnCancel.setStyleName(Reindeer.BUTTON_SMALL);
		btnCancel.addListener(this);
		addComponent(btnCancel);
		setComponentAlignment(btnCancel, Alignment.TOP_CENTER);
		
		// Upload-object
		upload = new Upload();
		upload.setImmediate(true);
		upload.setButtonCaption("Ersetzen");
		upload.setWidth("125px");
		upload.setReceiver(this);
		upload.addListener((StartedListener) this);
		upload.addListener((ProgressListener) this);
		upload.addListener((FinishedListener) this);
		upload.addListener((FailedListener) this);
		upload.addListener((SucceededListener) this);
		addComponent(upload);
		setComponentAlignment(upload, Alignment.TOP_CENTER);
		
		// Button to delete image
		btnDelete = new Button("Löschen");
		btnDelete.setStyleName(Reindeer.BUTTON_SMALL);
		btnDelete.addListener(this);
		btnDelete.setWidth("125px");
		addComponent(btnDelete);
		setComponentAlignment(btnDelete, Alignment.TOP_CENTER);

	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Updates the image and buttons on this layout.
	 */
	private void updateImage() {
		User user = UserContainer.getUser(item);
		embImage.setSource(FileUtil.getUserImage(user, getApplication()));
		btnDelete.setVisible(user.getImage() != null);
		btnCancel.setVisible(false);
		progressUpload.setVisible(false);
		upload.setVisible(true);
	}

	/**
	 * Updates the information about the current selected {@link User}.
	 * 
	 * @param item The item, that should be set as current user
	 */
	public void setItem(Item item) {
		this.item = item;
		lblFirstName.setPropertyDataSource(item.getItemProperty("firstName"));
		lblLastName.setPropertyDataSource(item.getItemProperty("lastName"));
		updateImage();
	}

	/**
	 * Tries to delete the image of a user.
	 * 
	 * @return Whether the image could be deleted
	 */
	private boolean deleteImage() {
		Property prop = item.getItemProperty("image");
		File file = (File) prop.getValue();
		if (file == null) return true;
		if (!file.delete()) return false;
		prop.setValue(null);
		updateImage();
		tab.activeUserUpdated();
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnDelete) {
			getApplication().getMainWindow().addWindow(
					new ConfirmationWindow("Wirklich löschen?",
					"Soll das Bild wirklich gelöscht werden?", this));
		} else if (event.getButton() == btnCancel) {
			upload.interruptUpload();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean okButtonClicked() {
		if (!deleteImage()) {
			getApplication().getMainWindow().showNotification(
				"Leider konnte das Bild nicht gelöscht werden! Sind die " +
				"Rechte richtig gesetzt?", Notification.TYPE_ERROR_MESSAGE);
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void uploadStarted(StartedEvent event) {
		if (!deleteImage()) {
			getApplication().getMainWindow().showNotification("Das alte Bild " +
					"des Nutzers konnte leider nicht gelöscht werden!",
					Notification.TYPE_ERROR_MESSAGE);
			return;
		}

		upload.setVisible(false);
		btnDelete.setVisible(false);
		progressUpload.setValue(0f);
		progressUpload.setVisible(true);
		btnCancel.setVisible(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadFinished(FinishedEvent event) {
		updateImage();
		tab.activeUserUpdated();
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadFailed(FailedEvent event) {
		Window win = getApplication().getMainWindow();
		if(deleteImage()) {
			win.showNotification("Der Upload wurde abgebrochen!",
					Notification.TYPE_WARNING_MESSAGE);
		} else {
			win.showNotification("Der Upload wurde abgebrochen!",
					"<br />Jedoch konnte das bereits hochgeladene Fragment " +
					"nicht gelöscht werden. Dies sollte nun manuell getan " +
					"werden!", Notification.TYPE_ERROR_MESSAGE, true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateProgress(long readBytes, long contentLength) {
		progressUpload.setValue(new Float(readBytes / (float) contentLength));
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadSucceeded(SucceededEvent event) {
		File file = (File) item.getItemProperty("image").getValue();
		Dimension dim = new Dimension(150, 500);
		try {
			BufferedImage buffImage = ImageIO.read(file);
			BufferedImage buffImageNew = ImageScaler.scaleImage(buffImage, dim);
			ImageIO.write(buffImageNew, ImageScaler.getFormatName(file), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public OutputStream receiveUpload(String filename, String mimeType) {
		try {
			String ext = filename.substring(filename.lastIndexOf('.'));
			File fileBase = FileUtil.getFile(FileUtil.IMAGE_USER);
			File file = File.createTempFile("img", ext, fileBase);
			item.getItemProperty("image").setValue(file);
			return new FileOutputStream(file);
		} catch (IOException e) {
			getApplication().getMainWindow().showNotification("Leider hat " +
					"die Anwendung keinen Zugriff auf das Arbeitsverzeichnis!",
					Notification.TYPE_ERROR_MESSAGE);
			upload.interruptUpload();
			return null;
		}

	}

}