package de.gianfelice.aeskulab.data.table;

import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * Displays a {@link List} object as simple, comma separated list.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class ListColumnGenerator implements ColumnGenerator {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public Label generateCell(Table source, Object item, Object column) {
		final Label label = new Label();
		final Property prop = source.getItem(item).getItemProperty(column);
		setQuals(label, prop);

		if (prop instanceof ValueChangeNotifier) {
			ValueChangeNotifier notifier = (ValueChangeNotifier) prop;
			notifier.addListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				public void valueChange(ValueChangeEvent event) {
					setQuals(label, prop);
				}
			});
		}

		return label;
	}
	
	/**
	 * Updates the content.
	 * 
	 * @param label The label, that should be modified
	 * @param prop  The property which should be interpreted
	 */
	private void setQuals(Label label, Property prop) {
		String quals = "";
		Object val = prop.getValue();
		if (val instanceof List) {
			List<?> list = (List<?>) val;
			if (list.size() > 0) {
				for (int i = 0; i < list.size() - 1; i++)
					quals += list.get(i).toString() + ", ";
				quals += list.get(list.size() - 1).toString();
			}
		}
		label.setValue(quals);
	}

}