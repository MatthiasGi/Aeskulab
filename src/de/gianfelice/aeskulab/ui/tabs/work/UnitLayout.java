package de.gianfelice.aeskulab.ui.tabs.work;

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

import de.gianfelice.aeskulab.data.container.UnitContainer;
import de.gianfelice.aeskulab.data.container.WorkContainer;
import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Qualification;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;
import de.gianfelice.aeskulab.data.entities.Work;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A layout to display information about all units assigned to a {@link Work}
 * object.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Work
 */
public class UnitLayout extends VerticalLayout {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The current active object. */
	private Work current;
	
	/** The container with all objects. */
	private WorkContainer works;
	
	/** The container with all units. */
	private UnitContainer units;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates and prepares the layout.
	 * 
	 * @param works Container with all objects
	 * @param units Container with all units
	 */
	public UnitLayout(WorkContainer works, UnitContainer units) {
		this.works = works;
		this.units = units;
		setWidth("100%");
		setMargin(true);
		setSpacing(true);
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * Sets the current active object.
	 * 
	 * @param current The new object
	 */
	public void setCurrent(Work current) {
		this.current = current;
		update();
	}
	
	/**
	 * Updates the layout.
	 */
	public void update() {
		removeAllComponents();
		if (current == null) return;
		DBManager.getCommunicator().refresh(current);
		List<Squad> squads = current.getSquads();
		List<Vehicle> vehicles = current.getVehicles();
		Application app = getApplication();
		
		for (final Squad squad : squads) {
			Panel panel = new Panel();
			addComponent(panel);
			HorizontalLayout horContent = new HorizontalLayout();
			horContent.setMargin(true);
			horContent.setSpacing(true);
			horContent.setWidth("100%");
			panel.setContent(horContent);
			
			Embedded emb = new Embedded();
			emb.setSource(new ClassResource("res/tac/trupp.png", app));
			emb.setWidth("50px");
			emb.setHeight("30px");
			horContent.addComponent(emb);
			
			VerticalLayout verContent = new VerticalLayout();
			verContent.setSpacing(true);
			horContent.addComponent(verContent);
			horContent.setExpandRatio(verContent, 1f);
			
			Label lblName = new Label(squad.getName());
			lblName.setStyleName(Reindeer.LABEL_H2);
			verContent.addComponent(lblName);
			verContent.addComponent(new Label(squad.getType().getName()));
			
			List<Helper> helpers = squad.getHelpers();
			for (Helper helper : helpers) {
				verContent.addComponent(new Label("<hr />",
						Label.CONTENT_XHTML));
				verContent.addComponent(new Label(helper.getFirstName() + " " +
						helper.getLastName()));
				Label lblTel = new Label(helper.getTelNumber());
				lblTel.setStyleName(Reindeer.LABEL_SMALL);
				verContent.addComponent(lblTel);
				String quals = "";
				List<Qualification> qs = helper.getQualifications();
				for (int i = 1; i < qs.size(); i++)
					quals += qs.get(i - 1) + ", ";
				Label lblQuals = new Label(quals + qs.get(qs.size() - 1));
				lblQuals.setStyleName(Reindeer.LABEL_SMALL);
				verContent.addComponent(lblQuals);
			}
			
			Slider slider = new Slider(0, 9);
			slider.setOrientation(Slider.ORIENTATION_VERTICAL);
			slider.setImmediate(true);
			slider.addListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					int state = ((Double)
							event.getProperty().getValue()).intValue();
					squad.setState(state);
					Long id = squad.getId();
					works.getItem(id).getItemProperty("state").setValue(state);
					boolean visible = units.areUnavailableVisible();
					units.setUnavailableVisible(true);
					units.getItem(id).getItemProperty("state").setValue(state);
					units.setUnavailableVisible(visible);
				}
			});
			try {
				slider.setValue(squad.getState());
			} catch (Exception e) {}
			horContent.addComponent(slider);
		}
		
		for (final Vehicle vehicle : vehicles) {
			Panel panel = new Panel();
			addComponent(panel);
			HorizontalLayout horContent = new HorizontalLayout();
			horContent.setMargin(true);
			horContent.setSpacing(true);
			horContent.setWidth("100%");
			panel.setContent(horContent);
			
			Embedded emb = new Embedded();
			File img = vehicle.getImage();
			if (img == null) {
				emb.setSource(new ClassResource("res/tac/fahrzeug.png", app));
			} else {
				emb.setSource(new FileResource(img, app));
			}
			emb.setWidth("50px");
			horContent.addComponent(emb);
			
			VerticalLayout verContent = new VerticalLayout();
			verContent.setSpacing(true);
			horContent.addComponent(verContent);
			horContent.setExpandRatio(verContent, 1f);
			
			Label lblName = new Label(vehicle.getName());
			lblName.setStyleName(Reindeer.LABEL_H2);
			verContent.addComponent(lblName);
			verContent.addComponent(new Label(vehicle.getType().getName()));
			
			List<Helper> helpers = vehicle.getHelpers();
			for (Helper helper : helpers) {
				verContent.addComponent(new Label("<hr />",
						Label.CONTENT_XHTML));
				verContent.addComponent(new Label(helper.getFirstName() + " " +
						helper.getLastName()));
				Label lblTel = new Label(helper.getTelNumber());
				lblTel.setStyleName(Reindeer.LABEL_SMALL);
				verContent.addComponent(lblTel);
				String quals = "";
				List<Qualification> qs = helper.getQualifications();
				for (int i = 1; i < qs.size(); i++)
					quals += qs.get(i - 1) + ", ";
				Label lblQuals = new Label(quals + qs.get(qs.size() - 1));
				lblQuals.setStyleName(Reindeer.LABEL_SMALL);
				verContent.addComponent(lblQuals);
			}
			
			Slider slider = new Slider(0, 9);
			slider.setOrientation(Slider.ORIENTATION_VERTICAL);
			slider.setImmediate(true);
			slider.addListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					int state = ((Double)
							event.getProperty().getValue()).intValue();
					vehicle.setState(state);
					Long id = vehicle.getId();
					works.getItem(id).getItemProperty("state").setValue(state);
					boolean visible = units.areUnavailableVisible();
					units.setUnavailableVisible(true);
					units.getItem(id).getItemProperty("state").setValue(state);
					units.setUnavailableVisible(visible);
				}
			});
			try {
				slider.setValue(vehicle.getState());
			} catch (Exception e) {}
			horContent.addComponent(slider);
		}
	}

}