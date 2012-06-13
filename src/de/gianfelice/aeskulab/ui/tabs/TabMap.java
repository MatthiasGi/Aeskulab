package de.gianfelice.aeskulab.ui.tabs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.Application;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Place;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.system.utils.FileUtil;
import de.gianfelice.aeskulab.ui.components.ComPlace;
import de.gianfelice.aeskulab.ui.components.ComPlace.DDGrid;
import de.gianfelice.aeskulab.ui.components.Unit;
import de.gianfelice.aeskulab.ui.components.UnitList;
import de.gianfelice.aeskulab.ui.tabs.map.HelperLayout;
import de.gianfelice.aeskulab.ui.tabs.map.PlaceLayout;
import de.gianfelice.aeskulab.ui.tabs.map.RemoveHandler;
import de.gianfelice.aeskulab.ui.tabs.map.SquadLayout;
import de.gianfelice.aeskulab.ui.tabs.map.VehicleLayout;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.DDAccordion;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.DDAbsoluteLayout.AbsoluteLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * This tab offers a tactical overview.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 */
public class TabMap extends Tab implements Receiver, FailedListener,
		FinishedListener, ProgressListener, StartedListener, DropHandler {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** Content panel. */
	private HorizontalSplitPanel horPanel;
	
	/** Accordion. */
	private DDAccordion acc;
	
	/** Toolbar. */
	private HorizontalLayout horToolbar;
	
	/** Map-Layout. */
	private DDAbsoluteLayout ddAbsMap;
	
	/** Replace map. */
	private Upload upload;
	
	/** Upload progress. */
	private ProgressIndicator progress;
	
	/** Cancel upload. */
	private Button btnCancel;
	
	/** Map. */
	private Embedded map;
	
	/** Date format for clock. */
	private SimpleDateFormat format;
	
	/** Squad container. */
	private SquadLayout squads;
	
	/** Vehicle container. */
	private VehicleLayout vehicles;

	/** Helper container. */
	private HelperLayout helpers;
	
	/** Place container. */
	private PlaceLayout places;
	
	/** Popup with information. */
	private Window popup;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the tab.
	 */
	public TabMap() {
		super();
		popup = new Window("Einheiteninformationen");
		popup.setWidth("600px");
		popup.setHeight("300px");
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentContainer getContentContainer() {
		format = new SimpleDateFormat("HH:mm:ss");
		squads = new SquadLayout(this);
		vehicles = new VehicleLayout(this);
		helpers = new HelperLayout(this);
		places = new PlaceLayout(this);
		
		horPanel = new HorizontalSplitPanel();
		horPanel.setSplitPosition(70);
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSizeFull();
		horPanel.addComponent(verLayout);
		acc = new DDAccordion();
		acc.setSizeFull();
		acc.addListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				DDVerticalLayout dd = (DDVerticalLayout) acc.getSelectedTab();
				dd.setDragMode(LayoutDragMode.NONE);
				dd.setDragMode(LayoutDragMode.CLONE);
			}
		});
		acc.setDropHandler(new RemoveHandler());
		horPanel.addComponent(acc);
		
		horToolbar = new HorizontalLayout();
		horToolbar.setWidth("100%");
		horToolbar.setMargin(true);
		horToolbar.setSpacing(true);
		horToolbar.setStyleName(Reindeer.LAYOUT_BLUE);
		verLayout.addComponent(horToolbar);
		Panel panel = new Panel();
		panel.setSizeFull();
		verLayout.addComponent(panel);
		verLayout.setExpandRatio(panel, 1.0f);
		ddAbsMap = new DDAbsoluteLayout();
		ddAbsMap.setDragMode(LayoutDragMode.CLONE);
		ddAbsMap.setDropHandler(this);
		panel.setContent(ddAbsMap);
		
		upload = new Upload();
		upload.setButtonCaption("Karte ersetzen");
		upload.setReceiver(this);
		upload.setImmediate(true);
		upload.addListener((StartedListener) this);
		upload.addListener((ProgressListener) this);
		upload.addListener((FinishedListener) this);
		upload.addListener((FailedListener) this);
		horToolbar.addComponent(upload);
		
		progress = new ProgressIndicator();
		progress.setVisible(false);
		progress.setPollingInterval(500);
		horToolbar.addComponent(progress);
		horToolbar.setComponentAlignment(progress, Alignment.MIDDLE_LEFT);
		btnCancel = new Button("Abbrechen");
		btnCancel.setStyleName(Reindeer.BUTTON_SMALL);
		btnCancel.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				upload.interruptUpload();
			}
		});
		btnCancel.setVisible(false);
		horToolbar.addComponent(btnCancel);
		horToolbar.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		horToolbar.setExpandRatio(btnCancel, 1.0f);
		
		final Label lblClock = new Label(format.format(new Date()));
		lblClock.setStyleName(Reindeer.LABEL_H1);
		lblClock.setSizeUndefined();
		Refresher ref = new Refresher();
		ref.setRefreshInterval(1000);
		ref.addListener(new RefreshListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void refresh(Refresher source) {
				lblClock.setValue(format.format(new Date()));
			}
		});
		horToolbar.addComponent(lblClock);
		horToolbar.addComponent(ref);
		
		map = new Embedded();
		ddAbsMap.addComponent(map);
		
		return horPanel;
	}
	
	/**
	 * Shows the popup window with the given content, if it isn't displayed yet,
	 * and sets the position, if needed.
	 * 
	 * @param content The content for the window
	 * @param left    The left coordinate
	 * @param top     The top coordinate
	 */
	public void showWindow(ComponentContainer content, int left, int top) {
		popup.setContent(content);
		Window win = getApplication().getMainWindow();
		if (!win.getChildWindows().contains(popup)) {
			win.addWindow(popup);
			popup.setPositionX(left);
			popup.setPositionY(top);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		
		Application app = getApplication();
		acc.addTab(squads, "Trupps",
				new ClassResource("res/16/group.png", app));
		acc.addTab(vehicles, "Fahrzeuge",
				new ClassResource("res/16/lorry.png", app));
		acc.addTab(helpers, "Helfer",
				new ClassResource("res/16/user.png", app));
		acc.addTab(places, "Orte",
				new ClassResource("res/16/map.png", app));
		updateMap();
		
		List<Squad> lstS = DBManager.getCommunicator().list(Squad.class);
		for (Squad s : lstS) {
			if (s.getLeft() == null || s.getTop() == null) continue;
			Unit u = new Unit(this, s);
			ddAbsMap.addComponent(u);
			ComponentPosition pos = ddAbsMap.getPosition(u);
			pos.setLeft((float) s.getLeft(), UNITS_PIXELS);
			pos.setTop((float) s.getTop(), UNITS_PIXELS);
		}
		List<Vehicle> lstV = DBManager.getCommunicator().list(Vehicle.class);
		for (Vehicle v : lstV) {
			if (v.getLeft() == null || v.getTop() == null) continue;
			Unit u = new Unit(this, v);
			ddAbsMap.addComponent(u);
			ComponentPosition pos = ddAbsMap.getPosition(u);
			pos.setLeft((float) v.getLeft(), UNITS_PIXELS);
			pos.setTop((float) v.getTop(), UNITS_PIXELS);
		}
		List<Helper> lstH = DBManager.getCommunicator().list(Helper.class);
		for (Helper h : lstH) {
			if (h.getLeft() == null || h.getTop() == null) continue;
			Unit u = new Unit(this, h);
			ddAbsMap.addComponent(u);
			ComponentPosition pos = ddAbsMap.getPosition(u);
			pos.setLeft((float) h.getLeft(), UNITS_PIXELS);
			pos.setTop((float) h.getTop(), UNITS_PIXELS);
		}
		List<Place> lstP = DBManager.getCommunicator().list(Place.class);
		for (Place p : lstP) {
			if (p.getLeft() == null || p.getTop() == null) continue;
			ComPlace c = new ComPlace(this, p);
			for (Squad s : p.getSquads()) c.addUnit(new Unit(this, s));
			for (Vehicle v : p.getVehicles()) c.addUnit(new Unit(this, v));
			for (Helper h : p.getHelpers()) c.addUnit(new Unit(this, h));
			ddAbsMap.addComponent(c);
			ComponentPosition pos = ddAbsMap.getPosition(c);
			pos.setLeft((float) p.getLeft(), UNITS_PIXELS);
			pos.setTop((float) p.getTop(), UNITS_PIXELS);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public OutputStream receiveUpload(String filename, String mimeType) {
		String ext = filename.substring(filename.lastIndexOf('.'));
		String[] exts = new String[]{".jpg", ".jpeg", ".JPG", ".JPEG"};
		if (!Arrays.asList(exts).contains(ext)) {
			getApplication().getMainWindow().showNotification("Es können " +
					"leider nur JPEGs (*.jpg, *.jpeg, *.JPG, *.JPEG) " +
					"angenommen werden!", Notification.TYPE_ERROR_MESSAGE);
			upload.interruptUpload();
		}
		try {
			return new FileOutputStream(FileUtil.getFile("map.jpg"));
		} catch (Exception e) {
			getApplication().getMainWindow().showNotification("Leider hat " +
					"die Anwendung keinen Zugriff auf das Arbeitsverzeichnis!",
					Notification.TYPE_ERROR_MESSAGE);
			upload.interruptUpload();
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadStarted(StartedEvent event) {
		upload.setVisible(false);
		progress.setValue(0f);
		progress.setVisible(true);
		btnCancel.setVisible(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateProgress(long readBytes, long contentLength) {
		progress.setValue(new Float(readBytes / (float) contentLength));
	}
	
	/**
	 * Updates the map.
	 */
	private void updateMap() {
		Application app = getApplication();
		try {
			File img = FileUtil.getFile("map.jpg");
			BufferedImage buffImg = ImageIO.read(img);
			float width = new Float(buffImg.getWidth());
			float height = new Float(buffImg.getHeight());
			map.setSource(new FileResource(FileUtil.getFile("map.jpg"), app));
			map.setWidth(width, UNITS_PIXELS);
			map.setHeight(height, UNITS_PIXELS);
			ddAbsMap.setWidth(width, UNITS_PIXELS);
			ddAbsMap.setHeight(height, UNITS_PIXELS);
		} catch (IOException e) {
			app.getMainWindow().showNotification(
					"Die Karte konnte nicht geladen werden!",
					Notification.TYPE_ERROR_MESSAGE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadFinished(FinishedEvent event) {
		upload.setVisible(true);
		progress.setVisible(false);
		btnCancel.setVisible(false);
		updateMap();
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadFailed(FailedEvent event) {
		getApplication().getMainWindow().showNotification(
				"Der Upload wurde abgebrochen!",
				Notification.TYPE_WARNING_MESSAGE);
	}

	/**
	 * {@inheritDoc}
	 */
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	/**
	 * {@inheritDoc}
	 */
	public void drop(DragAndDropEvent event) {
		LayoutBoundTransferable trans = (LayoutBoundTransferable)
				event.getTransferable();
		Component com = trans.getComponent();
		if (com instanceof UnitList) {
			addUnitList(event);
			return;
		}
		if (!(com instanceof Unit || com instanceof ComPlace)) return;
		if (trans.getSourceComponent() instanceof DDGrid
				&& com instanceof Unit) {
			DDGrid grid = (DDGrid) trans.getSourceComponent();
			grid.removeComponent(com);
			grid.recalculateLayout();
			Place p = grid.getPlace();
			Object o = ((Unit) com).getEntity();
			if (o instanceof Squad) {
				p.removeSquad((Squad) o);
			} else if (o instanceof Vehicle) {
				p.removeVehicle((Vehicle) o);
			} else if (o instanceof Helper) {
				p.removeHelper((Helper) o);
			}
			
			ddAbsMap.addComponent(com);
		}
		
		AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails)
				event.getTargetDetails();
		int left = details.getRelativeLeft();
		int top = details.getRelativeTop();
		ComponentPosition pos = ddAbsMap.getPosition(com);
		
		pos.setLeft((float) left, UNITS_PIXELS);
		pos.setTop((float) top, UNITS_PIXELS);
		Object obj;
		if (com instanceof Unit) {
			obj = ((Unit) com).getEntity();
		} else {
			obj = ((ComPlace) com).getPlace();
		}
		savePosition(obj, left, top);
	}

	/**
	 * Adds a {@code UnitList}-object to the layout.
	 * 
	 * @param  event The drop-event
	 */
	private void addUnitList(DragAndDropEvent event) {
		LayoutBoundTransferable trans = (LayoutBoundTransferable)
				event.getTransferable();
		UnitList com = (UnitList) trans.getComponent();
		Object entity = com.getEntity();
		AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails)
				event.getTargetDetails();
		Component u = null;
		
		if (entity instanceof Place && !onMap((Place) entity)) {
			u = new ComPlace(this, (Place) entity);
		} else if (!onMap(entity)) {
			u = new Unit(this, entity);
		} else {
			return;
		}

		ddAbsMap.addComponent(u);
		ComponentPosition pos = ddAbsMap.getPosition(u);
		int left = details.getRelativeLeft();
		int top = details.getRelativeTop();
		pos.setLeft((float) left, UNITS_PIXELS);
		pos.setTop((float) top, UNITS_PIXELS);
		savePosition(entity, left, top);
	}
	
	/**
	 * Saves the position of the given object.
	 * 
	 * @param obj  The object
	 * @param left The left coordinate
	 * @param top  The top coordinate
	 */
	public void savePosition(Object obj, Integer left, Integer top) {
		if (obj instanceof Squad) {
			((Squad) obj).setLeft(left);
			((Squad) obj).setTop(top);
		} else if (obj instanceof Vehicle) {
			((Vehicle) obj).setLeft(left);
			((Vehicle) obj).setTop(top);
		} else if (obj instanceof Helper) {
			((Helper) obj).setLeft(left);
			((Helper) obj).setTop(top);
		} else if (obj instanceof Place) {
			((Place) obj).setLeft(left);
			((Place) obj).setTop(top);
		}
	}
	
	/**
	 * Checks, whether the given squad is already on the map.
	 * 
	 * @param  squad The squad
	 * @return       If the squad is already on the map.
	 */
	public boolean onMap(Squad squad) {
		List<Place> lst = DBManager.getCommunicator().list(Place.class);
		for (Place p : lst) if (p.getSquads().contains(squad)) return true;
		return (squad.getTop() != null && squad.getLeft() != null); 
	}
	
	/**
	 * Checks, whether the given vehicle is already on the map.
	 * 
	 * @param  vehicle The vehicle
	 * @return         If the vehicle is already on the map.
	 */
	public boolean onMap(Vehicle vehicle) {
		List<Place> lst = DBManager.getCommunicator().list(Place.class);
		for (Place p : lst) if (p.getVehicles().contains(vehicle)) return true;
		return (vehicle.getTop() != null && vehicle.getLeft() != null);
	}
	
	/**
	 * Checks, whether the given helper is already on the map.
	 * 
	 * @param  helper The helper
	 * @return        If the helper is already on the map.
	 */
	public boolean onMap(Helper helper) {
		List<Place> lst = DBManager.getCommunicator().list(Place.class);
		for (Place p : lst) if (p.getHelpers().contains(helper)) return true;
		return (helper.getTop() != null && helper.getLeft() != null);
	}
	
	/**
	 * Checks, whether the given place is already on the map.
	 * 
	 * @param  place The place
	 * @return       If the place is already on the map.
	 */
	public boolean onMap(Place place) {
		return (place.getTop() != null && place.getLeft() != null);
	}
	
	/**
	 * Checks, whether the given object is already on the map.
	 * 
	 * @param  obj The object
	 * @return     If the object is already on the map.
	 */
	public boolean onMap(Object obj) {
		if (obj instanceof Squad   && onMap((Squad) obj))   return true;
		if (obj instanceof Vehicle && onMap((Vehicle) obj)) return true;
		if (obj instanceof Helper  && onMap((Helper) obj))  return true;
		if (obj instanceof Place   && onMap((Place) obj))   return true;
		return false;
	}

	/**
	 * Updates the state of a squad.
	 * 
	 * @param squad The squad
	 * @param state The state
	 */
	public void updateState(Squad squad, int state) {
		Iterator<Component> it = acc.getComponentIterator();
		while (it.hasNext()) {
			Component com = it.next();
			if (!(com instanceof DDVerticalLayout)) continue;
			Iterator<Component> it2 =
					((DDVerticalLayout) com).getComponentIterator();
			
			while (it2.hasNext()) {
				Component com2 = it2.next();
				if (!(com2 instanceof UnitList)) continue;
				UnitList u = (UnitList) com2;
				Object o = u.getEntity();
				if (squad.equals(o)) u.setState(state);
			}
		}
	}

	/**
	 * Updates the state of a vehicle.
	 * 
	 * @param vehicle The vehicle
	 * @param state   The state
	 */
	public void updateState(Vehicle vehicle, int state) {
		Iterator<Component> it = acc.getComponentIterator();
		while (it.hasNext()) {
			Component com = it.next();
			if (!(com instanceof DDVerticalLayout)) continue;
			Iterator<Component> it2 =
					((DDVerticalLayout) com).getComponentIterator();
			
			while (it2.hasNext()) {
				Component com2 = it2.next();
				if (!(com2 instanceof UnitList)) continue;
				UnitList u = (UnitList) com2;
				Object o = u.getEntity();
				if (vehicle.equals(o)) u.setState(state);
			}
		}
	}

}