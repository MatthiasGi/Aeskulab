package de.gianfelice.aeskulab.ui.tabs;

import java.util.Collection;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.UnitContainer;
import de.gianfelice.aeskulab.data.container.WorkContainer;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.data.entities.Work;
import de.gianfelice.aeskulab.ui.tabs.work.HelperLayout;
import de.gianfelice.aeskulab.ui.tabs.work.UnitLayout;
import de.gianfelice.aeskulab.ui.tabs.work.WorkLayout;

/**
 * This tab manages and displays all orders.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class TabWork extends Tab implements DropHandler, ItemClickListener,
		ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A table with all objects. */
	private TreeTable tblWork;
	
	/** A container with all objects. */
	private WorkContainer works;
	
	/** A container with all units. */
	private UnitContainer units;
	
	/** A table with all units. */
	private Table tblUnits;
	
	/** A layout to display, modify and create objects. */
	private WorkLayout layoutWork;
	
	/** Create new objects. */
	private Button btnNew;
	
	/** Set work to done. */
	private Button btnReady;
	
	/** A layout with unit information. */
	private UnitLayout layoutUnit;
	
	/** A layout with helper information. */
	private HelperLayout layoutHelper;
	
	/** The current active work. */
	private Work work;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the tab.
	 */
	public TabWork() {
		super();
		this.work = null;
	}
	
	/**
	 * Creates the tab and sets a work overview.
	 * 
	 * @param work The work
	 */
	public TabWork(Work work) {
		super();
		this.work = work;
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentContainer getContentContainer() {
		
		// Container
		units = new UnitContainer();
		works = new WorkContainer();
		
		// Sidebar
		VerticalSplitPanel verSidebar = new VerticalSplitPanel();
		verSidebar.setSplitPosition(60f);
		
		// Work overview
		VerticalLayout verWork = new VerticalLayout();
		verWork.setSizeFull();
		verSidebar.addComponent(verWork);
		tblWork = new TreeTable();
		tblWork.setContainerDataSource(works);
		tblWork.setVisibleColumns(WorkContainer.NATURAL_COLS);
		tblWork.setColumnHeaders(WorkContainer.COL_HEADERS);
		tblWork.setColumnWidth("state", 40);
		tblWork.setColumnAlignment("state", Table.ALIGN_CENTER);
		tblWork.setSizeFull();
		tblWork.setDragMode(TableDragMode.ROW);
		tblWork.setDropHandler(this);
		tblWork.setSelectable(true);
		tblWork.addListener(this);
		Collection<?> itemIds = tblWork.getItemIds();
		for (Object id : itemIds) {
			if (tblWork.areChildrenAllowed(id)) tblWork.setCollapsed(id, false);
		}
		verWork.addComponent(tblWork);
		verWork.setExpandRatio(tblWork, 1f);
		HorizontalLayout horWork = new HorizontalLayout();
		horWork.setMargin(true);
		horWork.setWidth("100%");
		horWork.setStyleName(Reindeer.LAYOUT_BLUE);
		CheckBox checkWork = new CheckBox("Abgearbeitete Einsätze anzeigen");
		checkWork.setWidth("100%");
		checkWork.setImmediate(true);
		checkWork.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				works.setDoneWorkVisible((Boolean)
						event.getProperty().getValue());
			}
		});
		horWork.addComponent(checkWork);
		verWork.addComponent(horWork);
		
		// Unit Overview
		VerticalLayout verUnits = new VerticalLayout();
		verUnits.setSizeFull();
		verSidebar.addComponent(verUnits);
		tblUnits = new Table();
		tblUnits.setContainerDataSource(units);
		tblUnits.setVisibleColumns(UnitContainer.NATURAL_COLS);
		tblUnits.setColumnHeaders(UnitContainer.COL_HEADERS);
		tblUnits.setColumnWidth("helpers", 40);
		tblUnits.setColumnAlignment("helpers", Table.ALIGN_CENTER);
		tblUnits.setColumnWidth("state", 40);
		tblUnits.setColumnAlignment("state", Table.ALIGN_CENTER);
		tblUnits.setSizeFull();
		tblUnits.setDragMode(TableDragMode.ROW);
		tblUnits.setDropHandler(this);
		tblUnits.setSelectable(true);
		tblUnits.addListener(this);
		verUnits.addComponent(tblUnits);
		verUnits.setExpandRatio(tblUnits, 1f);
		HorizontalLayout horUnits = new HorizontalLayout();
		horUnits.setMargin(true);
		horUnits.setWidth("100%");
		horUnits.setStyleName(Reindeer.LAYOUT_BLUE);
		CheckBox checkSquads = new CheckBox("Trupps");
		checkSquads.setWidth("100%");
		checkSquads.setImmediate(true);
		checkSquads.setValue(true);
		checkSquads.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				units.setSquadsVisible((Boolean)
						event.getProperty().getValue());
			}
		});
		horUnits.addComponent(checkSquads);
		CheckBox checkVehicles = new CheckBox("Fahrzeuge");
		checkVehicles.setWidth("100%");
		checkVehicles.setImmediate(true);
		checkVehicles.setValue(true);
		checkVehicles.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				units.setVehicleVisible((Boolean)
						event.getProperty().getValue());
			}
		});
		horUnits.addComponent(checkVehicles);
		CheckBox checkUnavailable = new CheckBox("Unverfügbare");
		checkUnavailable.setWidth("100%");
		checkUnavailable.setImmediate(true);
		checkUnavailable.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				units.setUnavailableVisible((Boolean)
						event.getProperty().getValue());
			}
		});
		horUnits.addComponent(checkUnavailable);
		verUnits.addComponent(horUnits);

		// Toolbar
		VerticalLayout verContent = new VerticalLayout();
		verContent.setSizeFull();
		HorizontalLayout horToolbar = new HorizontalLayout();
		horToolbar.setWidth("100%");
		horToolbar.setStyleName(Reindeer.LAYOUT_BLUE);
		horToolbar.setMargin(true);
		horToolbar.setSpacing(true);
		btnNew = new Button("Neuer Einsatz", this);
		horToolbar.addComponent(btnNew);
		btnReady = new Button("Einsatz abgeschlossen", this);
		horToolbar.addComponent(btnReady);
		horToolbar.setExpandRatio(btnReady, 1f);
		verContent.addComponent(horToolbar);
		
		// Informations
		HorizontalSplitPanel horInfo = new HorizontalSplitPanel();
		horInfo.setSplitPosition(60f);
		verContent.addComponent(horInfo);
		verContent.setExpandRatio(horInfo, 1f);
		
		// Work
		layoutWork = new WorkLayout(works);
		horInfo.addComponent(layoutWork);
		VerticalSplitPanel verUnitInfo = new VerticalSplitPanel();
		verUnitInfo.setSplitPosition(70f);
		layoutUnit = new UnitLayout(works, units);
		Panel panelUnits = new Panel(layoutUnit);
		panelUnits.setSizeFull();
		panelUnits.setStyleName(Reindeer.PANEL_LIGHT);
		verUnitInfo.addComponent(panelUnits);
		layoutHelper = new HelperLayout();
		verUnitInfo.addComponent(layoutHelper);
		horInfo.addComponent(verUnitInfo);
		
		// Create Container
		HorizontalSplitPanel horSplit = new HorizontalSplitPanel();
		horSplit.setSplitPosition(30f);
		horSplit.addComponent(verSidebar);
		horSplit.addComponent(verContent);
		return horSplit;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drop(DragAndDropEvent event) {
		DataBoundTransferable t = (DataBoundTransferable)
				event.getTransferable();
		IndexedContainer con = (IndexedContainer) t.getSourceContainer();
		Object id = t.getItemId();
		AbstractSelectTargetDetails drop = ((AbstractSelectTargetDetails)
				event.getTargetDetails());
		Table target = (Table) drop.getTarget();
		Object tId = drop.getItemIdOver();
		
		if (con == units && target == tblWork) {
			
			// Unit -> Work
			Work work = WorkContainer.getWork(tId);
			Object unit = UnitContainer.getUnit(id);
			if (!works.addUnit(unit, work)) return;

			if (unit instanceof Squad) {
				Squad squad = (Squad) unit;
				work.addSquad(squad);
				squad.setState(3);
			} else if (unit instanceof Vehicle) {
				Vehicle vehicle = (Vehicle) unit;
				work.addVehicle(vehicle);
				vehicle.setState(3);
			} else {
				return;
			}

			units.getItem(id).getItemProperty("state").setValue(3);
			works.getItem(id).getItemProperty("state").setValue(3);

		} else if (con == works && target == tblUnits) {
			
			// Work -> Units
			Object unit = UnitContainer.getUnit(id);
			if (unit == null) return;
			Work work = WorkContainer.getWork(works.getParent(id));
			works.removeItem(id);
			boolean visible = units.areUnavailableVisible();
			units.setUnavailableVisible(true);
			units.getItem(id).getItemProperty("state").setValue(1);
			if (unit instanceof Squad) {
				Squad squad = (Squad) unit;
				squad.setState(1);
				if (work != null) work.removeSquad(squad);
			} else if (unit instanceof Vehicle) {
				Vehicle vehicle = (Vehicle) unit;
				vehicle.setState(1);
				if (vehicle != null) work.removeVehicle(vehicle);
			}
			if (!visible) units.setUnavailableVisible(false);
			
		}

		layoutUnit.update();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		Object com = event.getSource();
		if (com == tblWork) {
			Work work = WorkContainer.getWork(event.getItemId());
			layoutWork.setCurrent(work);
			layoutUnit.setCurrent(work);
		} else if (com == tblUnits) {
			Object unit = UnitContainer.getUnit(event.getItemId());
			if (unit instanceof Squad) {
				layoutHelper.setHelper(((Squad) unit).getHelpers());
			} else if (unit instanceof Vehicle) {
				layoutHelper.setHelper(((Vehicle) unit).getHelpers());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		Button btn = event.getButton();
		
		if (btn == btnReady) {
			Work work = WorkContainer.getWork(tblWork.getValue());
			if (work == null) return;
			List<Squad> squads = work.getSquads();
			for (Squad squad : squads) squad.setState(1);
			List<Vehicle> vehicles = work.getVehicles();
			for (Vehicle vehicle : vehicles) vehicle.setState(1);
			work.setReady(true);
			works.getItem(work.getId()).getItemProperty("ready").setValue(true);
			works.setDoneWorkVisible(works.isReadyVisible());
			layoutWork.setCurrent(null);
			layoutWork.setEnabled(false);
			tblWork.select(null);
		}

		tblWork.select(null);
		layoutWork.setCurrent(null);
		layoutUnit.setCurrent(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		Application app = getApplication();
		btnNew.setIcon(new ClassResource("res/16/page_white_add.png", app));
		btnReady.setIcon(new ClassResource("res/16/briefcase.png", app));

		if (work != null) {
			layoutWork.setCurrent(work);
			layoutUnit.setCurrent(work);
			tblWork.select(work.getId());
		}
	}

}