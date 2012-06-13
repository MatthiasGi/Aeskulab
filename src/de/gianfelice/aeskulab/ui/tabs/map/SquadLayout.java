package de.gianfelice.aeskulab.ui.tabs.map;

import java.util.Collection;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.terminal.ClassResource;

import de.gianfelice.aeskulab.data.container.SquadContainer;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.ui.components.UnitList;
import de.gianfelice.aeskulab.ui.tabs.TabMap;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

/**
 * A sidebar that contains all squads.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 * @see     Squad
 * @see     UnitList
 */
public class SquadLayout extends DDVerticalLayout {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** The parent tab for update-events. */
	private TabMap tab;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the component and sets the parent tab.
	 * 
	 * @param tab The parent tab
	 */
	public SquadLayout(TabMap tab) {
		this.tab = tab;
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		
		SquadContainer con = new SquadContainer();
		con.setItemSorter(new DefaultItemSorter());
		Collection<?> ids = con.getItemIds();
		for (Object id : ids) {
			Squad s = SquadContainer.getSquad(con.getItem(id));
			if (s == null) continue;

			UnitList u = new UnitList(tab, s);
			u.setSource(new ClassResource("res/tac/trupp.png",
					getApplication()));
			u.setName(s.getName());
			u.setInfo(s.getType().getName());
			u.setState(s.getState());
			addComponent(u);
		}
		
		setDragMode(LayoutDragMode.CLONE);
		setDropHandler(new RemoveHandler());
	}

}