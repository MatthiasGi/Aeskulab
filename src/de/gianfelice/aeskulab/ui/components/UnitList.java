package de.gianfelice.aeskulab.ui.components;

import java.io.File;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Place;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.data.entities.Work;
import de.gianfelice.aeskulab.ui.tabs.TabMap;
import de.gianfelice.aeskulab.ui.tabs.TabWork;
import de.gianfelice.aeskulab.ui.tabs.map.WinHelper;
import de.gianfelice.aeskulab.ui.tabs.map.WinSquad;
import de.gianfelice.aeskulab.ui.tabs.map.WinVehicle;

/**
 * A component for D&D purposes to represent a unit inside a list.
 * 
 * @author  Matthias Gianfelice
 * @version 0.2.0
 * @see     Squad
 * @see     Vehicle
 * @see     Helper
 * @see     Place
 * @see     Work
 */
public class UnitList extends CustomComponent implements LayoutClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** An image of the unit. */
	private Embedded img;
	
	/** The name of the unit. */
	private Label lblName;
	
	/** Additional information for the unit. */
	private Label lblInfo;
	
	/** The current state of the unit. */
	private Label lblState;
	
	/** The corresponding entity. */
	private Object entity;
	
	/** The parent tab for update-events. */
	private TabMap tab;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the component.
	 * 
	 * @param tab    The parent tab
	 * @param entity The entity itself
	 */
	public UnitList(TabMap tab, Object entity) {
		this.tab = tab;
		this.entity = entity;
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setStyleName(Reindeer.LAYOUT_WHITE);
		horLayout.setWidth("100%");
		horLayout.setHeight("50px");
		horLayout.setSpacing(true);
		horLayout.setMargin(false, true, false, true);
		horLayout.addListener(this);
		setCompositionRoot(horLayout);
		
		img = new Embedded();
		horLayout.addComponent(img);
		horLayout.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		
		VerticalLayout verInfo = new VerticalLayout();
		verInfo.setSpacing(true);
		lblName = new Label();
		lblName.setSizeUndefined();
		lblName.setStyleName(Reindeer.LABEL_H2);
		verInfo.addComponent(lblName);
		lblInfo = new Label();
		lblInfo.setStyleName(Reindeer.LABEL_SMALL);
		lblInfo.setSizeUndefined();
		verInfo.addComponent(lblInfo);
		horLayout.addComponent(verInfo);
		horLayout.setExpandRatio(verInfo, 1.0f);
		
		lblState = new Label();
		lblState.setSizeUndefined();
		lblState.setStyleName(Reindeer.LABEL_H1);
		horLayout.addComponent(lblState);
		horLayout.setComponentAlignment(lblState, Alignment.MIDDLE_LEFT);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Sets the source of the image.
	 * 
	 * @param source The source
	 */
	public void setSource(Resource source) {
		img.setSource(source);
		if (!(source instanceof ClassResource)) img.setHeight("50px");
	}
	
	/**
	 * Sets the name of the component.
	 * 
	 * @param name The new name
	 */
	public void setName(String name) {
		lblName.setValue(name);
	}
	
	/**
	 * Sets the additional information.
	 * 
	 * @param info The new additional information
	 */
	public void setInfo(String info) {
		lblInfo.setValue(info);
	}
	
	/**
	 * Sets the current state.
	 * 
	 * @param state The new state
	 */
	public void setState(int state) {
		lblState.setValue(state);
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
		} else if (entity instanceof Place) {
			Place place = (Place) entity;
			HorizontalLayout horLayout = new HorizontalLayout();
			horLayout.setMargin(true);
			horLayout.setSpacing(true);
			
			Embedded emb = new Embedded();
			emb.setWidth("150px");
			File img = place.getImage();
			Resource res;
			if (img == null) {
				res = new ClassResource("res/150/transformer.png",
						getApplication());
			} else {
				res = new FileResource(img, getApplication());
			}
			emb.setSource(res);
			horLayout.addComponent(emb);
			
			Label lblName = new Label(place.getName());
			lblName.setStyleName(Reindeer.LABEL_H1);
			horLayout.addComponent(lblName);
			
			tab.showWindow(horLayout, event.getClientX(), event.getClientY());
		} else if (entity instanceof Work) {
			tab.getMainLayout().setContentTab(new TabWork((Work) entity));
		}
	}

}