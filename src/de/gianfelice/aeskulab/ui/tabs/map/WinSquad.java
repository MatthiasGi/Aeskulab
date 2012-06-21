package de.gianfelice.aeskulab.ui.tabs.map;

import java.io.File;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Audio;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.data.entities.Qualification;
import de.gianfelice.aeskulab.data.entities.Squad;
import de.gianfelice.aeskulab.ui.tabs.TabMap;

/**
 * A popup window to display information about a squad.
 * 
 * @author  Matthias Gianfelice
 * @version 0.1.1
 * @see     Squad
 */
public class WinSquad extends HorizontalLayout implements ValueChangeListener {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The selected squad. */
	private Squad squad;
	
	/** An image of the squad. */
	private Embedded emb;
	
	/** The main vertical layout. */
	private VerticalLayout verLayout;
	
	/** The parent tab for update-events. */
	private TabMap tab;
	
	/** A slider to set the state of the squad. */
	private Slider state;
	
	/** An audio component. */
	private Audio audio;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the window and sets the parent tab and squad.
	 * 
	 * @param tab   The parent tab
	 * @param squad The squad
	 */
	public WinSquad(TabMap tab, Squad squad) {
		this.tab = tab;
		this.squad = squad;
		
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		emb = new Embedded();
		emb.setWidth("50px");
		emb.setHeight("30px");
		addComponent(emb);
		
		verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		verLayout.setMargin(false, false, false, true);
		addComponent(verLayout);
		setExpandRatio(verLayout, 1.0f);
		
		Label lblName = new Label(squad.getName());
		lblName.setStyleName(Reindeer.LABEL_H1);
		verLayout.addComponent(lblName);
		verLayout.addComponent(new Label(squad.getType().getName()));

		state = new Slider(0, 9);
		state.setOrientation(Slider.ORIENTATION_VERTICAL);
		state.setImmediate(true);
		state.addListener(this);
		try {
			state.setValue(squad.getState());
		} catch (Exception e) {}
		addComponent(state);
		
		audio = new Audio();
		addComponent(audio);
		audio.setWidth("0px");
		audio.setHeight("0px");
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach() {
		super.attach();
		Application app = getApplication();
		emb.setSource(new ClassResource("res/tac/trupp.png", app));
		audio.setSource(new ClassResource("res/alarm.mp3", app));
		
		List<Helper> lst = squad.getHelpers();
		for (Helper h : lst) {
			Panel panel = new Panel();
			verLayout.addComponent(panel);
			
			HorizontalLayout horContent = new HorizontalLayout();
			horContent.setMargin(true);
			horContent.setSpacing(true);
			panel.setContent(horContent);
			
			Embedded emb = new Embedded();
			File img = h.getImage();
			if (img == null) {
				emb.setSource(new ClassResource("res/150/transformer.png",
						app));
			} else {
				emb.setSource(new FileResource(img, app));
			}
			emb.setWidth("50px");
			horContent.addComponent(emb);
			
			VerticalLayout verContent = new VerticalLayout();
			verContent.setSpacing(true);
			horContent.addComponent(verContent);
			
			Label lblName = new Label(h.getFirstName() + " " + h.getLastName());
			lblName.setStyleName(Reindeer.LABEL_H2);
			verContent.addComponent(lblName);
			verContent.addComponent(new Label(h.getTelNumber()));
			
			String quals = "";
			List<Qualification> qs = h.getQualifications();
			for (int i = 1; i < qs.size(); i++) quals += qs.get(i - 1) + ", ";
			Label lblQuals = new Label(quals + qs.get(qs.size() - 1));
			lblQuals.setStyleName(Reindeer.LABEL_SMALL);
			verContent.addComponent(lblQuals);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		int state = ((Double) this.state.getValue()).intValue();
		squad.setState(state);
		tab.updateState(squad, state);
		if (state == 0) audio.play();
	}
	
}