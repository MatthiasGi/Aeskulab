package de.gianfelice.aeskulab.ui.tabs.map;

import java.util.Collection;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.terminal.ClassResource;

import de.gianfelice.aeskulab.data.container.WorkItemContainer;
import de.gianfelice.aeskulab.data.entities.Work;
import de.gianfelice.aeskulab.ui.components.UnitList;
import de.gianfelice.aeskulab.ui.tabs.TabMap;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

/**
 * A sidebar that contains all {@link Work} objects, that are not ready.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 * @see     Work
 * @see     UnitList
 */
public class WorkLayout extends DDVerticalLayout {

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
	public WorkLayout(TabMap tab) {
		this.tab = tab;
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		
		WorkItemContainer con = new WorkItemContainer();
		con.setItemSorter(new DefaultItemSorter());
		Collection<?> ids = con.getItemIds();
		for (Object id : ids) {
			Work w = con.getItem(id).getBean();
			if (w == null) continue;

			UnitList u = new UnitList(tab, w);
			u.setSource(new ClassResource("res/tac/akut.png",
					getApplication()));
			u.setName(w.getCaption());
			u.setInfo(w.getPlace());
			addComponent(u);
		}
		
		setDragMode(LayoutDragMode.CLONE);
		setDropHandler(new RemoveHandler());
	}

}