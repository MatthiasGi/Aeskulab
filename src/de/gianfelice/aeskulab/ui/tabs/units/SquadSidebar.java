package de.gianfelice.aeskulab.ui.tabs.units;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.SquadContainer;
import de.gianfelice.aeskulab.data.container.SquadTypeContainer;
import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.SquadType;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow;
import de.gianfelice.aeskulab.ui.components.SquadTypeWindow;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow.ButtonOkListener;

/**
 * Manages and creates new squads.
 * 
 * @author  Matthias Gianfelice
 * @version 1.1.0
 */
public class SquadSidebar extends VerticalLayout implements ClickListener,
		ButtonOkListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A label for the name. */
	private Label lblName;
	
	/** A container with all squads. */
	private SquadContainer container;
	
	/** A textfield for the name. */
	private TextField txtName;
	
	/** A combobox to select a squad type. */
	private ComboBox combo;
	
	/** A button to cancel editing. */
	private Button btnCancel;
	
	/** A button to save changes. */
	private Button btnSave;
	
	/** A button to delete a squad. */
	private Button btnDelete;
	
	/** The current squad. */
	private Squad item = null;
	
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
	public SquadSidebar(SquadContainer container) {
		
		// Global options
		setMargin(true);
		setSpacing(true);
		this.container = container;
		
		// Name
		VerticalLayout verImage = new VerticalLayout();
		verImage.setWidth("100%");
		verImage.setMargin(true);
		verImage.setSpacing(true);
		verImage.setStyleName(Reindeer.LAYOUT_BLACK);
		addComponent(verImage);
		lblName = new Label();
		lblName.setSizeUndefined();
		lblName.setStyleName(Reindeer.LABEL_H1);
		verImage.addComponent(lblName);
		verImage.setComponentAlignment(lblName, Alignment.TOP_CENTER);
	
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
					"Wirklich löschen?", "Soll der Trupp " + item.getName() +
					" wirklich aufgelöst werden?", this);
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
				item = new Squad((String) txtName.getValue(), type);
				container.addSquad(item);
				setItem(item);
			} else {
				txtName.commit();
				item.setName((String) txtName.getValue());
				Item i = container.getItem(item.getId());
				i.getItemProperty("type").setValue(type);
				item.setType(type);
			}
		}
	}
	
	/**
	 * Resets the form.
	 */
	private void reset() {
		if (item == null) {
			txtName.setValue("");
			combo.setValue(null);
		} else {
			txtName.discard();
			combo.discard();
		}
	}
	
	/**
	 * Sets a new item.
	 * 
	 * @param item The new item
	 */
	public void setItem(Squad item) {
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
		container.removeItem(item.getId());
		DBManager.getCommunicator().delete(item);
		setItem(null);
		return true;
	}

}