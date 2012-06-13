package de.gianfelice.aeskulab.ui.components;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.data.entities.Vehicle;

// TODO: Temporary
@SuppressWarnings("javadoc")
public class Unit extends CustomComponent {

	private static final long serialVersionUID = 1L;
	
	private Embedded img;
	
	private Label name;
	
	private String tac;
	
	private Object entity;
	
	public Unit(Object entity) {
		this.entity = entity;
		setSizeUndefined();
		VerticalLayout verLayout = new VerticalLayout();
		setCompositionRoot(verLayout);
		img = new Embedded();
		img.setHeight("30px");
		verLayout.addComponent(img);
		verLayout.setComponentAlignment(img, Alignment.TOP_CENTER);
		name = new Label();
		name.setStyleName(Reindeer.LABEL_SMALL);
		name.setSizeUndefined();
		verLayout.addComponent(name);
		verLayout.setComponentAlignment(name, Alignment.TOP_CENTER);
		
		if (entity instanceof Helper) {
			setTac("person");
			setName(((Helper) entity).getLastName());
		} else if (entity instanceof Squad) {
			setTac("trupp");
			setName(((Squad) entity).getName());
		} else if (entity instanceof Vehicle) {
			setTac("fahrzeug");
			setName(((Vehicle) entity).getName());
		}
	}
	
	public void setTac(String tac) {
		Application app = getApplication();
		if (app == null) {
			this.tac = tac;
		} else {
			img.setSource(new ClassResource("res/tac/" + tac + ".png", app));
		}
	}
	
	public void setName(String name) {
		this.name.setValue(name);
	}
	
	public void attach() {
		super.attach();
		img.setSource(new ClassResource("res/tac/" + tac + ".png",
				getApplication()));
	}
	
	public Object getEntity() {
		return entity;
	}

}