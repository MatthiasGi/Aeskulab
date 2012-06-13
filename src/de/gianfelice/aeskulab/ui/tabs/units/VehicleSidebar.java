package de.gianfelice.aeskulab.ui.tabs.units;

import java.io.File;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.SquadTypeContainer;
import de.gianfelice.aeskulab.data.container.VehicleContainer;
import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.SquadType;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.system.utils.FileUtil;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow;
import de.gianfelice.aeskulab.ui.components.ImageManager;
import de.gianfelice.aeskulab.ui.components.SquadTypeWindow;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow.ButtonOkListener;

/**
 * Manages and creates new vehicles.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class VehicleSidebar extends VerticalLayout implements ClickListener, ButtonOkListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A label for the name. */
	private Label lblName;
	
	/** An image for the vehicle. */
	private ImageManager img;
	
	/** A container with all vehicles. */
	private VehicleContainer container;
	
	/** A textfield for the name. */
	private TextField txtName;
	
	/** A combobox to select a squad type. */
	private ComboBox combo;
	
	/** A button to cancel editing. */
	private Button btnCancel;
	
	/** A button to save changes. */
	private Button btnSave;
	
	/** A button to delete a vehicle. */
	private Button btnDelete;
	
	/** The current vehicle. */
	private Vehicle item = null;
	
	/** A form to hold all fields. */
	private Form form;
	
	/** A button to edit squad types. */
	private Button btnEditType;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the sidebar and sets the container.
	 * 
	 * @param container The container
	 */
	public VehicleSidebar(VehicleContainer container) {
		
		// Global options
		setMargin(true);
		setSpacing(true);
		this.container = container;
		
		// Layout for images
		VerticalLayout verImage = new VerticalLayout();
		verImage.setWidth("100%");
		verImage.setMargin(true);
		verImage.setSpacing(true);
		verImage.setStyleName(Reindeer.LAYOUT_BLACK);
		addComponent(verImage);
		
		// Name
		lblName = new Label();
		lblName.setSizeUndefined();
		lblName.setStyleName(Reindeer.LABEL_H1);
		verImage.addComponent(lblName);
		verImage.setComponentAlignment(lblName, Alignment.TOP_CENTER);
		
		// Image
		img = new ImageManager(FileUtil.IMAGE_VEHICLE) {
			private static final long serialVersionUID = 1L;
			
			protected void onPropertyUpdate(File newValue) {
				super.onPropertyUpdate(newValue);
				item.setImage(newValue);
			}
		};
		verImage.addComponent(img);
		verImage.setComponentAlignment(img, Alignment.TOP_CENTER);
	
		// Form
		form = new Form();
		addComponent(form);
		
		// Name
		txtName = new TextField("Name");
		txtName.setWidth("100%");
		txtName.setRequired(true);
		txtName.setRequiredError("Bitte geben Sie einen Namen an!");
		txtName.setWriteThrough(false);
		txtName.setInvalidCommitted(false);
		form.getLayout().addComponent(txtName);
		
		// Squad type container
		final SquadTypeContainer typeContainer = new SquadTypeContainer();
		HorizontalLayout horCombo = new HorizontalLayout();
		horCombo.setWidth("100%");
		horCombo.setCaption("Trupptyp");
		form.getLayout().addComponent(horCombo);
		
		// Combo
		combo = new ComboBox();
		combo.setWidth("100%");
		combo.setContainerDataSource(typeContainer);
		combo.setItemCaptionPropertyId("name");
		combo.setRequired(true);
		combo.setRequiredError("Bitte geben Sie einen Trupptypen an!");
		combo.setWriteThrough(false);
		combo.setInvalidCommitted(false);
		combo.setNullSelectionAllowed(false);
		horCombo.addComponent(combo);
		horCombo.setExpandRatio(combo, 1.0f);
		
		// Edit squad types
		btnEditType = new Button();
		btnEditType.setDescription("Trupptypen bearbeiten");
		btnEditType.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				getApplication().getMainWindow().addWindow(
						new SquadTypeWindow(typeContainer));
			}
		});
		horCombo.addComponent(btnEditType);
		
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
	@Override
	public void attach() {
		super.attach();
		btnEditType.setIcon(new ClassResource("res/16/pencil_go.png",
				getApplication()));
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		form.setComponentError(null);
		
		Button btn = event.getButton();
		if (btn == btnDelete) {
			ConfirmationWindow confirmation = new ConfirmationWindow(
					"Wirklich löschen?", "Soll das Fahrzeug " + item.getName() +
					" wirklich gelöscht werden?", this);
			getApplication().getMainWindow().addWindow(confirmation);
		} else if (btn == btnCancel) {
			reset();
		} else if (btn == btnSave) {
			try {
				txtName.validate();
				combo.validate();
			} catch (Exception e) {
				form.setComponentError(new UserError(e.getMessage()));
				return;
			}

			SquadTypeContainer typeContainer = new SquadTypeContainer();
			SquadType type = typeContainer.getItem(combo.getValue()).getBean();
			if (item == null) {
				item = new Vehicle((String) txtName.getValue());
				item.setType(type);
				container.addVehicle(item);
			} else {
				txtName.commit();
				Item i = container.getItem(item.getId());
				i.getItemProperty("type").setValue(type);
			}
			setItem(item);
		}
	}
	
	/**
	 * Resets the form.
	 */
	private void reset() {
		if (item == null) {
			txtName.setValue("");
			combo.setValue(null);
			img.setEnabled(false);
		} else {
			txtName.discard();
			combo.discard();
			img.setEnabled(true);
		}
	}
	
	/**
	 * Sets a new item.
	 * 
	 * @param item The new item
	 */
	public void setItem(Vehicle item) {
		this.item = item;
		reset();
		if (item == null) {
			lblName.setPropertyDataSource(
					new ObjectProperty<String>("", String.class));
			txtName.setPropertyDataSource(null);
		} else {
			Item i = container.getItem(item.getId());
			lblName.setPropertyDataSource(i.getItemProperty("name"));
			txtName.setPropertyDataSource(i.getItemProperty("name"));
			SquadType type = item.getType();
			combo.setValue((type == null) ? null : type.getId());
			img.setPropertyDataSource(i.getItemProperty("image"));
		}
		btnDelete.setEnabled(item != null);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean okButtonClicked() {
		List<Helper> helpers = item.getHelpers();
		for (Helper helper : helpers) container.removeItem(helper.getId());
		item.setHelpers(null);
		File fileImg = item.getImage();
		if (fileImg != null && !fileImg.delete())
			getApplication().getMainWindow().showNotification("Das Bild des " +
					"Fahrzeuges konnte leider nicht gelöscht werden!",
					Notification.TYPE_ERROR_MESSAGE);
		container.removeItem(item.getId());
		DBManager.getCommunicator().delete(item);
		setItem(null);
		return true;
	}

}