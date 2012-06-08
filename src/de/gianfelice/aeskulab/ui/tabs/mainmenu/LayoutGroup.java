package de.gianfelice.aeskulab.ui.tabs.mainmenu;

import com.vaadin.data.Item;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.EventGroup;
import de.gianfelice.aeskulab.ui.tabs.TabMainMenu;

/**
 * A layout, that contains controls to edit an {@link EventGroup}.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     EventGroup
 * @see     TabMainMenu
 */
public class LayoutGroup extends VerticalLayout implements ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A textfield for the title. */
	private TextField txtTitle;
	
	/** The group. */
	private EventGroup group;
	
	/** The main tab. */
	private TabMainMenu tab;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and adds data to it.
	 *
	 * @param group The group, that should be added ({@code null}, if a new
	 *              group should be created)
	 * @param tab   The main tab
	 */
	public LayoutGroup(EventGroup group, TabMainMenu tab) {
		this.group = group;
		this.tab = tab;
		setMargin(true);
		setSpacing(true);
		
		Label lblHeadline = new Label("Anlegen einer Einsatzgruppierung");
		if (group != null)
			lblHeadline.setValue("Bearbeiten einer Einsatzgruppierung");
		lblHeadline.setStyleName(Reindeer.LABEL_H1);
		addComponent(lblHeadline);
		
		addComponent(new Label("Eine Einsatzgruppierung ist lediglich dazu " +
				"da, um ähnliche Einsätze zu gruppieren. So lässt sich die " +
				"Struktur der Übersicht auf der linken Seite ordnen und auf " +
				"die einzelnen Elemente schneller zugreifen."));
		addComponent(new Label("So lässt sich von einer Gruppe auch nur ein " +
				"Titel bearbeiten, da es sich nicht um eine funktionale " +
				"Gruppe handelt."));
		
		GridLayout grid = new GridLayout(2, 1);
		grid.setSpacing(true);
		grid.setMargin(true);
		addComponent(grid);
		setComponentAlignment(grid, Alignment.TOP_CENTER);
		
		Label lblTitle = new Label("Titel:");
		grid.addComponent(lblTitle);
		grid.setComponentAlignment(lblTitle, Alignment.MIDDLE_LEFT);
		txtTitle = new TextField();
		txtTitle.setWriteThrough(false);
		txtTitle.setInvalidCommitted(false);
		txtTitle.setRequired(true);
		txtTitle.addValidator(new StringLengthValidator(
				"Der Titel muss zwischen 5 und 50 Zeichen lang sein!",
				5, 50, true));
		txtTitle.setWidth("250px");
		txtTitle.focus();
		grid.addComponent(txtTitle);
		
		Button btnSave = new Button("Speichern");
		btnSave.setStyleName(Reindeer.BUTTON_DEFAULT);
		btnSave.setClickShortcut(KeyCode.ENTER);
		btnSave.addListener(this);
		addComponent(btnSave);
		setComponentAlignment(btnSave, Alignment.BOTTOM_RIGHT);
		
		if (group != null) {
			Item item = tab.getContainer().getItem(group);
			txtTitle.setPropertyDataSource(item.getItemProperty("title"));
		}
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		try {
			txtTitle.setComponentError(null);
			if (group != null) {
				txtTitle.commit();
				group.setTitle((String) txtTitle.getValue());
			} else {
				txtTitle.validate();
				String title = (String) txtTitle.getValue();
				EventGroup group = new EventGroup(title);
				tab.addEventGroup(group);
			}
			tab.sortTree();
			getApplication().getMainWindow().showNotification(
					"Einstellungen gespeichert");
		} catch (EmptyValueException e) {
			txtTitle.setComponentError(
					new UserError("Bitte geben Sie einen Titel an!"));
		} catch (Exception e) {}
	}

}