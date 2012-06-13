package de.gianfelice.aeskulab.ui.tabs.map;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;

import de.gianfelice.aeskulab.data.container.HelperContainer;
import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Qualification;
import de.gianfelice.aeskulab.ui.components.UnitList;
import de.gianfelice.aeskulab.ui.tabs.TabMap;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

/**
 * A sidebar that contains all helpers.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 * @see     Helper
 * @see     UnitList
 */
public class HelperLayout extends DDVerticalLayout {

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
	public HelperLayout(TabMap tab) {
		this.tab = tab;
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		
		HelperContainer con = new HelperContainer();
		con.setItemSorter(new DefaultItemSorter());
		Collection<?> ids = con.getItemIds();
		for (Object id : ids) {
			Helper h = con.getItem(id).getBean();
			if (h == null) continue;

			UnitList u = new UnitList(tab, h);
			File file = h.getImage();
			Resource res = null;
			if (file == null) {
				res = new ClassResource("res/tac/person.png", getApplication());
			} else {
				res = new FileResource(file, getApplication());
			}
			u.setSource(res);
			u.setName(h.getFirstName() + " " + h.getLastName());
			String quals = "";
			List<Qualification> qs = h.getQualifications();
			for (int i = 1; i < qs.size(); i++) quals += qs.get(i - 1) + ", ";
			if (qs.size() > 0) quals += qs.get(qs.size() - 1);
			u.setInfo(quals);
			addComponent(u);
		}
		
		setDragMode(LayoutDragMode.CLONE);
		setDropHandler(new RemoveHandler());
	}

}