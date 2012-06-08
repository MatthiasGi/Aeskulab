package de.gianfelice.aeskulab.ui.tabs.mainmenu;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.EventEntity;
import de.gianfelice.aeskulab.data.entities.EventGroup;
import de.gianfelice.aeskulab.system.persistence.PersistentObject;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow;
import de.gianfelice.aeskulab.ui.components.ConfirmationWindow.ButtonOkListener;
import de.gianfelice.aeskulab.ui.tabs.TabMainMenu;

/**
 * A toolbar and different options.
 * 
 * @author  Matthias Gianfelice
 * @version 0.2.1
 */
public class ToolBar extends HorizontalLayout implements ClickListener,
		ButtonOkListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The main tab. */
	private TabMainMenu tab;
	
	/** Button to create a new group. */
	private Button btnNewGroup;
	
	/** Button to create a new event. */
	private Button btnNewEvent;
	
	/** Button to export an event. */
	private Button btnExport;
	
	/** Button to import an event. */
	private Button btnImport;
	
	/** Button do delete objects. */
	private Button btnDelete;
	
	/** Button to duplicate events. */
	private Button btnDuplicate;
	
	/** The current active object. Could possibly be {@code null}. */
	private PersistentObject active;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates a new toolbar and adds the components to it.
	 *
	 * @param tab The main tab
	 */
	public ToolBar(TabMainMenu tab) {
		this.tab = tab;
		setSpacing(true);
		setMargin(true);
		setWidth("100%");
		setStyleName(Reindeer.LAYOUT_BLUE);
		
		btnNewGroup = new Button("Neue Gruppe");		
		btnNewEvent = new Button("Neuer Einsatz");		
		btnExport = new Button("Exportieren");		
		btnImport = new Button("Importieren");
		btnDelete = new Button("Löschen");
		btnDuplicate = new Button("Duplizieren");
		addComponent(btnNewGroup);
		addComponent(btnNewEvent);
		addComponent(btnExport);
		addComponent(btnImport);
		addComponent(btnDelete);
		addComponent(btnDuplicate);
		setExpandRatio(btnImport, 1.0f);
		btnNewGroup.addListener(this);
		btnNewEvent.addListener(this);
		btnExport.addListener(this);
		btnImport.addListener(this);
		btnDelete.addListener(this);
		btnDuplicate.addListener(this);
		btnExport.setEnabled(false);
		btnImport.setEnabled(false);
		btnDelete.setEnabled(false);
		btnDuplicate.setEnabled(false);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		Application app = getApplication();
		btnNewGroup.setIcon(new ClassResource("res/16/folder_add.png", app));
		btnNewEvent.setIcon(new ClassResource(
				"res/16/page_white_add.png", app));
		btnExport.setIcon(new ClassResource("res/16/page_white_go.png", app));
		btnImport.setIcon(new ClassResource("res/16/page_white_get.png", app));
		btnDelete.setIcon(new ClassResource("res/16/bin.png", app));
		btnDuplicate.setIcon(new ClassResource(
				"res/16/page_white_copy.png", app));
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		Button btn = event.getButton();
		if (btn == btnNewGroup) {
			tab.setEventGroup(null);
		} else if (btn == btnNewEvent) {
			tab.setEventEntity(null);
		} else if (btn == btnDelete && active != null) {
			delete();
		} else {
			getApplication().getMainWindow().showNotification(
					"Die Funktion wurde leider noch nicht umgesetzt!",
					Notification.TYPE_WARNING_MESSAGE);
		}
	}
	
	/**
	 * Sets the active object.
	 *
	 * @param active The new active object
	 */
	public void setActive(PersistentObject active) {
		this.active = active;
		btnDelete.setEnabled(active != null);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean okButtonClicked() {
		if (active instanceof EventGroup) {
			((EventGroup) active).destroy();
		} else if (active instanceof EventEntity) {
			((EventEntity) active).destroy();
		}
		tab.updateTree();
		tab.setNull();
		this.active = null;
		return true;
	}
	
	/**
	 * Prepares the deletion of the current object.
	 */
	private void delete() {
		String text = "";
		if (active instanceof EventGroup) {
			EventGroup active = (EventGroup) this.active;
			text += "Soll die Gruppierung <i>" + active.getTitle() + "</i> ";
			text += "wirklich gelöscht werden?<br /><br />Die ihr zugeteilten ";
			text += "Einsätze werden nicht mitgelöscht, sondern in die ";
			text += "folgende Ebene verschoben.";
		} else if (active instanceof EventEntity){
			EventEntity active = (EventEntity) this.active;
			text += "Soll der Einsatz <i>" + active.getTitle() + "</i> ";
			text += "wirklich gelöscht werden? Diese Aktion lässt sich nicht ";
			text += "rückgängig machen! Alle mit ihm verbundenen Daten, wie ";
			text += "z.B. das Einsatztagebuch werden mit entfernt!";
		}
		ConfirmationWindow win = new ConfirmationWindow("Wirklich löschen?",
				text, true, this);
		getApplication().getMainWindow().addWindow(win);
	}
	
}