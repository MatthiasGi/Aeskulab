package de.gianfelice.aeskulab.ui.top.buttons;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.system.SessionHandler;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.system.persistence.DBManager;
import de.gianfelice.aeskulab.system.utils.FileUtil;
import de.gianfelice.aeskulab.ui.MainWindow;
import de.gianfelice.aeskulab.ui.tabs.Tab;
import de.gianfelice.aeskulab.ui.tabs.TabUser;
import de.gianfelice.aeskulab.ui.top.MenuComponent;

/**
 * A link to the user-managment section and a button to log out.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.1
 * @see     TabUser
 */
public class UserInfo extends MenuComponent {
	
	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A label with the name of the user. */
	private Label name;
	
	/** The image of the user. */
	private Embedded embImage;

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Component getContent() {
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSizeUndefined();
		horLayout.setHeight("100%");
		horLayout.setSpacing(true);
		horLayout.setSizeFull();
		
		VerticalLayout verLayout = new VerticalLayout();
		horLayout.addComponent(verLayout);
		horLayout.setComponentAlignment(verLayout, Alignment.MIDDLE_LEFT);
		
		name = new Label();
		verLayout.addComponent(name);
		
		Button btnLogout = new Button("Ausloggen");
		btnLogout.setStyleName(Reindeer.BUTTON_SMALL);
		btnLogout.addListener(new ClickListener() {
			
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				SessionHandler.logout();
				((MainWindow) getApplication().getMainWindow()).setLoginView();
			}
		});
		verLayout.addComponent(btnLogout);
		verLayout.setComponentAlignment(btnLogout, Alignment.BOTTOM_RIGHT);
		
		embImage = new Embedded();
		embImage.setHeight("40px");
		horLayout.addComponent(embImage);
		horLayout.setComponentAlignment(embImage, Alignment.MIDDLE_RIGHT);

		return horLayout;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		updateData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Tab getTab() {
		return new TabUser(this);
	}
	
	/**
	 * Updates the data in this component, i.e. name and image.
	 */
	public void updateData() {
		User user = SessionHandler.get();
		DBManager.getCommunicator().refresh(user);
		name.setValue(user.getFirstName() + " " + user.getLastName());
		embImage.setSource(FileUtil.getUserImage(user, getApplication()));
	}

}