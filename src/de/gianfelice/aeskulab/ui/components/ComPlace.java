package de.gianfelice.aeskulab.ui.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Place;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.ui.tabs.TabMap;

import fi.jasoft.dragdroplayouts.DDGridLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

// TODO: Temporary
@SuppressWarnings("javadoc")
public class ComPlace extends CustomComponent implements DropHandler {

	private static final long serialVersionUID = 1L;
	
	private Label name;
	
	private DDGrid grid;
	
	private Place place;
	
	private TabMap tab;
	
	private Embedded img;
	
	public ComPlace(TabMap tab, Place place) {
		this.tab = tab;
		this.place = place;
		setSizeUndefined();

		VerticalLayout verLayout = new VerticalLayout();
		setCompositionRoot(verLayout);
		
		img = new Embedded();
		img.setWidth("50px");
		img.setHeight("15px");
		verLayout.addComponent(img);
		verLayout.setComponentAlignment(img, Alignment.TOP_CENTER);
		
		name = new Label(place.getName());
		name.setStyleName(Reindeer.LABEL_SMALL);
		name.setSizeUndefined();
		verLayout.addComponent(name);
		verLayout.setComponentAlignment(name, Alignment.TOP_CENTER);
		
		grid = new DDGrid(1, 1);
		grid.setSpacing(true);
		grid.setMargin(true);
		grid.setDragMode(LayoutDragMode.CLONE);
		grid.setStyleName(Reindeer.LAYOUT_WHITE);
		grid.setDropHandler(this);
		verLayout.addComponent(grid);
		verLayout.setComponentAlignment(grid, Alignment.TOP_CENTER);
	}
	
	public void attach() {
		super.attach();
		img.setSource(new ClassResource("res/tac/ort.png", getApplication()));
	}
	
	public void setName(String name) {
		this.name.setValue(name);
	}

	@Override
	public void drop(DragAndDropEvent event) {
		LayoutBoundTransferable tran = (LayoutBoundTransferable)
				event.getTransferable();
		Component com = tran.getComponent();
		Unit u;
		if (com instanceof UnitList) {
			
			// Sidebar -> ComPlace
			Object o = ((UnitList) com).getEntity();
			if (tab.onMap(o)) return;
			u = new Unit(o);
			addObject(o);
			
		} else if (com instanceof Unit) {
			
			// Map / ComPlace -> ComPlace
			u = (Unit) com;
			((ComponentContainer) tran.getSourceComponent()).removeComponent(u);
			Object o = u.getEntity();
			tab.savePosition(o, null, null);
			addObject(o);
			
		} else {
			return;
		}

		grid.addComponent(u);
		grid.recalculateLayout();
	}
	
	public boolean addObject(Object o) {
		if (o instanceof Squad) {
			place.addSquad((Squad) o);
		} else if (o instanceof Vehicle) {
			place.addVehicle((Vehicle) o);
		} else if (o instanceof Helper) {
			place.addHelper((Helper) o);
		} else {
			return false;
		}
		return true;
	}
	
	public void addUnit(Unit u) {
		grid.addComponent(u);
		grid.recalculateLayout();
	}

	@Override
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}
	
	public class DDGrid extends DDGridLayout {
		
		private static final long serialVersionUID = 1L;

		public DDGrid(int i, int j) {
			super(i, j);
		}

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
		
		public Place getPlace() {
			return place;
		}
	}
	
	public Place getPlace() {
		return place;
	}

}