package de.gianfelice.aeskulab.ui.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.data.entities.Work;
import de.gianfelice.aeskulab.ui.components.ComPlace.DDGrid;
import de.gianfelice.aeskulab.ui.tabs.TabMap;
import de.gianfelice.aeskulab.ui.tabs.TabWork;

import fi.jasoft.dragdroplayouts.DDGridLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * A component for D&D purposes to represent a {@link Work} object.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 * @see     Work
 */
public class ComWork extends CustomComponent implements DropHandler,
		LayoutClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** The name. */
	private Label name;
	
	/** A grid to hold different entities. */
	private DDGridW grid;
	
	/** The work itself. */
	private Work work;
	
	/** The parent tab for update-events. */
	private TabMap tab;
	
	/** The tactical image. */
	private Embedded img;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the component.
	 * 
	 * @param tab  The parent tab
	 * @param work The work itself
	 */
	public ComWork(TabMap tab, Work work) {
		this.tab = tab;
		this.work = work;
		setSizeUndefined();

		VerticalLayout verLayout = new VerticalLayout();
		verLayout.addListener(this);
		setCompositionRoot(verLayout);
		
		img = new Embedded();
		img.setWidth("14px");
		img.setHeight("50px");
		verLayout.addComponent(img);
		verLayout.setComponentAlignment(img, Alignment.TOP_CENTER);
		
		name = new Label(work.getCaption());
		name.setStyleName(Reindeer.LABEL_SMALL);
		name.setSizeUndefined();
		verLayout.addComponent(name);
		verLayout.setComponentAlignment(name, Alignment.TOP_CENTER);
		
		grid = new DDGridW(1, 1);
		grid.setSpacing(true);
		grid.setMargin(true);
		grid.setDragMode(LayoutDragMode.CLONE);
		grid.setStyleName(Reindeer.LAYOUT_WHITE);
		grid.setDropHandler(this);
		verLayout.addComponent(grid);
		verLayout.setComponentAlignment(grid, Alignment.TOP_CENTER);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		img.setSource(new ClassResource("res/tac/akut.png", getApplication()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drop(DragAndDropEvent event) {
		LayoutBoundTransferable tran = (LayoutBoundTransferable)
				event.getTransferable();
		Component com = tran.getComponent();
		Unit u;
		if (com instanceof UnitList) {
			
			// Sidebar -> ComWork
			Object o = ((UnitList) com).getEntity();
			if (tab.onMap(o)) return;
			u = new Unit(tab, o);
			if (!addObject(o)) return;
			
		} else if (com instanceof Unit) {
			
			// Map / ComWork / ComPlace -> ComWork
			u = (Unit) com;
			if (u.getEntity() instanceof Helper) return;
			Component source = tran.getSourceComponent();
			Object o = u.getEntity();
			if (source instanceof DDGrid) {
				DDGrid grid = (DDGrid) source;
				if (o instanceof Squad) {
					grid.getPlace().removeSquad((Squad) o);
					grid.removeComponent(u);
					grid.recalculateLayout();
				} else if (o instanceof Vehicle) {
					grid.getPlace().removeVehicle((Vehicle) o);
					grid.removeComponent(u);
					grid.recalculateLayout();
				} else {
					return;
				}
			} else if (source instanceof DDGridW) {
				DDGridW grid = (DDGridW) source;
				if (o instanceof Squad) {
					grid.getWork().removeSquad((Squad) o);
					grid.removeComponent(u);
					grid.recalculateLayout();
				} else if (o instanceof Vehicle) {
					grid.getWork().removeVehicle((Vehicle) o);
					grid.removeComponent(u);
					grid.recalculateLayout();
				} else {
					return;
				}
			}
			tab.savePosition(o, null, null);
			addObject(o);
			
		} else {
			return;
		}

		grid.addComponent(u);
		grid.recalculateLayout();
	}
	
	/**
	 * Adds an entity to the object.
	 * 
	 * @param  o The entity
	 * @return   Whether the entity was valid
	 */
	public boolean addObject(Object o) {
		if (o instanceof Squad) {
			work.addSquad((Squad) o);
		} else if (o instanceof Vehicle) {
			work.addVehicle((Vehicle) o);
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Adds a unit to the layout.
	 * 
	 * @param u The unit
	 */
	public void addUnit(Unit u) {
		grid.addComponent(u);
		grid.recalculateLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	/**
	 * Gets the corresponding work.
	 * 
	 * @return The work
	 */
	public Work getWork() {
		return work;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutClick(LayoutClickEvent event) {
		if (event.isAltKey()) {
			grid.setVisible(!grid.isVisible());
		} else {
			tab.getMainLayout().setContentTab(new TabWork(work));
		}
	}

	
	// ---------------------------- Inner Class(es) ----------------------------
	/**
	 * The grid inside the component.
	 * 
	 * @author  Matthias Gianfelice
	 * @version 1.0.0
	 */
	public class DDGridW extends DDGridLayout {

		// ---------------------------- Attribute(s) ---------------------------
		/** The default serial version id. */
		private static final long serialVersionUID = 1L;

		// --------------------------- Constructor(s) --------------------------
		/**
		 * Creates the layout.
		 * 
		 * @param i The columns
		 * @param j The rows
		 */
		public DDGridW(int i, int j) {
			super(i, j);
		}

		// ----------------------------- Method(s) -----------------------------
		/**
		 * Recalculates the layout and rearranges the components.
		 */
		public void recalculateLayout() {
			double i = getComponentCount();
			List<Component> coms = new ArrayList<Component>();
			Iterator<Component> it = getComponentIterator();
			while (it.hasNext()) coms.add(it.next());
			for (Component com : coms) removeComponent(com);
			
			if (i < 2) {
				setColumns(1);
			} else if (i < 5) {
				setColumns(2);
			} else {
				setColumns(3);
			}
			double cols = getColumns();
			int rows = (int) Math.ceil(i / cols);
			if (rows < 1) rows = 1;
			setRows(rows);
			
			it = coms.iterator();
			for (int y = 0; y < rows; y++) {
				for (int x = 0; x < cols; x++) {
					if (!it.hasNext()) return;
					addComponent(it.next(), x, y);
				}
			}
		}

		/**
		 * Gets the corresponding work.
		 * 
		 * @return The work
		 */
		public Work getWork() {
			return work;
		}

	}

}