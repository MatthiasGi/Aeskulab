package de.gianfelice.aeskulab.ui.components;

import com.vaadin.Application;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.ui.tabs.TabMap;
import de.gianfelice.aeskulab.ui.tabs.map.WinHelper;
import de.gianfelice.aeskulab.ui.tabs.map.WinSquad;
import de.gianfelice.aeskulab.ui.tabs.map.WinVehicle;

/**
 * A component for D&D purposes to represent a place.
 * 
 * @author  Matthias Gianfelice
 * @version 0.2.0
 * @see     Squad
 * @see     Vehicle
 * @see     Helper
 */
public class Unit extends CustomComponent implements LayoutClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The tactical image. */
	private Embedded img;
	
	/** The name of the unit. */
	private Label name;
	
	/** The name of the tactical image. */
	private String tac;
	
	/** The unit itself. */
	private Object entity;
	
	/** The parent tab fpr update-events. */
	private TabMap tab;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the component.
	 * 
	 * @param tab    The parent tab
	 * @param entity The place itself
	 */
	public Unit(TabMap tab, Object entity) {
		this.tab = tab;
		this.entity = entity;
		setSizeUndefined();
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.addListener(this);
		setCompositionRoot(verLayout);
		img = new Embedded();
		img.setHeight("30px");
		verLayout.addComponent(img);
		verLayout.setComponentAlignment(img, Alignment.TOP_CENTER);
		name = new Label();
		name.setStyleName(Reindeer.LABEL_SMALL);
		name.setSizeUndefined();
		verLayout.addComponent(name);
		verLayout.setComponentAlignment(name, Alignment.TOP_CENTER);
		
		if (entity instanceof Helper) {
			setTac("person");
			name.setValue(((Helper) entity).getLastName());
		} else if (entity instanceof Squad) {
			setTac("trupp");
			name.setValue(((Squad) entity).getName());
		} else if (entity instanceof Vehicle) {
			setTac("fahrzeug");
			name.setValue(((Vehicle) entity).getName());
		}
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Sets the tactical image of the component.
	 * 
	 * @param tac The name of the tactical image
	 */
	public void setTac(String tac) {
		Application app = getApplication();
		if (app == null) {
			this.tac = tac;
		} else {
			img.setSource(new ClassResource("res/tac/" + tac + ".png", app));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		img.setSource(new ClassResource("res/tac/" + tac + ".png",
				getApplication()));
	}
	
	/**
	 * Gets the corresponding entity.
	 * 
	 * @return The entity
	 */
	public Object getEntity() {
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutClick(LayoutClickEvent event) {
		if (entity instanceof Squad) {
			tab.showWindow(new WinSquad(tab, (Squad) entity),
					event.getClientX(), event.getClientY());
		} else if (entity instanceof Vehicle) {
			tab.showWindow(new WinVehicle(tab, (Vehicle) entity),
					event.getClientX(), event.getClientY());
		} else if (entity instanceof Helper) {
			tab.showWindow(new WinHelper((Helper) entity), event.getClientX(),
					event.getClientY());
		}
	}

}