package de.gianfelice.aeskulab.data.table;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * Displays an icon which corresponds with the value of the column.
 *   
 * @author  Matthias Gianfelice
 * @version 1.0.3
 * @see     BooleanColumnGenerator
 */
public class NotNullColumnGenerator implements ColumnGenerator {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The application, which is used to load the icons. */
	private Application application;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the generator and sets the application.
	 * 
	 * @param application The application, which is used to load the icons
	 */
	public NotNullColumnGenerator(Application application) {
		this.application = application;
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public Embedded generateCell(Table source, Object item, Object column) {
		final Property prop = source.getItem(item).getItemProperty(column);
		final Embedded emb = new Embedded();
		emb.setWidth("16px");
		emb.setHeight("16px");
		setIcon(emb, prop);

		if (prop instanceof ValueChangeNotifier) {
			ValueChangeNotifier notifier = (ValueChangeNotifier) prop;
			notifier.addListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				public void valueChange(ValueChangeEvent event) {
					setIcon(emb, prop);
				}
			});
		}

		return emb;
	}

	/**
	 * Sets the icon of the {@link Embedded} object according to the value, that
	 * should be displayed.
	 * 
	 * @param emb  The object, that should be modified
	 * @param prop The property which should be interpreted
	 */
	private void setIcon(Embedded emb, Property prop) {
		String path = "res/";
		if (prop.getValue() != null) {
			path += "16/bullet_green.png";
		} else {
			path += "null.png";
		}
		emb.setSource(new ClassResource(path, application));
	}

}