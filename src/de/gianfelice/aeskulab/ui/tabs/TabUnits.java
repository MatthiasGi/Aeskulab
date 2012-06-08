package de.gianfelice.aeskulab.ui.tabs;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.HelperContainer;
import de.gianfelice.aeskulab.data.container.PlaceContainer;
import de.gianfelice.aeskulab.data.container.SquadContainer;
import de.gianfelice.aeskulab.data.container.VehicleContainer;
import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.data.table.BooleanColumnGenerator;
import de.gianfelice.aeskulab.data.table.ListColumnGenerator;
import de.gianfelice.aeskulab.data.table.NotNullColumnGenerator;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.ui.tabs.units.HelperSidebar;
import de.gianfelice.aeskulab.ui.tabs.units.PlaceSidebar;
import de.gianfelice.aeskulab.ui.tabs.units.SquadSidebar;
import de.gianfelice.aeskulab.ui.tabs.units.ToolBar;
import de.gianfelice.aeskulab.ui.tabs.units.VehicleSidebar;

/**
 * This tab is used to manage units.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class TabUnits extends Tab implements DropHandler {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A container with all helpers. */
	private HelperContainer containerHelper;
	
	/** A container with all places. */
	private PlaceContainer containerPlace;
	
	/** A container with all squads. */
	private SquadContainer containerSquad;
	
	/** A container with all vehicles. */
	private VehicleContainer containerVehicle;
	
	/** A panel, which contains the current sidebar. */
	private Panel sidebar;
	
	/** A table for all helpers. */
	private Table tblHelper;
	
	/** A table for all places. */
	private Table tblPlace;
	
	/** A table for all squads. */
	private TreeTable tblSquad;
	
	/** A table for all vehicles. */
	private TreeTable tblVehicle;

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public ComponentContainer getContentContainer() {
		containerHelper = new HelperContainer();
		containerPlace = new PlaceContainer();
		containerSquad = new SquadContainer();
		containerVehicle = new VehicleContainer();
		
		HorizontalSplitPanel horSplit = new HorizontalSplitPanel();
		horSplit.setSplitPosition(70);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSizeFull();
		horSplit.addComponent(verLayout);
		verLayout.addComponent(new ToolBar(this));
		sidebar = new Panel();
		sidebar.setStyleName(Reindeer.PANEL_LIGHT);
		horSplit.addComponent(sidebar);
		
		HorizontalSplitPanel horEntities = new HorizontalSplitPanel();
		verLayout.addComponent(horEntities);
		verLayout.setExpandRatio(horEntities, 1.0f);
		
		VerticalSplitPanel verLeft = new VerticalSplitPanel();
		horEntities.addComponent(verLeft);
		
		tblSquad = new TreeTable();
		tblSquad.setSizeFull();
		tblSquad.setContainerDataSource(containerSquad);
		tblSquad.setVisibleColumns(SquadContainer.NATURAL_COLS);
		tblSquad.setColumnHeaders(SquadContainer.COL_HEADERS);
		tblSquad.setSelectable(true);
		tblSquad.addListener(new ItemClickListener() {
			private static final long serialVersionUID = 1L;
			
			public void itemClick(ItemClickEvent event) {
				Squad s = SquadContainer.getSquad(event.getItem());
				if (s == null) return;
				SquadSidebar squad = new SquadSidebar(containerSquad);
				setSidebar(squad);
				squad.setItem(s);
			}
		});
		tblSquad.setDragMode(TableDragMode.ROW);
		tblSquad.setDropHandler(this);
		verLeft.addComponent(tblSquad);
		
		tblVehicle = new TreeTable();
		tblVehicle.setSizeFull();
		tblVehicle.setContainerDataSource(containerVehicle);
		tblVehicle.setVisibleColumns(VehicleContainer.NATURAL_COLS);
		tblVehicle.setColumnHeaders(VehicleContainer.COL_HEADERS);
		tblVehicle.setSelectable(true);
		tblVehicle.addListener(new ItemClickListener() {
			private static final long serialVersionUID = 1L;

			public void itemClick(ItemClickEvent event) {
				Vehicle v = VehicleContainer.getVehicle(event.getItem());
				if (v == null) return;
				VehicleSidebar vehicle = new VehicleSidebar(containerVehicle);
				setSidebar(vehicle);
				vehicle.setItem(v);
			}
		});
		tblVehicle.setDragMode(TableDragMode.ROW);
		tblVehicle.setDropHandler(this);
		verLeft.addComponent(tblVehicle);
		
		VerticalSplitPanel verRight = new VerticalSplitPanel();
		verRight.setSplitPosition(70);
		horEntities.addComponent(verRight);
		
		tblHelper = new Table();
		tblHelper.setSizeFull();
		tblHelper.setContainerDataSource(containerHelper);
		tblHelper.setVisibleColumns(HelperContainer.NATURAL_COLS);
		tblHelper.setColumnHeaders(HelperContainer.COL_HEADERS);
		tblHelper.setColumnReorderingAllowed(true);
		tblHelper.setColumnCollapsingAllowed(true);
		tblHelper.setSelectable(true);
		tblHelper.setSortContainerPropertyId("lastName");
		tblHelper.setColumnCollapsed("image", true);
		tblHelper.addGeneratedColumn("qualifications",
				new ListColumnGenerator());
		tblHelper.addListener(new ItemClickListener() {

			private static final long serialVersionUID = 1L;

			public void itemClick(ItemClickEvent event) {
				HelperSidebar helper = new HelperSidebar(containerHelper);
				setSidebar(helper);
				helper.setItem(containerHelper.getItem(event.getItemId()));
			}
		});
		tblHelper.setDragMode(TableDragMode.ROW);
		tblHelper.setDropHandler(this);
		verRight.addComponent(tblHelper);
		
		tblPlace = new Table();
		tblPlace.setSizeFull();
		tblPlace.setContainerDataSource(containerPlace);
		tblPlace.setVisibleColumns(PlaceContainer.NATURAL_COLS);
		tblPlace.setColumnHeaders(PlaceContainer.COL_HEADERS);
		tblPlace.setSelectable(true);
		tblPlace.setSortContainerPropertyId("name");
		tblPlace.addListener(new ItemClickListener() {
			private static final long serialVersionUID = 1L;

			public void itemClick(ItemClickEvent event) {
				PlaceSidebar place = new PlaceSidebar(containerPlace);
				setSidebar(place);
				place.setItem(containerPlace.getItem(event.getItemId()));
			}
		});
		verRight.addComponent(tblPlace);
		
		return horSplit;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		Application app = getApplication();
		
		tblHelper.addGeneratedColumn("image", new NotNullColumnGenerator(app));
		tblHelper.setColumnWidth("image", 40);
		tblHelper.setColumnAlignment("image", Table.ALIGN_CENTER);

		tblPlace.addGeneratedColumn("image", new NotNullColumnGenerator(app));
		tblPlace.setColumnWidth("image", 40);
		tblPlace.setColumnAlignment("image", Table.ALIGN_CENTER);
		
		tblSquad.addGeneratedColumn("requirements",
				new BooleanColumnGenerator(app));
		tblSquad.setColumnWidth("requirements", 50);
		tblSquad.setColumnAlignment("requirements", Table.ALIGN_CENTER);
		
		tblVehicle.addGeneratedColumn("requirements",
				new BooleanColumnGenerator(app));
		tblVehicle.setColumnWidth("requirements", 50);
		tblVehicle.setColumnAlignment("requirements", Table.ALIGN_CENTER);
		tblVehicle.addGeneratedColumn("image", new NotNullColumnGenerator(app));
		tblVehicle.setColumnWidth("image", 40);
		tblVehicle.setColumnAlignment("image", Table.ALIGN_CENTER);
		tblVehicle.addGeneratedColumn("tacticalImage",
				new NotNullColumnGenerator(app));
		tblVehicle.setColumnWidth("tacticalImage", 40);
		tblVehicle.setColumnAlignment("tacticalImage", Table.ALIGN_CENTER);
	}
	
	/**
	 * Sets the sidebar.
	 * 
	 * @param com The new sidebar
	 */
	public void setSidebar(ComponentContainer com) {
		sidebar.setContent(com);
	}
	
	/**
	 * Gets the container with all helpers.
	 * 
	 * @return The container with all helpers
	 */
	public HelperContainer getHelperContainer() {
		return containerHelper;
	}
	
	/**
	 * Gets the container with all places.
	 * 
	 * @return The container with all places
	 */
	public PlaceContainer getPlaceContainer() {
		return containerPlace;
	}
	
	/**
	 * Gets the container with all squads.
	 * 
	 * @return The container with all squads
	 */
	public SquadContainer getSquadContainer() {
		return containerSquad;
	}

	/**
	 * {@inheritDoc}
	 */
	public void drop(DragAndDropEvent event) {
		DataBoundTransferable t = (DataBoundTransferable)
				event.getTransferable();
		Container con = t.getSourceContainer();
		Object id = t.getItemId();
		AbstractSelectTargetDetails drop = ((AbstractSelectTargetDetails)
				event.getTargetDetails());
		Component target = drop.getTarget();
		Object tId = drop.getItemIdOver();

		if (con == containerHelper && target == tblSquad) {
			
			// Helper -> Squad
			containerSquad.addHelper(containerHelper.getItem(id).getBean(),
					SquadContainer.getSquad(containerSquad.getItem(tId)));
			
		} else if (con == containerSquad && target == tblHelper) {
			
			// Squad -> Helper
			BeanItem<Helper> helper = containerHelper.getItem(id);
			Item iSquad = containerSquad.getItem(id);
			if (helper != null) {
				Object pId = containerSquad.getParent(id);
				Item sItem = containerSquad.getItem(pId);
				Squad squad = SquadContainer.getSquad(sItem);
				if (squad != null) squad.removeHelper(helper.getBean());
				containerSquad.removeItem(id);
			} else if (iSquad != null) {
				Squad squad = DBManager.getCommunicator().find(Squad.class,
						(Long) id);
				List<Helper> helpers = squad.getHelpers();
				for (Helper h : helpers) containerSquad.removeItem(h.getId());
				squad.setHelpers(new ArrayList<Helper>());
			}
			
		} else if (con == containerSquad && target == tblSquad) {
			
			// Squad -> Squad
			BeanItem<Helper> helper = containerHelper.getItem(id);
			Item iSquad = containerSquad.getItem(id);
			if (helper != null) {
				Object pId = containerSquad.getParent(id);
				Item sItem = containerSquad.getItem(pId);
				Squad squad = SquadContainer.getSquad(sItem);
				if (squad != null) squad.removeHelper(helper.getBean());
				squad = SquadContainer.getSquad(containerSquad.getItem(tId));
				if (squad != null) {
					containerSquad.setParent(id, tId);
					squad.addHelper(helper.getBean());
				}
			} else if (iSquad != null) {
				Squad squad = DBManager.getCommunicator().find(Squad.class,
						(Long) id);
				List<Helper> helpers = squad.getHelpers();
				Squad tS = SquadContainer.getSquad(containerSquad.getItem(tId));
				for (Helper h : helpers) {
					containerSquad.setParent(h.getId(), tId);
					tS.addHelper(h);
				}
				squad.setHelpers(new ArrayList<Helper>());
			}
			
		} else if (con == containerHelper && target == tblVehicle) {
			
			// Helper -> Vehicle
			containerVehicle.addHelper(containerHelper.getItem(id).getBean(),
					VehicleContainer.getVehicle(containerVehicle.getItem(tId)));
			
		} else if (con == containerVehicle && target == tblHelper) {
			
			// Vehicle -> Helper
			BeanItem<Helper> helper = containerHelper.getItem(id);
			Item iVeh = containerVehicle.getItem(id);
			if (helper != null) {
				Object pId = containerVehicle.getParent(id);
				Item vItem = containerVehicle.getItem(pId);
				Vehicle veh = VehicleContainer.getVehicle(vItem);
				if (veh != null) veh.removeHelper(helper.getBean());
				containerVehicle.removeItem(id);
			} else if (iVeh != null) {
				Vehicle veh = DBManager.getCommunicator().find(Vehicle.class,
						(Long) id);
				List<Helper> helpers = veh.getHelpers();
				for (Helper h : helpers) containerVehicle.removeItem(h.getId());
				veh.setHelpers(new ArrayList<Helper>());
			}
			
		} else if (con == containerVehicle && target == tblVehicle) {
			
			// Vehicle -> Vehicle
			BeanItem<Helper> helper = containerHelper.getItem(id);
			Item iVeh = containerVehicle.getItem(id);
			if (helper != null) {
				Object pId = containerVehicle.getParent(id);
				Item vItem = containerVehicle.getItem(pId);
				Vehicle veh = VehicleContainer.getVehicle(vItem);
				if (veh != null) veh.removeHelper(helper.getBean());
				veh = VehicleContainer.getVehicle(
						containerVehicle.getItem(tId));
				if (veh != null) {
					containerVehicle.setParent(id, tId);
					veh.addHelper(helper.getBean());
				}
			} else if (iVeh != null) {
				Vehicle veh = DBManager.getCommunicator().find(Vehicle.class,
						(Long) id);
				List<Helper> helpers = veh.getHelpers();
				Vehicle tV = VehicleContainer.getVehicle(
						containerVehicle.getItem(tId));
				for (Helper h : helpers) {
					containerVehicle.setParent(h.getId(), tId);
					tV.addHelper(h);
				}
				veh.setHelpers(new ArrayList<Helper>());
			}
			
		} else if (con == containerSquad && target == tblVehicle) {
			
			// Squad -> Vehicle
			BeanItem<Helper> helper = containerHelper.getItem(id);
			Item iSquad = containerSquad.getItem(id);
			if (helper != null) {
				Object pId = containerSquad.getParent(id);
				Item sItem = containerSquad.getItem(pId);
				Squad squad = SquadContainer.getSquad(sItem);
				if (squad != null) squad.removeHelper(helper.getBean());
				containerSquad.removeItem(id);
				Vehicle veh = VehicleContainer.getVehicle(
						containerVehicle.getItem(tId));
				if (veh != null) 
					containerVehicle.addHelper(helper.getBean(), veh);
			} else if (iSquad != null) {
				Squad squad = DBManager.getCommunicator().find(Squad.class,
						(Long) id);
				List<Helper> helpers = squad.getHelpers();
				Vehicle tV = VehicleContainer.getVehicle(
						containerVehicle.getItem(tId));
				for (Helper h : helpers) {
					containerSquad.removeItem(h.getId());
					containerVehicle.addHelper(h, tV);
				}
				squad.setHelpers(new ArrayList<Helper>());
			}
			
		} else if (con == containerVehicle && target == tblSquad) {
			
			// Vehicle -> Squad
			BeanItem<Helper> helper = containerHelper.getItem(id);
			Item iVeh = containerVehicle.getItem(id);
			if (helper != null) {
				Object pId = containerVehicle.getParent(id);
				Item vItem = containerVehicle.getItem(pId);
				Vehicle veh = VehicleContainer.getVehicle(vItem);
				if (veh != null) veh.removeHelper(helper.getBean());
				containerVehicle.removeItem(id);
				Squad squad = SquadContainer.getSquad(
						containerSquad.getItem(tId));
				if (squad != null)
					containerSquad.addHelper(helper.getBean(), squad);
			} else if (iVeh != null) {
				Vehicle veh = DBManager.getCommunicator().find(Vehicle.class,
						(Long) id);
				List<Helper> helpers = veh.getHelpers();
				Squad tS = SquadContainer.getSquad(containerSquad.getItem(tId));
				for (Helper h : helpers) {
					containerVehicle.removeItem(h.getId());
					containerSquad.addHelper(h, tS);
				}
				veh.setHelpers(new ArrayList<Helper>());
			}
			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	/**
	 * Gets the container with all vehicles.
	 * 
	 * @return The container with all vehicles
	 */
	public VehicleContainer getVehicleContainer() {
		return containerVehicle;
	}

}