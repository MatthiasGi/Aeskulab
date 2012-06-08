package de.gianfelice.aeskulab.ui.tabs.mainmenu;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * A large, special button with icon that fits to the application.
 * 
 * @author     Matthias Gianfelice
 * @version    1.0.0
 * @deprecated Will be unnecessary after recreating the main menu
 */
public class ListButton extends CustomComponent implements LayoutClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** The icon. */
	private Embedded embIcon;
	
	/** The title. */
	private Label lblTitle;
	
	/** The listeners. */
	private List<ButtonClickedListener> listeners =
			new ArrayList<ListButton.ButtonClickedListener>();
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates and prepares a new list button.
	 */
	public ListButton() {
		setStyleName("a-listcomponent");		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setHeight("50px");
		horLayout.setSpacing(true);
		horLayout.addListener(this);
		setCompositionRoot(horLayout);
		
		VerticalLayout verImage = new VerticalLayout();
		verImage.setHeight("100%");
		verImage.setMargin(false, true, false, true);
		verImage.setStyleName("a-listcomponent-image");
		horLayout.addComponent(verImage);

		embIcon = new Embedded();
		embIcon.setWidth("30px");
		embIcon.setHeight("30px");
		verImage.addComponent(embIcon);
		verImage.setComponentAlignment(embIcon, Alignment.MIDDLE_CENTER);

		lblTitle = new Label();
		horLayout.addComponent(lblTitle);
		horLayout.setComponentAlignment(lblTitle, Alignment.MIDDLE_LEFT);
		horLayout.setExpandRatio(lblTitle, 1.0f);
	}
	
	/**
	 * Creates and prepares a new list button and adds the given title to it.
	 *
	 * @param title The title
	 */
	public ListButton(String title) {
		this();
		setTitle(title);
	}
	
	/**
	 * Creates and prepares a new list button and adds the given title and
	 * listener to it.
	 *
	 * @param title    The title
	 * @param listener The listener
	 */
	public ListButton(String title, ButtonClickedListener listener) {
		this(title);
		addListener(listener);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Sets the source of the icon.
	 *
	 * @param source The new source
	 */
	public void setSource(Resource source) {
		embIcon.setSource(source);
	}

	/**
	 * Sets the title.
	 *
	 * @param title The new title
	 */
	public void setTitle(String title) {
		lblTitle.setValue(title);
	}

	/**
	 * Adds a listener.
	 *
	 * @param listener The listener
	 */
	public void addListener(ButtonClickedListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener.
	 *
	 * @param listener The listener
	 */
	public void removeListener(ButtonClickedListener listener) {
		listeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void layoutClick(LayoutClickEvent event) {
		for (ButtonClickedListener listener : listeners) {
			listener.buttonClicked(this);
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
	public interface ButtonClickedListener {
		
		/**
		 * Will be called, if the user clicks on the button.
		 * 
		 * @param button The button, that was clicked
		 */
		public void buttonClicked(ListButton button);
		
	}

}