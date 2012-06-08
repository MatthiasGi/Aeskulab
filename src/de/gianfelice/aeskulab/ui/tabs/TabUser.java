package de.gianfelice.aeskulab.ui.tabs;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import de.gianfelice.aeskulab.data.container.UserContainer;
import de.gianfelice.aeskulab.data.table.BooleanColumnGenerator;
import de.gianfelice.aeskulab.data.table.NotNullColumnGenerator;
import de.gianfelice.aeskulab.system.authentication.User;
import de.gianfelice.aeskulab.ui.tabs.user.AdministratorLayout;
import de.gianfelice.aeskulab.ui.tabs.user.DataLayout;
import de.gianfelice.aeskulab.ui.tabs.user.GlobalLayout;
import de.gianfelice.aeskulab.ui.tabs.user.ImageLayout;
import de.gianfelice.aeskulab.ui.top.MenuComponent;
import de.gianfelice.aeskulab.ui.top.buttons.UserInfo;

/**
 * This tab is used to manage user accounts.
 * 
 * @author  Matthias Gianfelice
 * @version 2.2.1
 */
public class TabUser extends Tab implements ItemClickListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** Table with all users. */
	private Table tblUser;
	
	/** Contains the user image. */
	private ImageLayout imgLayout;
	
	/** Contains names, email and password. */
	private DataLayout dataLayout;
	
	/** Contains a switch to set the administrator state. */
	private AdministratorLayout adminLayout;

	/** {@link MenuComponent} for updating, in case it should be needed. */
	private UserInfo userInfo;

	/** Layout for adding and removing users. */
	private GlobalLayout globLayout;

	/** Container with all user data. */
	private UserContainer container;

	/** The current user for this tab. */
	private Item item;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the tab and assigns the {@link MenuComponent}.
	 * 
	 * @param userInfo
	 *            The {@link MenuComponent} with user information
	 */
	public TabUser(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentContainer getContentContainer() {
		container = new UserContainer();

		HorizontalSplitPanel horSplit = new HorizontalSplitPanel();
		horSplit.setSizeFull();
		horSplit.setSplitPosition(65);

		tblUser = new Table();
		tblUser.setContainerDataSource(container);
		tblUser.setVisibleColumns(UserContainer.NATURAL_COLS);
		tblUser.setColumnHeaders(UserContainer.COL_HEADERS);
		tblUser.addListener((ItemClickListener) this);
		tblUser.setSizeFull();
		tblUser.setColumnReorderingAllowed(true);
		tblUser.setColumnCollapsingAllowed(true);
		tblUser.setSelectable(true);
		tblUser.setSortContainerPropertyId("lastName");
		horSplit.addComponent(tblUser);

		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSizeFull();
		horSplit.addComponent(verLayout);
		
		Panel panelUser = new Panel();
		panelUser.setSizeFull();
		VerticalLayout verUser = new VerticalLayout();
		verUser.setSizeUndefined();
		verUser.setWidth("100%");
		verUser.setSpacing(true);
		verUser.setMargin(true);
		panelUser.setContent(verUser);
		verLayout.addComponent(panelUser);
		verLayout.setExpandRatio(panelUser, 1.0f);
		
		imgLayout = new ImageLayout(this);
		verUser.addComponent(imgLayout);
		dataLayout = new DataLayout(this);
		verUser.addComponent(dataLayout);
		adminLayout = new AdministratorLayout();
		verUser.addComponent(adminLayout);
		
		globLayout = new GlobalLayout(this);
		verLayout.addComponent(globLayout);

		return horSplit;
	}

	/**
	 * Returns the container with user data.
	 * 
	 * @return The container, that manages user data
	 */
	public UserContainer getContainer() {
		return container;
	}

	/**
	 * Updates the information about the current selected {@link User}.
	 * 
	 * @param item The item, that should be set as current user
	 */
	public void setItem(Item item) {
		this.item = item;
		imgLayout.setItem(item);
		dataLayout.setItem(item);
		adminLayout.setItem(item);
		globLayout.setItem(item);
		tblUser.select(item.getItemProperty("id"));
	}

	/**
	 * Checks, whether the current user was updated and calls the
	 * {@link UserInfo} to update itself, if this check is true.
	 */
	public void activeUserUpdated() {
		if (UserContainer.isCurrentUser(item)) userInfo.updateData();
	}

	/**
	 * Reorders the table, which is useful if items were added to the container.
	 */
	public void reorderTable() {
		Object id = tblUser.getSortContainerPropertyId();
		tblUser.setSortContainerPropertyId(null);
		tblUser.setSortContainerPropertyId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		tblUser.addGeneratedColumn("image", new NotNullColumnGenerator(
				getApplication()));
		tblUser.setColumnWidth("image", 40);
		tblUser.setColumnAlignment("image", Table.ALIGN_CENTER);

		tblUser.addGeneratedColumn("admin", new BooleanColumnGenerator(
				getApplication()));
		tblUser.setColumnWidth("admin", 40);
		tblUser.setColumnAlignment("admin", Table.ALIGN_CENTER);

		tblUser.addGeneratedColumn("blocked", new BooleanColumnGenerator(
				getApplication()));
		tblUser.setColumnWidth("blocked", 40);
		tblUser.setColumnAlignment("blocked", Table.ALIGN_CENTER);

		setItem(container.getCurrentUser());
	}

	/**
	 * {@inheritDoc}
	 */
	public void itemClick(ItemClickEvent event) {
		setItem(event.getItem());
	}

}