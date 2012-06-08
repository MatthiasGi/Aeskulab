package de.gianfelice.aeskulab.ui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

import de.gianfelice.aeskulab.data.container.QualificationContainer;
import de.gianfelice.aeskulab.data.entities.Qualification;

/**
 * A field to manage all qualifications linked to an object.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Qualification
 */
public class QualificationField extends TagField implements ClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The actual tagfield. */
	private TagField tags;
	
	/** A button to edit all qualifications. */
	private Button btnEdit;
	
	/** The container. */
	private QualificationContainer container;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the field with a new {@link QualificationContainer}.
	 */
	public QualificationField() {
		this(new QualificationContainer());
	}
	
	/**
	 * Creates the field with the given container.
	 * 
	 * @param container The container
	 */
	public QualificationField(QualificationContainer container) {
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setWidth("100%");
		setCompositionRoot(horLayout);
		
		this.container = container;		
		tags = new QualificationTagField();
		tags.setContainerDataSource(container);
		tags.setNewTokensAllowed(true);
		tags.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
		horLayout.addComponent(tags);
		horLayout.setExpandRatio(tags, 1.0f);
		
		btnEdit = new Button();
		btnEdit.setDescription("Qualifikationen bearbeiten");
		btnEdit.addListener((ClickListener) this);
		horLayout.addComponent(btnEdit);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		btnEdit.setIcon(new ClassResource("res/16/pencil_go.png",
				getApplication()));
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		getApplication().getMainWindow().addWindow(new QualificationWindow(
				container));
	}
	
	/**
	 * Returns the actual tag field.
	 * 
	 * @return The tag field
	 */
	public TagField getTagField() {
		return tags;
	}
	
	/**
	 * Returns all selected qualifications.
	 * 
	 * @return All qualifications
	 */
	public List<Qualification> getQualifications() {
		List<Qualification> quals = new ArrayList<Qualification>();
		Set<?> set = (Set<?>) tags.getValue();
		if (set == null) return null;
		for (Object obj : set) quals.add(container.getItem(obj).getBean());
		return quals;
	}
	
	/**
	 * Sets the qualifications.
	 * 
	 * @param quals The new qualifications
	 */
	public void setQualifications(List<Qualification> quals) {
		for (Qualification qual : quals) tags.addToken(qual.getName());
	}
	
	// ---------------------------- Inner Class(es) ----------------------------
	/**
	 * Extends the tag field to handle {@link Qualification} objects properly.
	 * 
	 * @author  Matthias Gianfelice
	 * @version 1.0.0
	 */
	private class QualificationTagField extends TagField {

		// ---------------------------- Attribute(s) ---------------------------
		/** The default serial version id. */
		private static final long serialVersionUID = 1L;
		
		// ----------------------------- Method(s) -----------------------------
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onTokenInput(Object tokenId) {
			Set<?> set = (Set<?>) getValue();
            if (set != null && set.contains(tokenId)) {
            	return;
            } else {
            	if (container.containsId(tokenId)) {
            		addToken(tokenId);
            	} else {
            		getApplication().getMainWindow().addWindow(
            				new QualificationWindow(tokenId.toString(),
            						container));
                }
            }
		}
		
	}

}