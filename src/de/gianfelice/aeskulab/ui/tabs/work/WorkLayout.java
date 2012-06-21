package de.gianfelice.aeskulab.ui.tabs.work;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.risto.stepper.IntStepper;

import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.WorkContainer;
import de.gianfelice.aeskulab.data.entities.Log;
import de.gianfelice.aeskulab.data.entities.Work;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A layout to display, edit and create {@link Work} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Work
 */
public class WorkLayout extends VerticalLayout implements ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The caption. */
	private TextField txtCaption;
	
	/** The place. */
	private TextField txtPlace;
	
	/** The date. */
	private DateField txtDate;
	
	/** The number of affected persons. */
	private IntStepper intPersons;
	
	/** The caller. */
	private TextField txtCaller;
	
	/** The number of the protocol. */
	private IntStepper intProtocol;
	
	/** The current active object. */
	private Work current;
	
	/** The container with all {@link Work} objects. */
	private WorkContainer container;
	
	/** The main form layout. */
	private GridLayout grid;
	
	/** A layout which contains the assigned logs. */
	private VerticalLayout verLog;
	
	/** The remarks. */
	private TextArea txtRemarks;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and sets the container.
	 * 
	 * @param container The container
	 */
	public WorkLayout(WorkContainer container) {
		this.container = container;
		grid = new GridLayout(4, 6);
		grid.setMargin(true);
		grid.setSpacing(true);
		grid.setWidth("100%");
		grid.setColumnExpandRatio(1, 1f);
		addComponent(grid);
		
		addCaption("Betreff");
		txtCaption = new TextField();
		txtCaption.setRequired(true);
		txtCaption.setWidth("100%");
		txtCaption.setTabIndex(1);
		grid.addComponent(txtCaption, 1, 0, 3, 0);
		
		addCaption("Ort");
		txtPlace = new TextField();
		txtPlace.setWidth("100%");
		txtPlace.setTabIndex(2);
		grid.addComponent(txtPlace, 1, 1, 3, 1);
		
		addCaption("Zeit");
		txtDate = new DateField();
		txtDate.setValue(new Date());
		txtDate.setWidth("100%");
		txtDate.setTabIndex(3);
		txtDate.setResolution(DateField.RESOLUTION_MIN);
		grid.addComponent(txtDate);
		
		addCaption("Anzahl Personen");
		intPersons = new IntStepper();
		intPersons.setValue(1);
		intPersons.setMinValue(1);
		intPersons.setTabIndex(4);
		grid.addComponent(intPersons);
		
		addCaption("Anrufer");
		txtCaller = new TextField();
		txtCaller.setWidth("100%");
		txtCaller.setTabIndex(5);
		grid.addComponent(txtCaller);
		
		addCaption("Nr. Protokoll");
		intProtocol = new IntStepper();
		intProtocol.setMinValue(1);
		intProtocol.setTabIndex(6);
		grid.addComponent(intProtocol);
		
		addCaption("Bemerkungen");
		txtRemarks = new TextArea();
		txtRemarks.setWidth("100%");
		grid.addComponent(txtRemarks, 1, 4, 3, 4);
		
		Button btnSave = new Button("Speichern", this);
		grid.addComponent(btnSave, 0, 5, 3, 5);
		grid.setComponentAlignment(btnSave, Alignment.MIDDLE_RIGHT);
		
		addComponent(new Label("<hr />", Label.CONTENT_XHTML));
		verLog = new VerticalLayout();
		verLog.setMargin(true);
		verLog.setSpacing(true);
		verLog.setWidth("100%");
		addComponent(verLog);

		setEnabled(false);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Adds a caption for a field to the next free slot of the layout.
	 * 
	 * @param caption The caption
	 */
	private void addCaption(String caption) {
		Label lblCaption = new Label(caption);
		lblCaption.setSizeUndefined();
		grid.addComponent(lblCaption);
		grid.setComponentAlignment(lblCaption, Alignment.MIDDLE_RIGHT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		String caption = (String) txtCaption.getValue();
		if (caption == null || caption.equals("")) {
			getWindow().showNotification("Bitte geben Sie einen Betreff an!",
					Notification.TYPE_WARNING_MESSAGE);
			return;
		}
		if (current == null) current = new Work(caption);
		current.setPlace((String) txtPlace.getValue());
		current.setTime((Date) txtDate.getValue());
		current.setNumberOfPersons((Integer) intPersons.getValue());
		current.setCaller((String) txtCaller.getValue());
		current.setNumberOfProtocol((Integer) intProtocol.getValue());
		current.setRemarks((String) txtRemarks.getValue());
		if (container.containsId(current.getId())) {
			Item item = container.getItem(current.getId());
			item.getItemProperty("caption").setValue(caption);
		} else {
			container.addWork(current);
		}
	}
	
	/**
	 * Sets the current active object.
	 * 
	 * @param current The new object
	 */
	public void setCurrent(Work current) {
		this.current = current;
		txtCaption.focus();
		verLog.removeAllComponents();
		
		String caption = "";
		String place = "";
		Date date = new Date();
		int persons = 1;
		String caller = "";
		int protocol = 0;
		String remarks = "";
		
		if (current != null) {
			if (current.getCaption() != null) caption = current.getCaption();
			if (current.getPlace() != null) place = current.getPlace();
			if (current.getTime() != null) date = current.getTime();
			persons = current.getNumberOfPersons();
			if (current.getCaller() != null) caller = current.getCaller();
			protocol = current.getNumberOfProtocol();
			if (current.getRemarks() != null) remarks = current.getRemarks();
			
			Log log = current.getLog();
			Label lblHeadline = new Label(log.getText());
			lblHeadline.setStyleName(Reindeer.LABEL_H2);
			verLog.addComponent(lblHeadline);
			
			String query = "SELECT l FROM Log l WHERE l.parent = :parent";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("parent", log);
			List<Log> logs = DBManager.getCommunicator().list(query, params);
			for (Log l : logs) verLog.addComponent(new Label(l.getText()));
		}

		txtCaption.setValue(caption);
		txtPlace.setValue(place);
		txtDate.setValue(date);
		intPersons.setValue(persons);
		txtCaller.setValue(caller);
		intProtocol.setValue(protocol);
		txtRemarks.setValue(remarks);
		setEnabled(true);
	}
	
}