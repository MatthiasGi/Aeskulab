package de.gianfelice.aeskulab.ui.tabs.map;

import java.io.File;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Qualification;

/**
 * A popup window to display information about a helper.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 * @see     Helper
 */
public class WinHelper extends HorizontalLayout {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The selected helper. */
	private Helper helper;
	
	/** An image of the helper. */
	private Embedded emb;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the window and sets the helper.
	 * 
	 * @param helper The helper
	 */
	public WinHelper(Helper helper) {
		this.helper = helper;
		
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		emb = new Embedded();
		emb.setWidth("150px");
		addComponent(emb);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		verLayout.setMargin(false, false, false, true);
		addComponent(verLayout);
		setExpandRatio(verLayout, 1.0f);
		
		Label lblName = new Label(helper.getFirstName() + " " +
				helper.getLastName());
		lblName.setStyleName(Reindeer.LABEL_H1);
		verLayout.addComponent(lblName);
		verLayout.addComponent(new Label(helper.getTelNumber()));
		
		String quals = "";
		List<Qualification> qs = helper.getQualifications();
		for (int i = 1; i < qs.size(); i++) quals += qs.get(i - 1) + ", ";
		Label lblQuals = new Label(quals + qs.get(qs.size() - 1));
		lblQuals.setStyleName(Reindeer.LABEL_SMALL);
		verLayout.addComponent(lblQuals);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		Application app = getApplication();
		File file = helper.getImage();
		if (file == null) {
			emb.setSource(new ClassResource("res/150/transformer.png", app));
		} else {
			emb.setSource(new FileResource(file, app));
		}
	}
	
}