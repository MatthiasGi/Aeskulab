package de.gianfelice.aeskulab.ui.tabs.map;

import java.util.Collection;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.terminal.ClassResource;

import de.gianfelice.aeskulab.data.container.SquadContainer;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.ui.components.UnitList;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

// TODO: Temporary
@SuppressWarnings("javadoc")
public class SquadLayout extends DDVerticalLayout {

	private static final long serialVersionUID = 1L;
	
	public void attach() {
		super.attach();
		
		SquadContainer con = new SquadContainer();
		con.setItemSorter(new DefaultItemSorter());
		Collection<?> ids = con.getItemIds();
		for (Object id : ids) {
			Squad s = SquadContainer.getSquad(con.getItem(id));
			if (s == null) continue;

			UnitList u = new UnitList(s);
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