package de.gianfelice.aeskulab.ui.tabs;

import java.util.Collection;

import com.vaadin.Application;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.LogContainer;
import de.gianfelice.aeskulab.data.entities.Log;
import de.gianfelice.aeskulab.data.table.DateColumnGenerator;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * This tab manages all events, that occur and displays them.
 * 
 * @author  Matthias Gianfelice
 * @version 0.0.1
 */
public class TabLog extends Tab implements ClickListener, TextChangeListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** The container with all logs. */
	private LogContainer container;
	
	/** A field to search for entries. */
	private TextField txtSearch;
	
	/** Clear the search. */
	private Button btnClear;
	
	/** A text area for new entries. */
	private TextArea txtNew;
	
	/** Add new log. */
	private Button btnNew;
	
	/** Print all entries. */
	private Button btnPrint;
	
	/** A table with all logs. */
	private TreeTable tblLog;

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentContainer getContentContainer() {
		HorizontalSplitPanel split = new HorizontalSplitPanel();
		
		tblLog = new TreeTable();
		tblLog.setSelectable(true);
		tblLog.setSizeFull();
		container = new LogContainer();
		tblLog.setContainerDataSource(container);
		Collection<?> ids = container.getItemIds();
		for (Object id : ids) tblLog.setCollapsed(id, false);
		tblLog.setVisibleColumns(LogContainer.NATURAL_COLS);
		tblLog.setColumnHeaders(LogContainer.COL_HEADERS);
		tblLog.setColumnWidth("date", 110);
		tblLog.addGeneratedColumn("date", new DateColumnGenerator());
		split.addComponent(tblLog);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSizeFull();
		split.addComponent(verLayout);
		
		HorizontalLayout horSearch = new HorizontalLayout();
		horSearch.setMargin(true);
		horSearch.setSpacing(true);
		horSearch.setWidth("100%");
		verLayout.addComponent(horSearch);
		horSearch.setStyleName(Reindeer.LAYOUT_BLUE);
		
		txtSearch = new TextField();
		txtSearch.setImmediate(true);
		txtSearch.setWidth("100%");
		txtSearch.setInputPrompt("Suche");
		txtSearch.addListener(this);
		horSearch.addComponent(txtSearch);
		horSearch.setExpandRatio(txtSearch, 1.0f);
		btnClear = new Button();
		btnClear.addListener(this);
		horSearch.addComponent(btnClear);
		
		VerticalLayout verNew = new VerticalLayout();
		verNew.setMargin(true);
		verNew.setSpacing(true);
		verNew.setSizeFull();
		verNew.setStyleName(Reindeer.LAYOUT_WHITE);
		verNew.addComponent(new Label("Hier kann ein neuer Eintrag manuell " +
				"hinzugefügt werden. Soll eine Bemerkung angelegt werden, " +
				"bitte zunächst links den entsprechenden Beitrag auswählen. " +
				"Es kann keine Bemerkung zu einer Bemerkung erstellt werden."));
		verLayout.addComponent(verNew);
		verLayout.setExpandRatio(verNew, 1.0f);
		
		HorizontalLayout horNew = new HorizontalLayout();
		horNew.setWidth("100%");
		horNew.setSpacing(true);
		verNew.addComponent(horNew);
		verNew.setExpandRatio(horNew, 1.0f);
		txtNew = new TextArea();
		txtNew.setWidth("100%");
		txtNew.setInputPrompt("Neuer Eintrag");
		horNew.addComponent(txtNew);
		horNew.setExpandRatio(txtNew, 1.0f);
		btnNew = new Button();
		btnNew.addListener(this);
		horNew.addComponent(btnNew);
		
		HorizontalLayout horPrint = new HorizontalLayout();
		horPrint.setWidth("100%");
		horPrint.setStyleName(Reindeer.LAYOUT_BLUE);
		horPrint.setMargin(true);
		horPrint.setSpacing(true);
		verLayout.addComponent(horPrint);
		
		Label lblPrint = new Label("Die Software bietet die " +
				"Möglichkeit, die aktuelle Übersicht herunterzuladen. Beim " +
				"Klick auf den nebenstehenden Knopf wird eine Textdatei mit " +
				"allen Einträgen generiert und steht nach dieser " +
				"Vorbereitung zur Verfügung.");
		lblPrint.setEnabled(false);
		lblPrint.setWidth("100%");
		horPrint.addComponent(lblPrint);
		horPrint.setExpandRatio(lblPrint, 1.0f);
		btnPrint = new Button("Auszug erstellen");
		btnPrint.setDisableOnClick(true);
		btnPrint.addListener(this);
		btnPrint.setEnabled(false);
		horPrint.addComponent(btnPrint);
		horPrint.setComponentAlignment(btnPrint, Alignment.MIDDLE_LEFT);
		
		return split;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		Application app = getApplication();
		btnClear.setIcon(new ClassResource("res/16/cancel.png", app));
		btnNew.setIcon(new ClassResource("res/16/add.png", app));
		btnPrint.setIcon(new ClassResource("res/16/report_go.png", app));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void buttonClick(ClickEvent event) {		
		Button btn = event.getButton();
		if (btn == btnClear) {
			txtSearch.setValue("");
			container.removeAllContainerFilters();
		} else if (btn == btnNew) {
			String text = (String) txtNew.getValue();
			if (text == null || text.length() < 3) {
				getApplication().getMainWindow().showNotification(
						"Bitte geben Sie einen gültigen Text an!",
						Notification.TYPE_ERROR_MESSAGE);
				return;
			}
			Log log = new Log(text);
			
			Long id = (Long) tblLog.getValue();
			if (id != null) {
				Log parent = DBManager.getCommunicator().find(Log.class, id);
				if (parent.getParent() != null) {
					getApplication().getMainWindow().showNotification(
							"Es kann keine Bemerkung zu einer Bemerkung " +
							"abgegeben werden!",
							Notification.TYPE_ERROR_MESSAGE);
					DBManager.getCommunicator().delete(log);
					return;
				}
				log.setParent(parent);
			}
			container.addLog(log);
			txtNew.setValue("");
			
			if (id != null) tblLog.setCollapsed(id, false);
		} else if (btn == btnPrint) {
			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void textChange(TextChangeEvent event) {
		container.removeAllContainerFilters();
		String text = event.getText();
		if (text.length() < 3) return;
		container.addContainerFilter("text", text, true, false);
	}
	
}