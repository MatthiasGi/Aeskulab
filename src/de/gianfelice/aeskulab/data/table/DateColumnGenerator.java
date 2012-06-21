package de.gianfelice.aeskulab.data.table;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * Displays a date in a tactical correct way.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public class DateColumnGenerator implements ColumnGenerator {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The basic date format. */
	private SimpleDateFormat format;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the generator.
	 */
	public DateColumnGenerator() {
		format = new SimpleDateFormat("ddHHmm'a'MMMyy");
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Object o = source.getItem(itemId).getItemProperty(columnId).getValue();
		if (!(o instanceof Date)) return null;
		return format.format((Date) o);
	}

}