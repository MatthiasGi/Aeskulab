package de.gianfelice.aeskulab.ui.tabs.units;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.ui.tabs.TabUnits;

/**
 * A toolbar to create new entities.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class ToolBar extends HorizontalLayout implements ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** Creates a new helper. */
	private Button btnNewHelper;
	
	/** Creates a new squad. */
	private Button btnNewSquad;
	
	/** Creates a new vehicle. */
	private Button btnNewVehicle;
	
	/** Creates a new place. */
	private Button btnNewPlace;
	
	/** The parent tab. */
	private TabUnits tab;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the toolbar and sets the parent tab.
	 * 
	 * @param tab The tab, that should be assigned to this toolbar.
	 */
	public ToolBar(final TabUnits tab) {
		this.tab = tab;
		setWidth("100%");
		setMargin(true);
		setSpacing(true);
		setStyleName(Reindeer.LAYOUT_BLUE);
		
		btnNewHelper = new Button("Neuer Helfer", this);
		addComponent(btnNewHelper);
		btnNewSquad = new Button("Neuer Trupp", this);
		addComponent(btnNewSquad);
		btnNewVehicle = new Button("Neues Fahrzeug", this);
		addComponent(btnNewVehicle);
		btnNewPlace = new Button("Neuer Ort", this);
		addComponent(btnNewPlace);
		setExpandRatio(btnNewPlace, 1.0f);
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		Application app = getApplication();
		btnNewHelper.setIcon(new ClassResource("res/16/user_add.png", app));
		btnNewSquad.setIcon(new ClassResource("res/16/group_add.png", app));
		btnNewVehicle.setIcon(new ClassResource("res/16/lorry_add.png", app));
		btnNewPlace.setIcon(new ClassResource("res/16/map_add.png", app));
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		Button btn = event.getButton();
		if (btn == btnNewHelper) {
			tab.setSidebar(new HelperSidebar(tab.getHelperContainer()));
		} else if (btn == btnNewPlace) {
			tab.setSidebar(new PlaceSidebar(tab.getPlaceContainer()));
		} else if (btn == btnNewSquad) {
			tab.setSidebar(new SquadSidebar(tab.getSquadContainer()));
		} else if (btn == btnNewVehicle) {
			tab.setSidebar(new VehicleSidebar(tab.getVehicleContainer()));
		}
	}

}