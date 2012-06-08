package de.gianfelice.aeskulab.ui.components;

import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

// TODO: Temporary
@SuppressWarnings("javadoc")
public class Unit extends CustomComponent {

	private static final long serialVersionUID = 1L;
	
	private Embedded img;
	
	private Label name;
	
	public Unit() {
		setSizeUndefined();
		VerticalLayout verLayout = new VerticalLayout();
		setCompositionRoot(verLayout);
		img = new Embedded();
		img.setWidth("30px");
		img.setHeight("30px");
		verLayout.addComponent(img);
		verLayout.setComponentAlignment(img, Alignment.TOP_CENTER);
		name = new Label();
		name.setStyleName(Reindeer.LABEL_SMALL);
		name.setSizeUndefined();
		verLayout.addComponent(name);
		verLayout.setComponentAlignment(name, Alignment.TOP_CENTER);
	}
	
	public void setTac(String tac) {
		img.setSource(new ClassResource("res/tac/" + tac + ".png",
				getApplication()));
	}
	
	public void setName(String name) {
		this.name.setValue(name);
	}

}