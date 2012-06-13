package de.gianfelice.aeskulab.ui.tabs.map;

import java.io.File;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Qualification;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.ui.tabs.TabMap;

/**
 * A popup window to display information about a vehicle.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.0
 * @see     Vehicle
 */
public class WinVehicle extends HorizontalLayout implements ValueChangeListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The selected vehicle. */
	private Vehicle vehicle;
	
	/** An image of the vehicle. */
	private Embedded emb;
	
	/** The main vertical layout. */
	private VerticalLayout verLayout;
	
	/** The parent tab for update-events. */
	private TabMap tab;
	
	/** A slider to set the state of the vehicle. */
	private Slider state;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the window and sets the parent tab and vehicle.
	 * 
	 * @param tab     The parent tab
	 * @param vehicle The vehicle
	 */
	public WinVehicle(TabMap tab, Vehicle vehicle) {
		this.tab = tab;
		this.vehicle = vehicle;
		
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		emb = new Embedded();
		emb.setWidth("150px");
		addComponent(emb);
		
		verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		verLayout.setMargin(false, false, false, true);
		addComponent(verLayout);
		setExpandRatio(verLayout, 1.0f);
		
		Label lblName = new Label(vehicle.getName());
		lblName.setStyleName(Reindeer.LABEL_H1);
		verLayout.addComponent(lblName);
		verLayout.addComponent(new Label(vehicle.getType().getName()));

		state = new Slider(0, 9);
		state.setOrientation(Slider.ORIENTATION_VERTICAL);
		state.setImmediate(true);
		state.addListener(this);
		try {
			state.setValue(vehicle.getState());
		} catch (Exception e) {}
		addComponent(state);
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		Application app = getApplication();
		File file = vehicle.getImage();
		if (file == null) {
			emb.setSource(new ClassResource("res/150/transformer.png", app));
		} else {
			emb.setSource(new FileResource(file, app));
		}
		
		List<Helper> lst = vehicle.getHelpers();
		for (Helper h : lst) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		int state = ((Double) this.state.getValue()).intValue();
		vehicle.setState(state);
		tab.updateState(vehicle, state);
	}
	
}