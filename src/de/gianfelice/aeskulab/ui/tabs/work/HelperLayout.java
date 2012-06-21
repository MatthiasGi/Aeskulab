package de.gianfelice.aeskulab.ui.tabs.work;

import java.io.File;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Qualification;

/**
 * Creates a layout that display information about helpers.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Helper
 */
public class HelperLayout extends Panel {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The main layout. */
	private VerticalLayout verLayout;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates and prepares the layout.
	 */
	public HelperLayout() {
		setStyleName(Reindeer.PANEL_LIGHT);
		setSizeFull();
		verLayout = new VerticalLayout();
		verLayout.setWidth("100%");
		verLayout.setMargin(true);
		verLayout.setSpacing(true);
		setContent(verLayout);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Displays information about the given helpers.
	 * 
	 * @param helpers The helpers
	 */
	public void setHelper(List<Helper> helpers) {
		verLayout.removeAllComponents();
		Application app = getApplication();
		
		for (Helper h : helpers) {
			Panel panel = new Panel();
			verLayout.addComponent(panel);
			
			HorizontalLayout horContent = new HorizontalLayout();
			horContent.setMargin(true);
			horContent.setSpacing(true);
			panel.setContent(horContent);
			
			Embedded emb = new Embedded();
			File img = h.getImage();
			if (img == null) {
				emb.setSource(new ClassResource("res/150/transformer.png",
						app));
			} else {
				emb.setSource(new FileResource(img, app));
			}
			emb.setWidth("50px");
			horContent.addComponent(emb);
			
			VerticalLayout verContent = new VerticalLayout();
			verContent.setSpacing(true);
			horContent.addComponent(verContent);
			
			Label lblName = new Label(h.getFirstName() + " " + h.getLastName());
			lblName.setStyleName(Reindeer.LABEL_H2);
			verContent.addComponent(lblName);
			verContent.addComponent(new Label(h.getTelNumber()));
			
			String quals = "";
			List<Qualification> qs = h.getQualifications();
			for (int i = 1; i < qs.size(); i++) quals += qs.get(i - 1) + ", ";
			Label lblQuals = new Label(quals + qs.get(qs.size() - 1));
			lblQuals.setStyleName(Reindeer.LABEL_SMALL);
			verContent.addComponent(lblQuals);
		}
	}

}