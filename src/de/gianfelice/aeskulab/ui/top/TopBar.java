package de.gianfelice.aeskulab.ui.top;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

import de.gianfelice.aeskulab.ui.MainLayout;
import de.gianfelice.aeskulab.ui.top.MenuComponent.ComponentClickedListener;
import de.gianfelice.aeskulab.ui.top.buttons.MainMenu;
import de.gianfelice.aeskulab.ui.top.buttons.MenuLog;
import de.gianfelice.aeskulab.ui.top.buttons.MenuMap;
import de.gianfelice.aeskulab.ui.top.buttons.MenuUnits;
import de.gianfelice.aeskulab.ui.top.buttons.MenuWork;
import de.gianfelice.aeskulab.ui.top.buttons.UserInfo;

/**
 * The main menubar of this application.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.1
 * @see     MenuComponent
 */
public class TopBar extends HorizontalLayout
		implements ComponentClickedListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The currently active item. */
	private MenuComponent activeItem;
	
	/** The main layout of the application. */
	private MainLayout layout;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates and prepares the menubar and sets the layout.
	 * 
	 * @param layout The layout
	 */
	public TopBar(MainLayout layout) {
		this.layout = layout;
		addStyleName("a-topbar");
		setWidth("100%");
		setHeight("50px");

		activeItem = new MainMenu();
		activeItem.addStyleName("a-menucomponent-active");
		addMenuComponent(activeItem, this);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setHeight("100%");
		addComponent(horLayout);
		setComponentAlignment(horLayout, Alignment.MIDDLE_CENTER);
		setExpandRatio(horLayout, 1.0f);
		
		addMenuComponent(new MenuMap(layout), horLayout);
		addMenuComponent(new MenuUnits(), horLayout);
		addMenuComponent(new MenuWork(), horLayout);
		addMenuComponent(new MenuLog(), horLayout);
		
		addMenuComponent(new UserInfo(), this);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Adds the given {@link MenuComponent} to the given layout and sets a
	 * listener.
	 * 
	 * @param component The component
	 * @param parent    The layout
	 */
	private void addMenuComponent(MenuComponent component,
			ComponentContainer parent) {
		component.addListener(this);
		parent.addComponent(component);
	}

	/**
	 * {@inheritDoc}
	 */
	public void componentClicked(MenuComponent component) {
		if (activeItem == component) return;
		activeItem.removeStyleName("a-menucomponent-active");
		component.addStyleName("a-menucomponent-active");
		layout.setContentTab(component.getTab());
		activeItem = component;
	} 

}