package de.gianfelice.aeskulab.ui.tabs.mainmenu;

import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Tree.TreeTargetDetails;

import de.gianfelice.aeskulab.data.container.EventTreeContainer;
import de.gianfelice.aeskulab.data.entities.EventEntity;
import de.gianfelice.aeskulab.data.entities.EventGroup;

/**
 * Handles drop events on the {@link EventTree}.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     EventTree
 * @see     EventTreeContainer
 */
@SuppressWarnings({ "deprecation", "javadoc" })
public class EventTreeDropHandler implements DropHandler {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The tree. */
	private EventTree tree;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Instantiates the handler for the given tree.
	 *
	 * @param tree The tree
	 */
	public EventTreeDropHandler(EventTree tree) {
		this.tree = tree;
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public void drop(DragAndDropEvent event) {
		Transferable t = event.getTransferable();
		if (t.getSourceComponent() != tree
				|| !(t instanceof DataBoundTransferable)) return;

		Object sourceItem = ((DataBoundTransferable) t).getItemId();
		Object targetItem = ((TreeTargetDetails) event.getTargetDetails()).
				getItemIdInto();
		if (!(sourceItem instanceof EventGroup
				|| sourceItem instanceof EventEntity || targetItem == null
				|| targetItem instanceof EventGroup)) return;
		
		EventGroup target = null;
		if (targetItem != null) target = (EventGroup) targetItem;
		EventTreeContainer container = (EventTreeContainer)
				tree.getContainerDataSource();
		
		if (sourceItem instanceof EventGroup) {
			EventGroup source = (EventGroup) sourceItem;
			source.setParent(target);
		} else if (sourceItem instanceof EventEntity) {
			EventEntity source = (EventEntity) sourceItem;
			source.setEventGroup(target);
		}
		
		container.setParent(sourceItem, target);
		tree.sort();
	}

	/**
	 * {@inheritDoc}
	 */
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

}
