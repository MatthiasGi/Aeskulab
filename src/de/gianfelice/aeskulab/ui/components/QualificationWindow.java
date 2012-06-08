package de.gianfelice.aeskulab.ui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.QualificationContainer;
import de.gianfelice.aeskulab.data.entities.Qualification;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A window to manage all {@link Qualification} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Qualification
 */
public class QualificationWindow extends Window implements ClickListener,
		ValueChangeListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The top layout. */
	private VerticalLayout verTop;
	
	/** The bottom layout. */
	private VerticalLayout verBot;
	
	/** The container. */
	private QualificationContainer container;
	
	/** A combobox to select a {@link Qualification}. */
	private ComboBox combo;
	
	/** A button to create a new {@link Qualification}. */
	private Button btnNew;
	
	/** A button to cancel editing. */
	private Button btnCancel;
	
	/** A button to save all changes. */
	private Button btnSave;
	
	/** A button to delete a {@link Qualification}. */
	private Button btnDelete;
	
	/** A textfield for the name. */
	private TextField txtName;
	
	/** A tagfield with all minor qualifications. */
	private TagField tagQual;
	
	/** The current {@link Qualification}. */
	private Qualification active = null;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the window, sets the container, that should be edited, and
	 * creates a new {@link Qualification} with the given name.
	 * 
	 * @param newItemCaption The name
	 * @param container      The container
	 */
	public QualificationWindow(String newItemCaption,
			QualificationContainer container) {
		this(container);
		reset(false);
		txtName.setValue(newItemCaption);
	}
	
	/**
	 * Creates the window and sets the container, that should be edited.
	 * 
	 * @param container The container
	 */
	public QualificationWindow(QualificationContainer container) {
		
		// Global options
		super("Qualifikationen bearbeiten");
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
		verTop.addComponent(new Label("Wählen Sie bitte zunächst eine zu " +
				"bearbeitenden Qualifikation aus oder erstellen Sie eine " +
				"Neue:"));
		
		// Layout with combobox and button to create new qualification
		HorizontalLayout horOr = new HorizontalLayout();
		horOr.setSpacing(true);
		horOr.setWidth("100%");
		verTop.addComponent(horOr);
		
		// Combobox
		combo = new ComboBox();
		combo.setWidth("100%");
		combo.setContainerDataSource(container);
		combo.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
		combo.setImmediate(true);
		combo.addListener(this);
		combo.setNullSelectionAllowed(false);
		horOr.addComponent(combo);
		horOr.setComponentAlignment(combo, Alignment.MIDDLE_LEFT);
		horOr.setExpandRatio(combo, 1.0f);
		
		// Create new qualification
		Label lblOr = new Label("oder");
		lblOr.setSizeUndefined();
		lblOr.setStyleName(Reindeer.LABEL_SMALL);
		horOr.addComponent(lblOr);
		horOr.setComponentAlignment(lblOr, Alignment.MIDDLE_LEFT);
		btnNew = new Button("Neue Qualifikation");
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
				"Legen Sie einen Namen für die Qualifikation fest:"));
		
		// Name
		txtName = new TextField();
		txtName.setWidth("100%");
		verBot.addComponent(txtName);
		verBot.addComponent(new Label("Geben Sie diejenigen Qualifikationen " +
				"an, die unter der aktuellen liegen. Sollte bspw. nach " +
				"Helfern mit einer darunter liegenden Qualifikation gesucht " +
				"werden, würden auch all diejenigen Helfer gelistet, die " +
				"über diese Qualifikation verfügen:"));
		
		// Minor Qualifications
		tagQual = new TagField();
		tagQual.setContainerDataSource(container);
		verBot.addComponent(tagQual);
		
		// Warning message
		Label lblWarning = new Label("Hinweis: Die Software erstellt intern " +
				"keine Hierarchie o.ä. Strukturen. Soll also auch ein bspw. " +
				"Rettungsassistent gelistet werden, wenn nach einem " +
				"Rettungshelfer gesucht wird, genügt es nicht, unter der " +
				"Qualifikation Rettungssanitäter den Rettungshelfer und " +
				"unter Rettungsassistent den Rettungssanitäter zu nennen. " +
				"Der Rettungshelfer muss in diesem Falle auch hier angegeben " +
				"werden!");
		lblWarning.setStyleName(Reindeer.LABEL_SMALL);
		verBot.addComponent(lblWarning);
		
		// Buttons
		HorizontalLayout horButtons = new HorizontalLayout();
		horButtons.setSpacing(true);
		horButtons.setWidth("100%");
		verBot.addComponent(horButtons);
		
		// Delete qualification
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
	@Override
	public void attach() {
		super.attach();
		btnNew.setIcon(new ClassResource("res/16/add.png", getApplication()));
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
		List<Qualification> quals = active.getMinorQualifications();
		for (Qualification qual : quals) tagQual.addToken(qual.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		txtName.setComponentError(null);
		Button btn = event.getButton();
		if (btn == btnNew) {
			reset(false);
			active = null;
		} else if (btn == btnDelete) {
			if (active == null) reset(true);
			container.removeItem(active.getName());
			List<Qualification> quals = active.getMajorQualifications();
			for (Qualification qual : quals)
				qual.removeMinorQualification(active);
			DBManager.getCommunicator().delete(active);
			reset(true);
		} else if (btn == btnCancel) {
			reset(true);
		} else if (btn == btnSave) {
			String name = (String) txtName.getValue();
			if (name == null || name.equals("")) {
				txtName.setComponentError(new UserError(
						"Bitte geben Sie einen gültigen Namen an!"));
				txtName.focus();
				return;
			} else if (active == null) {
				try {
					active = new Qualification(name);
					container.addBean(active);
				} catch (Exception e) {
					txtName.setComponentError(new UserError("Es existiert " +
							"bereits eine Qualifikation mit diesem Namen!"));
					return;
				}
			}
			Set<?> set = (Set<?>) tagQual.getValue();
			if (set != null) {
				List<Qualification> quals = new ArrayList<Qualification>();
				for (Object obj : set) quals.add(container.getItem(obj).getBean());
				active.setMinorQualifications(quals);
			}
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
		tagQual.setValue(null);
		if (top) {
			active = null;
			combo.focus();
		} else {
			txtName.focus();
		}
	}

}