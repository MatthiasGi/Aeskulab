package de.gianfelice.aeskulab.ui.tabs.units;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
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

import de.gianfelice.aeskulab.data.container.HelperContainer;
import de.gianfelice.aeskulab.data.container.QualificationContainer;
import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Qualification;
import de.gianfelice.aeskulab.system.authentication.AuthenticationUtil;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.system.utils.FileUtil;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow;
import de.gianfelice.aeskulab.ui.components.ImageManager;
import de.gianfelice.aeskulab.ui.components.QualificationField;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow.ButtonOkListener;

/**
 * Manages and creates new helpers.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class HelperSidebar extends VerticalLayout implements ClickListener, ButtonOkListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A label for the first name. */
	private Label lblFirstName;
	
	/** A label for the last name. */
	private Label lblLastName;
	
	/** An image of the helper. */
	private ImageManager image;
	
	/** A container with all helpers. */
	private HelperContainer container;
	
	/** A textfield for the first name. */
	private TextField txtFirstName;
	
	/** A textfield for the last name. */
	private TextField txtLastName;
	
	/** A textfield for the phone number. */
	private TextField txtNumber;
	
	/** A button to cancel editing. */
	private Button btnCancel;
	
	/** A button to save changes. */
	private Button btnSave;
	
	/** A button to delete a helper. */
	private Button btnDelete;
	
	/** The current helper. */
	private BeanItem<Helper> item = null;
	
	/** A form to hold all fields. */
	private Form form;
	
	/** A field with all qualifications. */
	private QualificationField quals;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the sidebar and sets the container.
	 * 
	 * @param container The container
	 */
	public HelperSidebar(HelperContainer container) {
		
		// Global options
		setMargin(true);
		setSpacing(true);
		this.container = container;
		
		// Image layout
		VerticalLayout verImage = new VerticalLayout();
		verImage.setWidth("100%");
		verImage.setMargin(true);
		verImage.setSpacing(true);
		verImage.setStyleName(Reindeer.LAYOUT_BLACK);
		addComponent(verImage);
		
		// Name
		HorizontalLayout horName = new HorizontalLayout();
		horName.setSpacing(true);
		verImage.addComponent(horName);
		verImage.setComponentAlignment(horName, Alignment.TOP_CENTER);
		lblFirstName = new Label();
		lblFirstName.setStyleName(Reindeer.LABEL_H1);
		lblFirstName.setSizeUndefined();
		horName.addComponent(lblFirstName);
		lblLastName = new Label();
		lblLastName.setStyleName(Reindeer.LABEL_H1);
		lblLastName.setSizeUndefined();
		horName.addComponent(lblLastName);
		
		// Image
		image = new ImageManager(FileUtil.IMAGE_USER);
		verImage.addComponent(image);
		verImage.setComponentAlignment(image, Alignment.TOP_CENTER);
		
		// Form
		form = new Form();
		addComponent(form);
		
		// First name
		txtFirstName = new TextField("Vorname");
		txtFirstName.setWidth("100%");
		txtFirstName.setRequired(true);
		txtFirstName.setRequiredError("Bitte geben Sie einen Vornamen an!");
		txtFirstName.addValidator(AuthenticationUtil.VALIDATOR_FIRSTNAME);
		txtFirstName.setWriteThrough(false);
		txtFirstName.setInvalidCommitted(false);
		form.getLayout().addComponent(txtFirstName);
		
		// Last name
		txtLastName = new TextField("Nachname");
		txtLastName.setWidth("100%");
		txtLastName.setRequired(true);
		txtLastName.setRequiredError("Bitte geben Sie einen Nachnamen an!");
		txtLastName.addValidator(AuthenticationUtil.VALIDATOR_LASTNAME);
		txtLastName.setWriteThrough(false);
		txtLastName.setInvalidCommitted(false);
		form.getLayout().addComponent(txtLastName);
		
		// Number
		txtNumber = new TextField("Nummer");
		txtNumber.setWidth("100%");
		txtNumber.setWriteThrough(false);
		form.getLayout().addComponent(txtNumber);
		
		// Qualifications
		quals = new QualificationField();
		quals.setCaption("Qualifikationen");
		form.getLayout().addComponent(quals);
		
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
			String firstName = (String) item.getItemProperty("firstName").getValue();
			String lastName = (String) item.getItemProperty("lastName").getValue();
			ConfirmationWindow confirmation = new ConfirmationWindow(
					"Wirklich löschen?", "Soll " + firstName + " " + lastName + 
					" wirklich unwiderruflich gelöscht werden?", this);
			getApplication().getMainWindow().addWindow(confirmation);
		} else if (btn == btnCancel) {
			reset();
		} else if (btn == btnSave) {
			try {
				txtFirstName.validate();
				txtLastName.validate();
			} catch (Exception e) {
				form.setComponentError(new UserError(e.getMessage()));
				return;
			}
			Object telNumber = txtNumber.getValue();
			
			if (item == null) {
				String firstName = (String) txtFirstName.getValue();
				String lastName = (String) txtLastName.getValue();
				Helper helper = new Helper(firstName, lastName);
				container.addBean(helper);
				setItem(container.getItem(helper.getId()));
				txtNumber.setValue(telNumber);
			} else {
				txtFirstName.commit();
				txtLastName.commit();
			}
			txtNumber.commit();
			
			Set<?> set = (Set<?>) quals.getTagField().getValue();
			if (set != null) {
				List<Qualification> quals = new ArrayList<Qualification>();
				QualificationContainer container = new QualificationContainer();
				for (Object obj : set)
					quals.add(container.getItem(obj.toString()).getBean());
				item.getItemProperty("qualifications").setValue(quals);
			} else {
				item.getItemProperty("qualifications").setValue(null);
			}
		}
	}
	
	/**
	 * Resets the form.
	 */
	private void reset() {
		if (item == null) {
			txtFirstName.setValue("");
			txtLastName.setValue("");
			txtNumber.setValue("");
			image.setEnabled(false);
		} else {
			txtFirstName.discard();
			txtLastName.discard();
			txtNumber.discard();
			image.setEnabled(true);
		}
	}
	
	/**
	 * Sets a new item.
	 * 
	 * @param item The new item
	 */
	public void setItem(BeanItem<Helper> item) {
		this.item = item;
		reset();
		if (item == null) {
			lblFirstName.setPropertyDataSource(
					new ObjectProperty<String>("", String.class));
			lblLastName.setPropertyDataSource(
					new ObjectProperty<String>("", String.class));
			image.setPropertyDataSource(null);
			txtFirstName.setPropertyDataSource(null);
			txtLastName.setPropertyDataSource(null);
			txtNumber.setPropertyDataSource(null);
			quals.getTagField().setValue(null);
		} else {
			lblFirstName.setPropertyDataSource(
					item.getItemProperty("firstName"));
			lblLastName.setValue(item.getItemProperty("lastName"));
			txtFirstName.setPropertyDataSource(
					item.getItemProperty("firstName"));
			txtLastName.setPropertyDataSource(item.getItemProperty("lastName"));
			txtNumber.setPropertyDataSource(item.getItemProperty("telNumber"));
			image.setPropertyDataSource(item.getItemProperty("image"));
			Object val = item.getItemProperty("qualifications").getValue();
			if (val instanceof List<?> && val != null) {
				List<?> list = (List<?>) val;
				for (Object obj : list) quals.getTagField().addToken(obj);
			} else {
				quals.getTagField().setValue(null);
			}
		}
		btnDelete.setEnabled(item != null);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean okButtonClicked() {
		Helper helper = item.getBean();
		container.removeItem(helper.getId());
		File fileImg = helper.getImage();
		if (fileImg != null && !fileImg.delete())
			getApplication().getMainWindow().showNotification("Das Bild des " +
					"Benutzers konnte leider nicht gelöscht werden!",
					Notification.TYPE_ERROR_MESSAGE);
		DBManager.getCommunicator().delete(helper);
		setItem(null);
		return true;
	}

}