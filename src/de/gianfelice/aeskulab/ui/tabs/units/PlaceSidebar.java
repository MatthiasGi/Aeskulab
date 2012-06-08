package de.gianfelice.aeskulab.ui.tabs.units;

import java.io.File;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.PlaceContainer;
import de.gianfelice.aeskulab.data.entities.Place;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.system.utils.FileUtil;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow;
import de.gianfelice.aeskulab.ui.components.ImageManager;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow.ButtonOkListener;

/**
 * Manages and creates new places.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class PlaceSidebar extends VerticalLayout implements ClickListener, ButtonOkListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** An image of the place. */
	private ImageManager image;
	
	/** A container with all places. */
	private PlaceContainer container;
	
	/** A label for the name. */
	private Label lblName;
	
	/** A textfield for the name. */
	private TextField txtName;
	
	/** A button to cancel editing. */
	private Button btnCancel;
	
	/** A button to save changes. */
	private Button btnSave;
	
	/** A button to delete a place. */
	private Button btnDelete;
	
	/** The current place. */
	private BeanItem<Place> item = null;
	
	/** A form to hold all fields. */
	private Form form;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the sidebar and sets the container.
	 * 
	 * @param container The container
	 */
	public PlaceSidebar(PlaceContainer container) {
		
		// Global options
		setMargin(true);
		setSpacing(true);
		this.container = container;
		
		// Image
		VerticalLayout verImage = new VerticalLayout();
		verImage.setWidth("100%");
		verImage.setMargin(true);
		verImage.setSpacing(true);
		verImage.setStyleName(Reindeer.LAYOUT_BLACK);
		addComponent(verImage);
		lblName = new Label();
		lblName.setStyleName(Reindeer.LABEL_H1);
		lblName.setSizeUndefined();
		verImage.addComponent(lblName);
		verImage.setComponentAlignment(lblName, Alignment.TOP_CENTER);
		image = new ImageManager(FileUtil.IMAGE_PLACE);
		verImage.addComponent(image);
		verImage.setComponentAlignment(image, Alignment.TOP_CENTER);
		
		// Name
		form = new Form();
		addComponent(form);
		txtName = new TextField("Name");
		txtName.setWidth("100%");
		txtName.setRequired(true);
		txtName.setRequiredError("Bitte geben Sie einen Namen an!");
		txtName.addValidator(new StringLengthValidator(
				"Der Name muss zwischen 3 und 50 Zeichen lang sein!",
				3, 50, false));
		txtName.setWriteThrough(false);
		txtName.setInvalidCommitted(false);
		form.getLayout().addComponent(txtName);
		
		// Buttons
		HorizontalLayout horButtons = new HorizontalLayout();
		horButtons.setSpacing(true);
		horButtons.setWidth("100%");
		addComponent(horButtons);
		
		// Delete
		btnDelete = new Button("Löschen");
		btnDelete.setStyleName(Reindeer.BUTTON_SMALL);
		btnDelete.addListener(this);
		btnDelete.setEnabled(false);
		horButtons.addComponent(btnDelete);
		horButtons.setComponentAlignment(btnDelete, Alignment.MIDDLE_LEFT);
		horButtons.setExpandRatio(btnDelete, 1.0f);
		
		// Cancel
		btnCancel = new Button("Abbrechen");
		btnCancel.setStyleName(Reindeer.BUTTON_LINK);
		btnCancel.addListener(this);
		horButtons.addComponent(btnCancel);
		horButtons.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		
		// Save
		btnSave = new Button("Speichern");
		btnSave.addListener(this);
		horButtons.addComponent(btnSave);

	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		form.setComponentError(null);
		
		Button btn = event.getButton();
		if (btn == btnDelete) {
			String name = (String) item.getItemProperty("name").getValue();
			ConfirmationWindow confirmation = new ConfirmationWindow(
					"Wirklich löschen?", "Soll der Platz " + name +
					" wirklich aufgelöst werden?", this);
			getApplication().getMainWindow().addWindow(confirmation);
		} else if (btn == btnCancel) {
			reset();
		} else if (btn == btnSave) {
			try {
				txtName.validate();
			} catch (Exception e) {
				form.setComponentError(new UserError(e.getMessage()));
				return;
			}
			
			if (item == null) {
				Place place = new Place((String) txtName.getValue());
				container.addBean(place);
				setItem(container.getItem(place.getId()));
			} else {
				txtName.commit();
			}
		}
	}
	
	/**
	 * Resets the form.
	 */
	private void reset() {
		if (item == null) {
			txtName.setValue("");
			image.setEnabled(false);
		} else {
			txtName.discard();
			image.setEnabled(true);
		}
	}
	
	/**
	 * Sets a new item.
	 * 
	 * @param item The new item
	 */
	public void setItem(BeanItem<Place> item) {
		this.item = item;
		reset();
		if (item == null) {
			lblName.setPropertyDataSource(
					new ObjectProperty<String>("", String.class));
			txtName.setPropertyDataSource(null);
			image.setPropertyDataSource(null);
		} else {
			lblName.setPropertyDataSource(item.getItemProperty("name"));
			txtName.setPropertyDataSource(item.getItemProperty("name"));
			image.setPropertyDataSource(item.getItemProperty("image"));
		}
		btnDelete.setEnabled(item != null);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean okButtonClicked() {
		Place place = item.getBean();
		container.removeItem(place.getId());
		File fileImg = place.getImage();
		if (fileImg != null && !fileImg.delete())
			getApplication().getMainWindow().showNotification("Das Bild des " +
					"Ortes konnte leider nicht gelöscht werden!",
					Notification.TYPE_ERROR_MESSAGE);
		DBManager.getCommunicator().delete(place);
		setItem(null);
		return true;
	}

}