package de.gianfelice.aeskulab.ui.components;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.vaadin.data.Property;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
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
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.system.utils.FileUtil;
import de.gianfelice.aeskulab.system.utils.ImageScaler;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow.ButtonOkListener;

/**
 * A component to manage images, that are linked to {@link Property} objects.
 * This layout offers upload, display and removing of images.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class ImageManager extends CustomComponent implements ClickListener,
		ButtonOkListener, FailedListener, FinishedListener, ProgressListener,
		StartedListener, SucceededListener, Receiver {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** Base path. */
	private String path;

	/** Linked property. */
	private Property property;

	/** Embedded to display image. */
	private Embedded emb;

	/** Progress for upload. */
	private ProgressIndicator progress;

	/** Button to cancel upload. */
	private Button btnCancel;

	/** Upload object. */
	private Upload upload;
	
	/** Button to delete image. */
	private Button btnDelete;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and sets the base path.
	 * 
	 * @param path Base path, where the images should be saved
	 */
	public ImageManager(String path) {
		
		// Initialisation
		this.path = path;
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		setCompositionRoot(verLayout);
		setWidth("150px");
		setEnabled(false);
		
		// Image
		emb = new Embedded();
		emb.setWidth("150px");
		verLayout.addComponent(emb);
		
		// Progress and Cancel
		progress = new ProgressIndicator();
		progress.setWidth("150px");
		progress.setPollingInterval(500);
		progress.setVisible(false);
		verLayout.addComponent(progress);
		btnCancel = new Button("Abbrechen");
		btnCancel.setStyleName(Reindeer.BUTTON_SMALL);
		btnCancel.addListener(this);
		btnCancel.setVisible(false);
		verLayout.addComponent(btnCancel);
		verLayout.setComponentAlignment(btnCancel, Alignment.TOP_CENTER);
		
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
		verLayout.addComponent(upload);
		verLayout.setComponentAlignment(upload, Alignment.TOP_CENTER);
		
		// Delete
		btnDelete = new Button("Löschen");
		btnDelete.setStyleName(Reindeer.BUTTON_SMALL);
		btnDelete.addListener(this);
		btnDelete.setWidth("125px");
		btnDelete.setVisible(false);
		verLayout.addComponent(btnDelete);
		verLayout.setComponentAlignment(btnDelete, Alignment.TOP_CENTER);

	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		if (emb.getSource() == null) emb.setSource(getNullImage());
	}
	
	/**
	 * The image, that should be displayed, if the real one could not be found.
	 * 
	 * @return A resource with a placeholder image
	 */
	protected Resource getNullImage() {
		return new ClassResource("res/150/transformer.png", getApplication());
	}
	
	/**
	 * Sets the property, that should be linked with this layout.
	 * 
	 * @param property The new property
	 */
	public void setPropertyDataSource(Property property) {
		if (property == null) {
			upload.setVisible(true);
			btnDelete.setVisible(false);
			progress.setVisible(false);
			btnCancel.setVisible(false);
			setEnabled(false);
			this.property = null;
			return;
		}
		
		Object val = property.getValue();
		if (!(val == null || val instanceof File)) return;
		this.property = property;
		update();
	}
	
	/**
	 * Updates the layout.
	 */
	private void update() {
		Object val = property.getValue();
		setEnabled(true);
		upload.setVisible(true);
		btnDelete.setVisible(val != null);
		progress.setVisible(false);
		btnCancel.setVisible(false);
		if (val instanceof File && val != null) {
			emb.setSource(new FileResource((File) val, getApplication()));
		} else {
			emb.setSource(getNullImage());
		}
		
	}
	
	/**
	 * Deletes the image.
	 * 
	 * @return Whether the action was successful
	 */
	private boolean deleteImage() {
		Object val = property.getValue();
		if (!(val instanceof File)) return true;
		File file = (File) val;
		if (!file.delete()) return false;
		onPropertyUpdate(null);
		update();
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public OutputStream receiveUpload(String filename, String mimeType) {
		try {
			String ext = filename.substring(filename.lastIndexOf('.'));
			File fileBase = FileUtil.getFile(path);
			File file = File.createTempFile("img", ext, fileBase);
			onPropertyUpdate(file);
			return new FileOutputStream(file);
		} catch (IOException e) {
			getApplication().getMainWindow().showNotification("Leider hat " +
					"die Anwendung keinen Zugriff auf das Arbeitsverzeichnis!",
					Notification.TYPE_ERROR_MESSAGE);
			upload.interruptUpload();
			return null;
		}
	}
	
	/**
	 * Will be called, when the file changes.
	 * 
	 * @param newValue The new file
	 */
	protected void onPropertyUpdate(File newValue) {
		property.setValue(newValue);
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadSucceeded(SucceededEvent event) {
		File file = (File) property.getValue();
		Dimension dim = new Dimension(150, 500);
		try {
			BufferedImage buffImage = ImageIO.read(file);
			BufferedImage buffImageNew = ImageScaler.scaleImage(buffImage, dim);
			ImageIO.write(buffImageNew, ImageScaler.getFormatName(file), file);
		} catch (IOException e) {
			getApplication().getMainWindow().showNotification(
					"Das Bild konnte nicht skaliert werden!",
					Notification.TYPE_ERROR_MESSAGE);
		}
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
		progress.setValue(0f);
		progress.setVisible(true);
		btnCancel.setVisible(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateProgress(long readBytes, long contentLength) {
		progress.setValue(new Float(readBytes / (float) contentLength));
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadFinished(FinishedEvent event) {
		update();
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadFailed(FailedEvent event) {
		Window win = getApplication().getMainWindow();
		if (deleteImage()) {
			win.showNotification("Der Upload wurde abgebrochen!",
					Notification.TYPE_WARNING_MESSAGE);
		} else {
			win.showNotification("Der Upload wurde abgebrochen!", "<br /> " +
					"Jedoch konnte das bereits hochgeladene Fragment nicht " +
					"gelöscht werden. Dies sollte nun manuell getan werden!",
					Notification.TYPE_ERROR_MESSAGE, true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean okButtonClicked() {
		if (!deleteImage())
			getApplication().getMainWindow().showNotification(
					"Leider konnte das Bild nicht gelöscht werden! Sind die " +
					"Rechte richtig gesetzt?", Notification.TYPE_ERROR_MESSAGE);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		Button btn = event.getButton();
		if (btn == btnDelete) {
			getApplication().getMainWindow().addWindow(new ConfirmationWindow(
					"Wirklich löschen?",
					"Soll das aktuelle Bild wirklich gelöscht werden?", this));
		} else if (btn == btnCancel) {
			upload.interruptUpload();
		}
	}

}