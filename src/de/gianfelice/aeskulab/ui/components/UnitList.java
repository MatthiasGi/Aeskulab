package de.gianfelice.aeskulab.ui.components;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

// TODO: Temporary
@SuppressWarnings("javadoc")
public class UnitList extends CustomComponent {

	private static final long serialVersionUID = 1L;
	
	private Embedded img;
	
	private Label lblName;
	
	private Label lblInfo;
	
	private Label lblState;
	
	private Object entity;
	
	public UnitList(Object entity) {
		this.entity = entity;
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setStyleName(Reindeer.LAYOUT_WHITE);
		horLayout.setWidth("100%");
		horLayout.setHeight("50px");
		horLayout.setSpacing(true);
		horLayout.setMargin(false, true, false, true);
		setCompositionRoot(horLayout);
		
		img = new Embedded();
		horLayout.addComponent(img);
		horLayout.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		
		VerticalLayout verInfo = new VerticalLayout();
		verInfo.setSpacing(true);
		lblName = new Label();
		lblName.setSizeUndefined();
		lblName.setStyleName(Reindeer.LABEL_H2);
		verInfo.addComponent(lblName);
		lblInfo = new Label();
		lblInfo.setStyleName(Reindeer.LABEL_SMALL);
		lblInfo.setSizeUndefined();
		verInfo.addComponent(lblInfo);
		horLayout.addComponent(verInfo);
		horLayout.setExpandRatio(verInfo, 1.0f);
		
		lblState = new Label();
		lblState.setSizeUndefined();
		lblState.setStyleName(Reindeer.LABEL_H1);
		horLayout.addComponent(lblState);
		horLayout.setComponentAlignment(lblState, Alignment.MIDDLE_LEFT);
	}
	
	public void setSource(Resource source) {
		img.setSource(source);
	}
	
	public void setName(String name) {
		lblName.setValue(name);
	}
	
	public void setInfo(String info) {
		lblInfo.setValue(info);
	}
	
	public void setState(int state) {
		lblState.setValue(state);
	}
	
	public Object getEntity() {
		return entity;
	}

}