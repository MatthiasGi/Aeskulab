package de.gianfelice.aeskulab.ui.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.gianfelice.aeskulab.data.container.SquadTypeContainer;
import de.gianfelice.aeskulab.data.entities.HelperType;
import de.gianfelice.aeskulab.data.entities.Qualification;
import de.gianfelice.aeskulab.data.entities.SquadType;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A window to manage all {@link SquadType} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     SquadType
 */
public class SquadTypeWindow extends Window implements ValueChangeListener, ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The container. */
	private SquadTypeContainer container;
	
	/** The top layout. */
	private VerticalLayout verTop;
	
	/** The bottom layout. */
	private VerticalLayout verBot;
	
	/** A button to create a new {@link SquadType}. */
	private Button btnNew;
	
	/** A button to add a new {@link HelperType}. */
	private Button btnNewHelper;
	
	/** A button to delete a {@link SquadType}. */
	private Button btnDelete;
	
	/** A button to cancel editing. */
	private Button btnCancel;
	
	/** A button to save all changes. */
	private Button btnSave;
	
	/** A combobox to select a {@link SquadType}. */
	private ComboBox combo;

	/** The current {@link SquadType}. */
	private SquadType active = null;
	
	/** A textfield for the name. */
	private TextField txtName;
	
	/** A list with all {@link HelperType} objects. */
	private VerticalLayout list;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the window and sets the container, that should be edited.
	 * 
	 * @param container The container
	 */
	public SquadTypeWindow(SquadTypeContainer container) {
		
		// Global options
		super("Trupptypen bearbeiten");
		this.container = container;
		setModal(true);
		setWidth("600px");
		
		// Main layout
		VerticalLayout verContent = new VerticalLayout();
		verContent.setMargin(true);
		verContent.setSpacing(true);
		setContent(verContent);
		
		// Top layout
		verTop = new VerticalLayout();
		verTop.setSpacing(true);
		addComponent(verTop);
		verTop.addComponent(new Label("Wählen Sie bitte zunächst einen zu " +
				"bearbeitenden Typen aus oder erstellen Sie einen Neuen:"));
		
		// Layout with combobox and button to create new type
		HorizontalLayout horOr = new HorizontalLayout();
		horOr.setSpacing(true);
		horOr.setWidth("100%");
		verTop.addComponent(horOr);
		
		// Combobox
		combo = new ComboBox();
		combo.setWidth("100%");
		combo.setContainerDataSource(container);
		combo.setItemCaptionPropertyId("name");
		combo.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
		combo.setImmediate(true);
		combo.addListener(this);
		combo.setNullSelectionAllowed(false);
		horOr.addComponent(combo);
		horOr.setComponentAlignment(combo, Alignment.MIDDLE_LEFT);
		horOr.setExpandRatio(combo, 1.0f);
		
		// Create new type
		Label lblOr = new Label("oder");
		lblOr.setSizeUndefined();
		lblOr.setStyleName(Reindeer.LABEL_SMALL);
		horOr.addComponent(lblOr);
		horOr.setComponentAlignment(lblOr, Alignment.MIDDLE_LEFT);
		btnNew = new Button("Neuer Typ");
		btnNew.addListener((ClickListener) this);
		horOr.addComponent(btnNew);
		
		// Separator of top and bottom
		addComponent(new Label("<hr />", Label.CONTENT_XHTML));
		
		// Bottom layout
		verBot = new VerticalLayout();
		verBot.setSpacing(true);
		verBot.setEnabled(false);
		addComponent(verBot);
		verBot.addComponent(new Label(
				"Legen Sie einen Namen für den Typ fest:"));
		
		// Name
		txtName = new TextField();
		txtName.setWidth("100%");
		verBot.addComponent(txtName);
		verBot.addComponent(new Label(
				"Verwalten Sie nun die zugehörigen Helfertypen:"));
		
		// Helper types
		Panel content = new Panel();
		content.setWidth("100%");
		content.setHeight("300px");
		verBot.addComponent(content);
		
		// Content of panel with helper types
		VerticalLayout verPanel = new VerticalLayout();
		content.setContent(verPanel);
		verPanel.setSizeFull();
		verPanel.setSpacing(true);
		verPanel.setMargin(true);
		
		// List panel with helper types
		Panel listPanel = new Panel();
		listPanel.setStyleName(Reindeer.PANEL_LIGHT);
		listPanel.setSizeFull();
		verPanel.addComponent(listPanel);
		verPanel.setExpandRatio(listPanel, 1.0f);
		
		// List with helper types
		list = new VerticalLayout();
		list.setSpacing(true);
		listPanel.setContent(list);
		
		// Create new helper types
		verPanel.addComponent(new Label("<hr />", Label.CONTENT_XHTML));
		btnNewHelper = new Button("Neuer Helfertyp");
		btnNewHelper.addListener((ClickListener) this);
		verPanel.addComponent(btnNewHelper);
		verPanel.setComponentAlignment(btnNewHelper, Alignment.TOP_RIGHT);
		
		// Buttons
		HorizontalLayout horButtons = new HorizontalLayout();
		horButtons.setSpacing(true);
		horButtons.setWidth("100%");
		verBot.addComponent(horButtons);
		
		// Delete type
		btnDelete = new Button("Löschen");
		btnDelete.setStyleName(Reindeer.BUTTON_SMALL);
		btnDelete.addListener((ClickListener) this);
		horButtons.addComponent(btnDelete);
		horButtons.setComponentAlignment(btnDelete, Alignment.MIDDLE_LEFT);
		horButtons.setExpandRatio(btnDelete, 1.0f);
		
		// Cancel editing
		btnCancel = new Button("Abbrechen");
		btnCancel.setStyleName(Reindeer.BUTTON_LINK);
		btnCancel.addListener((ClickListener) this);
		horButtons.addComponent(btnCancel);
		horButtons.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		
		// Save changes
		btnSave = new Button("Speichern");
		btnSave.addListener((ClickListener) this);
		horButtons.addComponent(btnSave);

	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		txtName.setComponentError(null);
		Button btn = event.getButton();
		if (btn == btnNew) {
			reset(false);
			active = null;
		} else if (btn == btnNewHelper) {
			list.addComponent(new HelperTypePanel());
		} else if (btn == btnDelete) {
			if (active == null) reset(true);
			container.removeItem(active.getId());
			List<HelperType> helpers = active.getHelperTypes();
			for (HelperType helper : helpers) {
				active.removeHelperType(helper);
				DBManager.getCommunicator().delete(helper);
			}
			DBManager.getCommunicator().delete(active);
			reset(true);
		} else if (btn == btnCancel) {
			reset(true);
		} else if (btn == btnSave) {
			String name = (String) txtName.getValue();
			if (name == null || name.equals("")) {
				txtName.setComponentError(new UserError(
						"Bitt geben Sie einen gültigen Namen an!"));
				txtName.focus();
				return;
			} else if (active == null) {
				try {
					active = new SquadType(name);
					container.addBean(active);
				} catch (Exception e) {
					txtName.setComponentError(new UserError("Es existiert " +
							"bereits eine Qualifikation mit diesem Namen!"));
					return;
				}
			}
			Iterator<Component> coms = list.getComponentIterator();
			List<HelperType> helperTypes = new ArrayList<HelperType>();
			while (coms.hasNext()) {
				Component com = coms.next();
				if (!(com instanceof HelperTypePanel)) continue;
				HelperTypePanel panel = (HelperTypePanel) com;
				List<Qualification> quals = panel.getQualField().getQualifications();
				HelperType helperType = new HelperType();
				helperType.store();
				if (quals != null) helperType.setQualifications(quals);
				helperTypes.add(helperType);
			}
			active.setHelperTypes(helperTypes);
			reset(true);
		}
	}

	/**
	 * Resets the window.
	 * 
	 * @param top Whether the top layout should be enabled, or the bottom one
	 */
	private void reset(boolean top) {
		verTop.setEnabled(top);
		verBot.setEnabled(!top);
		combo.setValue(null);
		txtName.setValue("");
		list.removeAllComponents();
		if (top) {
			active = null;
			combo.focus();
		} else {
			txtName.focus();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueChange(ValueChangeEvent event) {
		Object obj = combo.getValue();
		if (obj == null) return;
		active = container.getItem(obj).getBean();
		reset(false);
		txtName.setValue(active.getName());
		List<HelperType> helpers = active.getHelperTypes();
		for (HelperType helper : helpers) {
			HelperTypePanel panel = new HelperTypePanel();
			panel.getQualField().setQualifications(helper.getQualifications());
			list.addComponent(panel);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		Application app = getApplication();
		btnNew.setIcon(new ClassResource("res/16/add.png", app));
		btnNewHelper.setIcon(new ClassResource("res/16/user_add.png", app));
	}

	// ---------------------------- Inner Class(es) ----------------------------
	/**
	 * A simple panel to display a {@link HelperType} object.
	 * 
	 * @author  Matthias Gianfelice
	 * @version 1.0.0
	 * @see     HelperType
	 */
	private class HelperTypePanel extends Panel implements ClickListener {

		// ---------------------------- Attribute(s) ---------------------------
		/** The default serial version id. */
		private static final long serialVersionUID = 1L;
		
		/** Simple image to make the panel more beautiful. */
		private Embedded emb;
		
		/** Qualifications of the helper type. */
		private QualificationField quals;
		
		/** Button to remove the helper type. */
		private Button btnDelete;
		
		// --------------------------- Constructor(s) --------------------------
		/**
		 * Creates the panel.
		 */
		public HelperTypePanel() {
			setWidth("100%");
			HorizontalLayout horLayout = new HorizontalLayout();
			horLayout.setSizeFull();
			horLayout.setMargin(true);
			horLayout.setSpacing(true);
			setContent(horLayout);
			
			emb = new Embedded();
			emb.setWidth("30px");
			emb.setHeight("30px");
			horLayout.addComponent(emb);
			
			quals = new QualificationField();
			quals.setWidth("100%");
			horLayout.addComponent(quals);
			horLayout.setExpandRatio(quals, 1.0f);
			
			btnDelete = new Button();
			btnDelete.addListener(this);
			btnDelete.setDescription("Entfernt diesen Helfertyp");
			horLayout.addComponent(btnDelete);
		}
		
		// ----------------------------- Method(s) -----------------------------
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void attach() {
			super.attach();
			Application app = getApplication();
			emb.setSource(new ClassResource("res/30/sheriff.png", app));
			btnDelete.setIcon(new ClassResource("res/16/cancel.png", app));
		}
		
		/**
		 * Gets the qualification field.
		 * 
		 * @return The qualification field
		 */
		public QualificationField getQualField() {
			return quals;
		}

		/**
		 * {@inheritDoc}
		 */
		public void buttonClick(ClickEvent event) {
			list.removeComponent(this);
		}
		
	}
	
}