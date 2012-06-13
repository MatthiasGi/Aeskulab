package de.gianfelice.aeskulab.ui.tabs.map;

import java.io.File;
import java.util.Collection;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;

import de.gianfelice.aeskulab.data.container.VehicleContainer;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.ui.components.UnitList;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

// TODO: Temporary
@SuppressWarnings("javadoc")
public class VehicleLayout extends DDVerticalLayout {

	private static final long serialVersionUID = 1L;
	
	public void attach() {
		super.attach();
		
		VehicleContainer con = new VehicleContainer();
		con.setItemSorter(new DefaultItemSorter());
		Collection<?> ids = con.getItemIds();
		for (Object id : ids) {
			Vehicle v = VehicleContainer.getVehicle(con.getItem(id));
			if (v == null) continue;

			UnitList u = new UnitList(v);
			File file = v.getImage();
			Resource res = null;
			if (file == null) {
				res = new ClassResource("res/tac/fahrzeug.png",
						getApplication());
			} else {
				res = new FileResource(file, getApplication());
			}
			u.setSource(res);
			u.setName(v.getName());
			u.setInfo(v.getType().getName());
			u.setState(v.getState());
			addComponent(u);
		}
		
		setDragMode(LayoutDragMode.CLONE);
		setDropHandler(new RemoveHandler());

	}

}