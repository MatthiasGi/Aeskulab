package de.gianfelice.aeskulab.ui.top;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import de.gianfelice.aeskulab.ui.tabs.Tab;

/**
 * A component that is displayed in the {@link TopBar}.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.2
 * @see     TopBar
 */
public abstract class MenuComponent extends CustomComponent
		implements LayoutClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** Contains all listeners. */
	private List<ComponentClickedListener> listeners;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates and prepares the component.
	 */
	public MenuComponent() {
		listeners = new ArrayList<ComponentClickedListener>();
		setStyleName("a-menucomponent");
		setSizeUndefined();
		setHeight("100%");
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSizeFull();
		Component component = getContent();
		
		verLayout.addComponent(component);
		verLayout.setMargin(false, true, false, true);
		verLayout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
		verLayout.addListener(this);
		
		setCompositionRoot(verLayout);
	}
	
	/**
	 * Adds a listener to the internal list.
	 * 
	 * @param listener The listener, that should be added
	 */
	public void addListener(ComponentClickedListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener from the internal list.
	 *
	 * @param listener The listener, that should be removed
	 */
	public void removeListener(ComponentClickedListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Returns the content, that should be displayed inside the component.
	 * 
	 * @return The content of the component
	 */
	protected abstract Component getContent();
	
	/**
	 * Returns the {@link Tab}, that fits to the component.
	 * 
	 * @return The specific {@link Tab}
	 */
	protected abstract Tab getTab();
	
	/**
	 * {@inheritDoc}
	 */
	public void layoutClick(LayoutClickEvent event) {
		for (ComponentClickedListener listener : listeners) {
			listener.componentClicked(this);
		}
	}
	
	// ---------------------------- Inner Class(es) ----------------------------
	/**
	 * A simple interface, that provides a listener for clicking on the
	 * component.
	 * 
	 * @author  Matthias Gianfelice
	 * @version 1.0.0
	 */
	public interface ComponentClickedListener {
		
		/**
		 * Will be called, if the user clicks on the component.
		 * 
		 * @param component The component, that was clicked
		 */
		public void componentClicked(MenuComponent component);
		
	}

}