package de.gianfelice.aeskulab.ui.tabs.map;

import java.io.File;
import java.util.Collection;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;

import de.gianfelice.aeskulab.data.container.PlaceContainer;
import de.gianfelice.aeskulab.data.entities.Place;
import de.gianfelice.aeskulab.ui.components.UnitList;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

// TODO: Temporary
@SuppressWarnings("javadoc")
public class PlaceLayout extends DDVerticalLayout {

	private static final long serialVersionUID = 1L;
	
	public void attach() {
		super.attach();
		
		PlaceContainer con = new PlaceContainer();
		con.setItemSorter(new DefaultItemSorter());
		Collection<?> ids = con.getItemIds();
		for (Object id : ids) {
			Place p = con.getItem(id).getBean();
			if (p == null) continue;

			UnitList u = new UnitList(p);
			File file = p.getImage();
			Resource res = null;
			if (file == null) {
				res = new ClassResource("res/tac/ort.png", getApplication());
			} else {
				res = new FileResource(file, getApplication());
			}
			u.setSource(res);
			u.setName(p.getName());
			addComponent(u);
		}
		
		setDragMode(LayoutDragMode.CLONE);
		setDropHandler(new RemoveHandler());

	}

}