package de.gianfelice.aeskulab.ui.tabs.mainmenu;

import java.util.Date;

import com.vaadin.data.Item;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.EventEntity;
import de.gianfelice.aeskulab.ui.tabs.TabMainMenu;

/**
 * A layout, that contains controls to edit an {@link EventEntity}.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     EventEntity
 * @see     TabMainMenu
 */
public class LayoutEvent extends VerticalLayout implements ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The entity. */
	private EventEntity entity;
	
	/** The main tab. */
	private TabMainMenu tab;
	
	/** A textfield for the title. */
	private TextField txtTitle;
	
	/** A field for the start date. */
	private DateField dateStart;
	
	/** A field for the end date. */
	private DateField dateEnd;
	
	/** A textfield for the place. */
	private TextField txtPlace;
	
	/** A textfield for the contact. */
	private TextField txtContact;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and adds data to it.
	 *
	 * @param entity The entity, that should be added ({@code null}, if a new
	 *               entity should be created)
	 * @param tab    The main tab
	 */
	public LayoutEvent(EventEntity entity, TabMainMenu tab) {
		this.entity = entity;
		this.tab = tab;
		setMargin(true);
		setSpacing(true);
		
		Label lblHeadline = new Label("Anlegen eines Einsatzes");
		if (entity != null) lblHeadline.setValue("Bearbeiten eines Einsatzes");
		lblHeadline.setStyleName(Reindeer.LABEL_H1);
		addComponent(lblHeadline);
		addComponent(new Label("Der Einsatz stellt die eigentliche Sammlung " +
				"an Daten dar, die in dieser Software verwaltet werden. Hier " +
				"wird ein Einsatz ausgewählt und geladen, sowie die gröbsten " +
				"Daten angegeben."));
		addComponent(new Label("Beachten Sie: Die Software unterscheidet " +
				"zwischen <i>lokalen</i> und <i>globalen</i> Einstellungen. " +
				"Erstere betreffen nur den aktuell ausgewählten Einsatz, " +
				"während Letztere für alle Einsätze gemeinsam gelten bzw. " +
				"zur Verfügung stehen (bspw. Einsatzkräfte und Fahrzeuge).",
				Label.CONTENT_XHTML));
		
		GridLayout grid = new GridLayout(4, 3);
		grid.setMargin(true);
		grid.setSpacing(true);
		addComponent(grid);
		setComponentAlignment(grid, Alignment.TOP_CENTER);
		
		addMsg(grid, "Titel");
		txtTitle = new TextField();
		txtTitle.setWriteThrough(false);
		txtTitle.setInvalidCommitted(false);
		txtTitle.setRequired(true);
		txtTitle.addValidator(new StringLengthValidator(
				"Der Titel muss zwischen 5 und 50 Zeichen lang sein!",
				5, 50, true));
		txtTitle.setWidth("100%");
		txtTitle.focus();
		grid.addComponent(txtTitle, 1, 0, 3, 0);
		
		addMsg(grid, "Startzeit");
		dateStart = new DateField();
		dateStart.setWriteThrough(false);
		dateStart.setInvalidCommitted(false);
		dateStart.setResolution(DateField.RESOLUTION_MIN);
		grid.addComponent(dateStart);
		
		addMsg(grid, "Endzeit");
		dateEnd = new DateField();
		if (entity != null) dateEnd.setValue(entity.getDateEnd());
		dateEnd.setResolution(DateField.RESOLUTION_MIN);
		grid.addComponent(dateEnd);
		
		addMsg(grid, "Veranstaltungsort");
		txtPlace = new TextField();
		txtPlace.setWidth("100%");
		if (entity != null) txtPlace.setValue(entity.getPlace());
		grid.addComponent(txtPlace);
		
		addMsg(grid, "Ansprechpartner");
		txtContact = new TextField();
		txtContact.setWidth("100%");
		if (entity != null) txtContact.setValue(entity.getContact());
		grid.addComponent(txtContact);

		Button btnSave = new Button("Speichern", this);
		btnSave.setStyleName(Reindeer.BUTTON_DEFAULT);
		btnSave.setClickShortcut(KeyCode.ENTER);
		addComponent(btnSave);
		setComponentAlignment(btnSave, Alignment.BOTTOM_RIGHT);
		
		if (entity != null) {
			Item item = tab.getContainer().getItem(entity);
			txtTitle.setPropertyDataSource(item.getItemProperty("title"));
			dateStart.setPropertyDataSource(item.getItemProperty("dateStart"));
		}
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Adds a centred message to the given {@link GridLayout}, e.g. for a field
	 * description.
	 *
	 * @param grid The grid
	 * @param msg  The message
	 */
	private void addMsg(GridLayout grid, String msg) {
		Label lbl = new Label(msg);
		grid.addComponent(lbl);
		grid.setComponentAlignment(lbl, Alignment.MIDDLE_RIGHT);
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		
		// Validation
		txtTitle.setComponentError(null);
		try {
			txtTitle.validate();
		} catch (EmptyValueException e) {
			txtTitle.setComponentError(
					new UserError("Bitte geben Sie einen Titel an!"));
			return;
		} catch (Exception e) {
			return;
		}
		
		// Create || Save
		boolean edit = (entity != null);
		if (edit) {
			txtTitle.commit();
			dateStart.commit();
		} else {
			entity = new EventEntity();
		}
		entity.setTitle((String) txtTitle.getValue());
		Object val = dateStart.getValue();
		if (val != null) entity.setDateStart((Date) val);
		val = dateEnd.getValue();
		if (val != null) entity.setDateEnd((Date) val);
		entity.setPlace((String) txtPlace.getValue());
		entity.setContact((String) txtContact.getValue());
		
		// Save
		if (!edit) tab.addEventEntity(entity);
		tab.sortTree();
		getApplication().getMainWindow().showNotification(
				"Einstellungen gespeichert");
		
	}

}