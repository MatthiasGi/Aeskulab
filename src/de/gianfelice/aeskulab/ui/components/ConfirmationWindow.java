package de.gianfelice.aeskulab.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

/**
 * A window that asks the user for confirmation of the current action. Simple
 * container to let all windows look the same, nearly everything can be accessed
 * via getters.
 * 
 * @author  Matthias Gianfelice
 * @version 0.2.1
 */
public class ConfirmationWindow extends Window implements ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The button to confirm the dialog. */
	private Button btnOk;
	
	/** The button to cancel the dialog. */
	private Button btnCancel;
	
	/** A list of all cancel listeners. */
	private List<ButtonCancelListener> btnCancelListeners =
			new ArrayList<ButtonCancelListener>();
	
	/** A list of all ok listeners. */
	private List<ButtonOkListener> btnOkListeners =
			new ArrayList<ButtonOkListener>();
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the dialog with given text and title. HTML is not allowed.
	 * 
	 * @param title The title of the dialog
	 * @param text  The text to be shown inside the dialog
	 * @see         #ConfirmationWindow(String, String, boolean)
	 */
	public ConfirmationWindow(String title, String text) {
		this(title, text, false);
	}
	
	/**
	 * Creates the dialog with given text and title.
	 * 
	 * @param title       The title of the dialog
	 * @param text        The text to be shown inside the dialog
	 * @param HTMLAllowed Whether HTML should be allowed for the text
	 * @see               #ConfirmationWindow(String, String)
	 */
	public ConfirmationWindow(String title, String text, boolean HTMLAllowed) {
		super(title);
		setModal(true);
		setStyleName(Reindeer.WINDOW_LIGHT);
		setResizable(false);
		setDraggable(false);
		setWidth("300px");
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSizeFull();
		verLayout.setSpacing(true);
		verLayout.setMargin(true);
		setContent(verLayout);
		
		Label lblText = new Label(text);
		if (HTMLAllowed) lblText.setContentMode(Label.CONTENT_XHTML);
		verLayout.addComponent(lblText);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		verLayout.addComponent(horLayout);
		verLayout.setComponentAlignment(horLayout, Alignment.BOTTOM_RIGHT);
		
		btnCancel = new Button("Abbrechen");
		btnCancel.setStyleName(Reindeer.BUTTON_LINK);
		btnCancel.addListener(this);
		horLayout.addComponent(btnCancel);
		horLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		
		btnOk = new Button("OK");
		btnOk.setStyleName(Reindeer.BUTTON_DEFAULT);
		btnOk.setClickShortcut(KeyCode.ENTER);
		btnOk.addListener(this);
		horLayout.addComponent(btnOk);
	}
	
	/**
	 * Creates the dialog with given text and title and adds a listener. HTML is
	 * not allowed.
	 * 
	 * @param title    The title of the dialog
	 * @param text     The text to be shown inside the dialog
	 * @param listener The listener that should be added 
	 * @see            #ConfirmationWindow(String, String)
	 * @see            #ConfirmationWindow(String, String, boolean,
	 *                 ButtonOkListener)
	 */
	public ConfirmationWindow(String title, String text,
			ButtonOkListener listener) {
		this(title, text);
		addListener(listener);
	}
	
	/**
	 * Creates the dialog with given text and title and adds a listener.
	 * 
	 * @param title       The title of the dialog
	 * @param text        The text to be shown inside the dialog
	 * @param HTMLallowed Whether HTML should be allowed for the text
	 * @param listener    The listener that should be added
	 * @see               #ConfirmationWindow(String, String, boolean)
	 * @see               #ConfirmationWindow(String, String, ButtonOkListener)
	 */
	public ConfirmationWindow(String title, String text, boolean HTMLallowed,
			ButtonOkListener listener) {
		this(title, text, HTMLallowed);
		addListener(listener);
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnCancel) {
			
			if (btnCancelListeners.isEmpty()) {
				close();
			} else {
				for (ButtonCancelListener listener : btnCancelListeners) {
					if (listener.cancelButtonClicked()) close();
				}
			}
			
		} else if (event.getButton() == btnOk) {
			
			if (btnOkListeners.isEmpty()) {
				close();
			} else {
				for (ButtonOkListener listener : btnOkListeners) {
					if (listener.okButtonClicked()) close();
				}
			}
			
		}
	}
	
	/**
	 * Adds a listener to the submit-button.
	 * 
	 * @param listener The listener to add
	 */
	public void addListener(ButtonOkListener listener) {
		btnOkListeners.add(listener);
	}
	
	/**
	 * Removes a listener of the submit-button.
	 * 
	 * @param listener The listener, that should be removed
	 */
	public void removeListener(ButtonOkListener listener) {
		btnOkListeners.remove(listener);
	}
	
	/**
	 * Adds a listener to the cancel-button.
	 * 
	 * @param listener The listener to add
	 */
	public void addListener(ButtonCancelListener listener) {
		btnCancelListeners.add(listener);
	}
	
	/**
	 * Removes a listener from the cancel-button.
	 * 
	 * @param listener The listener, that should be removed
	 */
	public void removeListener(ButtonCancelListener listener) {
		btnCancelListeners.remove(listener);
	}
	
	/**
	 * Changes the caption of the default button.
	 * 
	 * @param caption The new caption for the button
	 */
	public void setOkButtonCaption(String caption) {
		btnOk.setCaption(caption);
	}
	
	/**
	 * Changes the caption of the cancel-button.
	 * 
	 * @param caption The new caption for the button
	 */
	public void setCancelButtonCaption(String caption) {
		btnCancel.setCaption(caption);
	}

	// --------------------------- Inner Interface(s) --------------------------
	/**
	 * This interface will be triggered, when the submit-button was clicked.
	 * 
	 * @author  Matthias Gianfelice
	 * @version 1.0.0
	 */
	public interface ButtonOkListener {
		
		// ----------------------------- Method(s) -----------------------------
		/**
		 * Will be executed, when the submit-button was clicked.
		 * 
		 * @return Whether the confirmation should be closed
		 */
		public boolean okButtonClicked();
		
	}
	
	/**
	 * This interface will be triggered, when the cancel-button was clicked.
	 * 
	 * @author  Matthias Gianfelice
	 * @version 1.0.0
	 */
	public interface ButtonCancelListener {
		
		// ----------------------------- Method(s) -----------------------------
		/**
		 * Will be executed, when the cancel-button was clicked.
		 * 
		 * @return Whether the confirmation should be closed
		 */
		public boolean cancelButtonClicked();
		
	}

}