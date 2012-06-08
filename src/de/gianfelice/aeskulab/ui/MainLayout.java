package de.gianfelice.aeskulab.ui;

import com.vaadin.ui.VerticalLayout;

import de.gianfelice.aeskulab.ui.tabs.Tab;
import de.gianfelice.aeskulab.ui.tabs.TabMap;
import de.gianfelice.aeskulab.ui.top.TopBar;

/**
 * The main layout of the application, which contains the view for an inlogged
 * user.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     MainWindow
 */
public class MainLayout extends VerticalLayout {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A layout which contains the page content. */
	private VerticalLayout verLayout;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and prepares it.
	 */
	public MainLayout() {
		setSizeFull();
		addComponent(new TopBar(this));
		verLayout = new VerticalLayout();
		verLayout.setSizeFull();
		addComponent(verLayout);
		setExpandRatio(verLayout, 1.0f);
	}
	
	// ------------------------------- Method(s) -------------------------------	
	/**
	 * Updates the content of the page.
	 * 
	 * @param tab The new content
	 */
	public void setContentTab(Tab tab) {
		verLayout.removeAllComponents();
		verLayout.addComponent(tab);
		tab.setSizeFull();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		
		// TODO: Temporary
		// setContentTab(new TabMainMenu());
		setContentTab(new TabMap());
	}

}