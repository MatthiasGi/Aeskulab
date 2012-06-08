package de.gianfelice.aeskulab.ui.tabs;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.container.EventTreeContainer;
import de.gianfelice.aeskulab.data.entities.EventEntity;
import de.gianfelice.aeskulab.data.entities.EventGroup;
import de.gianfelice.aeskulab.ui.tabs.mainmenu.EventTree;
import de.gianfelice.aeskulab.ui.tabs.mainmenu.GlobalLayout;
import de.gianfelice.aeskulab.ui.tabs.mainmenu.LayoutEvent;
import de.gianfelice.aeskulab.ui.tabs.mainmenu.LayoutGroup;
import de.gianfelice.aeskulab.ui.tabs.mainmenu.ToolBar;

/**
 * This tab is used to manage {@link EventEntity} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 0.2.0
 */
@SuppressWarnings("deprecation")
public class TabMainMenu extends Tab {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The content. */
	private Panel content;
	
	/** The toolbar. */
	private ToolBar toolbar;
	
	/** The tree. */
	private EventTree tree;

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentContainer getContentContainer() {		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSizeFull();

		VerticalSplitPanel verSidebar = new VerticalSplitPanel();
		verSidebar.setWidth("300px");
		verSidebar.setHeight("100%");
		verSidebar.setSplitPosition(150, UNITS_PIXELS, true);
		verSidebar.setLocked(true);
		tree = new EventTree(this);
		verSidebar.addComponent(tree);
		verSidebar.addComponent(new GlobalLayout());
		horLayout.addComponent(verSidebar);
		
		VerticalLayout verContent = new VerticalLayout();
		verContent.setSizeFull();
		horLayout.addComponent(verContent);
		horLayout.setExpandRatio(verContent, 1.0f);
		
		toolbar = new ToolBar(this);
		verContent.addComponent(toolbar);
		
		content = new Panel();
		content.setSizeFull();
		verContent.addComponent(content);
		verContent.setExpandRatio(content, 1.0f);
		
		// Make useless
		setEnabled(false);
		VerticalLayout verWarning = new VerticalLayout();
		verWarning.setSizeFull();
		content.setContent(verWarning);
		Label lblWarning = new Label(
				"Diese Funktionalität steht z.Z. nicht zur Verfügung!");
		lblWarning.setStyleName(Reindeer.LABEL_H1);
		lblWarning.setSizeUndefined();
		verWarning.addComponent(lblWarning);
		verWarning.setComponentAlignment(lblWarning, Alignment.MIDDLE_CENTER);
		
		return horLayout;
	}
	
	/**
	 * Sets the content to {@code null}.
	 */
	public void setNull() {
		content.setContent(null);
	}
	
	/**
	 * Sets the current active event group.
	 *
	 * @param group The new event group
	 */
	public void setEventGroup(EventGroup group) {
		content.setContent(new LayoutGroup(group, this));
		toolbar.setActive(group);
	}
	
	/**
	 * Sets the current active event entity.
	 *
	 * @param entity The new event entity
	 */
	public void setEventEntity(EventEntity entity) {
		content.setContent(new LayoutEvent(entity, this));
		toolbar.setActive(entity);
	}
	
	/**
	 * Update the whole tree, which is very expensive.
	 */
	public void updateTree() {
		tree.readData();
	}
	
	/**
	 * Gets the container.
	 *
	 * @return The container
	 */
	public EventTreeContainer getContainer() {
		return (EventTreeContainer) tree.getContainerDataSource();
	}
	
	/**
	 * Adds an event group to the tree and selects it.
	 *
	 * @param group The group
	 */
	public void addEventGroup(EventGroup group) {
		tree.addEventGroup(group);
		tree.select(group);
		setEventGroup(group);
	}
	
	/**
	 * Adds an event entity to the tree and selects it.
	 *
	 * @param entity The entity
	 */
	public void addEventEntity(EventEntity entity) {
		tree.addEntity(entity);
		tree.select(entity);
		setEventEntity(entity);
	}
	
	/**
	 * Sorts the tree.
	 */
	public void sortTree() {
		tree.sort();
	}

}