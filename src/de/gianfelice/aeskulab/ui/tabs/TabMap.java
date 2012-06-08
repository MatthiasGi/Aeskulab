package de.gianfelice.aeskulab.ui.tabs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.vaadin.Application;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.system.utils.FileUtil;
import de.gianfelice.aeskulab.ui.components.Unit;
import de.gianfelice.aeskulab.ui.tabs.map.SquadLayout;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.DDAbsoluteLayout.AbsoluteLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * This tab offers a tactical overview.
 * 
 * @author  Matthias Gianfelice
 * @version 0.0.1
 */
public class TabMap extends Tab implements Receiver, FailedListener,
		FinishedListener, ProgressListener, StartedListener, DropHandler {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** Content panel. */
	private HorizontalSplitPanel horPanel;
	
	/** Accordion. */
	private Accordion acc;
	
	/** Toolbar. */
	private HorizontalLayout horToolbar;
	
	/** Map-Layout. */
	private DDAbsoluteLayout ddAbsMap;
	
	/** Replace map. */
	private Upload upload;
	
	/** Upload progress. */
	private ProgressIndicator progress;
	
	/** Cancel upload. */
	private Button btnCancel;
	
	/** Map. */
	private Embedded map;
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentContainer getContentContainer() {
		horPanel = new HorizontalSplitPanel();
		horPanel.setSplitPosition(70);
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSizeFull();
		horPanel.addComponent(verLayout);
		acc = new Accordion();
		acc.setSizeFull();
		horPanel.addComponent(acc);
		
		horToolbar = new HorizontalLayout();
		horToolbar.setWidth("100%");
		horToolbar.setMargin(true);
		horToolbar.setSpacing(true);
		horToolbar.setStyleName(Reindeer.LAYOUT_BLUE);
		verLayout.addComponent(horToolbar);
		Panel panel = new Panel();
		panel.setSizeFull();
		verLayout.addComponent(panel);
		verLayout.setExpandRatio(panel, 1.0f);
		ddAbsMap = new DDAbsoluteLayout();
		ddAbsMap.setDragMode(LayoutDragMode.CLONE);
		ddAbsMap.setDropHandler(this);
		panel.setContent(ddAbsMap);
		
		upload = new Upload();
		upload.setButtonCaption("Karte ersetzen");
		upload.setReceiver(this);
		upload.setImmediate(true);
		upload.addListener((StartedListener) this);
		upload.addListener((ProgressListener) this);
		upload.addListener((FinishedListener) this);
		upload.addListener((FailedListener) this);
		horToolbar.addComponent(upload);
		
		progress = new ProgressIndicator();
		progress.setVisible(false);
		progress.setPollingInterval(500);
		horToolbar.addComponent(progress);
		horToolbar.setComponentAlignment(progress, Alignment.MIDDLE_LEFT);
		btnCancel = new Button("Abbrechen");
		btnCancel.setStyleName(Reindeer.BUTTON_SMALL);
		btnCancel.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				upload.interruptUpload();
			}
		});
		btnCancel.setVisible(false);
		horToolbar.addComponent(btnCancel);
		horToolbar.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		horToolbar.setExpandRatio(btnCancel, 1.0f);
		
		map = new Embedded();
		ddAbsMap.addComponent(map);
		
		return horPanel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		
		Application app = getApplication();
		acc.addTab(new SquadLayout(), "Trupps",
				new ClassResource("res/16/group.png", app));
		acc.addTab(new Label("Fahrzeuge"), "Fahrzeuge",
				new ClassResource("res/16/lorry.png", app));
		acc.addTab(new Label("Helfer"), "Helfer",
				new ClassResource("res/16/user.png", app));
		acc.addTab(new Label("Orte"), "Orte",
				new ClassResource("res/16/map.png", app));
		updateMap();
		
		Unit u = new Unit();
		ddAbsMap.addComponent(u);
		u.setName("Gianfelice");
		u.setTac("person");
		u = new Unit();
		ddAbsMap.addComponent(u);
		u.setName("Fitzon");
		u.setTac("person");
	}

	/**
	 * {@inheritDoc}
	 */
	public OutputStream receiveUpload(String filename, String mimeType) {
		String ext = filename.substring(filename.lastIndexOf('.'));
		String[] exts = new String[]{".jpg", ".jpeg", ".JPG", ".JPEG"};
		if (!Arrays.asList(exts).contains(ext)) {
			getApplication().getMainWindow().showNotification("Es können " +
					"leider nur JPEGs (*.jpg, *.jpeg, *.JPG, *.JPEG) " +
					"angenommen werden!", Notification.TYPE_ERROR_MESSAGE);
			upload.interruptUpload();
		}
		try {
			return new FileOutputStream(FileUtil.getFile("map.jpg"));
		} catch (Exception e) {
			getApplication().getMainWindow().showNotification("Leider hat " +
					"die Anwendung keinen Zugriff auf das Arbeitsverzeichnis!",
					Notification.TYPE_ERROR_MESSAGE);
			upload.interruptUpload();
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadStarted(StartedEvent event) {
		upload.setVisible(false);
		progress.setValue(0f);
		progress.setVisible(true);
		btnCancel.setVisible(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateProgress(long readBytes, long contentLength) {
		progress.setValue(new Float(readBytes / (float) contentLength));
	}
	
	/**
	 * Updates the map.
	 */
	private void updateMap() {
		Application app = getApplication();
		try {
			File img = FileUtil.getFile("map.jpg");
			BufferedImage buffImg = ImageIO.read(img);
			float width = new Float(buffImg.getWidth());
			float height = new Float(buffImg.getHeight());
			map.setSource(new FileResource(FileUtil.getFile("map.jpg"), app));
			map.setWidth(width, UNITS_PIXELS);
			map.setHeight(height, UNITS_PIXELS);
			ddAbsMap.setWidth(width, UNITS_PIXELS);
			ddAbsMap.setHeight(height, UNITS_PIXELS);
		} catch (IOException e) {
			app.getMainWindow().showNotification(
					"Die Karte konnte nicht geladen werden!",
					Notification.TYPE_ERROR_MESSAGE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadFinished(FinishedEvent event) {
		upload.setVisible(true);
		progress.setVisible(false);
		btnCancel.setVisible(false);
		updateMap();
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadFailed(FailedEvent event) {
		getApplication().getMainWindow().showNotification(
				"Der Upload wurde abgebrochen!",
				Notification.TYPE_WARNING_MESSAGE);
	}

	/**
	 * {@inheritDoc}
	 */
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	/**
	 * {@inheritDoc}
	 */
	public void drop(DragAndDropEvent event) {
		LayoutBoundTransferable trans = (LayoutBoundTransferable)
				event.getTransferable();
		Component com = trans.getComponent();
		if (!(com instanceof Unit)) return;
		
		AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails)
				event.getTargetDetails();
		int left = details.getRelativeLeft();
		int top = details.getRelativeTop();
		ComponentPosition pos = ddAbsMap.getPosition(com);
		
		pos.setLeft((float) left, UNITS_PIXELS);
		pos.setTop((float) top, UNITS_PIXELS);
	}

}