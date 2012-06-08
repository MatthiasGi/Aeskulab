package de.gianfelice.aeskulab.ui.components;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;

/**
 * A simple button that can be connected to TextFields to be enabled as soon as
 * they change their input.
 * 
 * @author     Matthias Gianfelice
 * @version    1.0.0
 * @deprecated Will be removed for stable version.
 */
public class SubmitButton extends Button implements TextChangeListener {
	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates a new submit button.
	 */
	public SubmitButton() {
		super();
		setDisableOnClick(true);
	}
	
	/**
	 * Creates a new submit button with the given caption.
	 * 
	 * @param caption Caption for the button
	 */
	public SubmitButton(String caption) {
		super(caption);
		setDisableOnClick(true);
	}
	
	/**
	 * Creates a new submit button with the given caption and assigns a
	 * click-listener.
	 * 
	 * @param caption  Caption for the button
	 * @param listener Click-listener for the button
	 */
	public SubmitButton(String caption, ClickListener listener) {
		super(caption, listener);
		setDisableOnClick(true);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public void attach() {
		setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	public void textChange(TextChangeEvent event) {
		setEnabled(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Class<Boolean> getType() {
        return Boolean.class;
    }
	
}