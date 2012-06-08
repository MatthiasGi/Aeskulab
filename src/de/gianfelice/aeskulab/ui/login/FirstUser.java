package de.gianfelice.aeskulab.ui.login;

import java.util.Properties;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

import de.gianfelice.aeskulab.ui.MainWindow;

/**
 * This layout contains a wizard, which creates the first user for the system.
 * 
 * @author     Matthias Gianfelice
 * @version    1.2.3
 * @deprecated Will be removed in the next version and replaced by a whole new
 *             one paged wizard
 */
public class FirstUser extends VerticalLayout {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** A collection of the properties, that the user enters. */
	protected Properties props;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the layout and adds the wizard.
	 */
	public FirstUser() {

		// General options
		setSizeFull();
		props = new Properties();
		
		// Create the wizard and set settings
		Wizard wizard = new Wizard();
		wizard.getCancelButton().setVisible(false);
		wizard.getBackButton().setCaption("Zurück");
		wizard.getFinishButton().setCaption("Fertig stellen");
		wizard.getNextButton().setCaption("Weiter");
		wizard.getNextButton().setClickShortcut(KeyCode.ENTER);
		wizard.setWidth("800px");
		wizard.setHeight("500px");
		
		// Add the steps
		wizard.addStep(new Step1Name(this));
		wizard.addStep(new Step2Email(this));
		wizard.addStep(new Step3Control(this));
		
		// Add a listener to react on finishing the process
		wizard.addListener(new WizardProgressListener() {
			
			public void wizardCompleted(WizardCompletedEvent event) {
				((MainWindow) getApplication().getMainWindow()).setLoginView();
			}
			
			public void wizardCancelled(WizardCancelledEvent event) {}
			
			public void stepSetChanged(WizardStepSetChangedEvent event) {}
			
			public void activeStepChanged(WizardStepActivationEvent event) {}
			
		});
		
		// Add the wizard
		addComponent(wizard);
		setComponentAlignment(wizard, Alignment.MIDDLE_CENTER);
		
	}
	
}