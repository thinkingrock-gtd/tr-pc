/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.actn.recurrence.modify;

import au.com.trgtd.tr.util.Utils;
import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.action.Recurrence;
import tr.model.action.Recurrence.Basis;
import tr.model.action.RecurrenceRemover;
import tr.model.project.Project;

/**
 * Modify recurrence wizard.
 * 
 * @author <a href="mailto:jeremy@thinkingrock.com.au">Jeremy Moore</a>
 */
public class ModifyRecurrenceWizard {

    private static final Logger LOG = Logger.getLogger("tr.view.actn");
    private Data data;
    private Action action;
    private WizardDescriptor wizardDescriptor;

    /**
     * Starts the modify recurrence wizard.
     * @param action The action that has the recurrence.
     */
    public void start(Action action) {
        data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        this.action = action;
        if (action == null) {
            return;
        }
        if (!action.isStateScheduled()) {
            return;
        }
        Recurrence recurrence = action.getRecurrence();
        if (recurrence == null) {
            return;
        }
        WizardDescriptor.Iterator iterator = new ModifyRecurrenceWizardIterator(action, recurrence);
        wizardDescriptor = new WizardDescriptor(iterator);
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle(org.openide.util.NbBundle.getMessage(ModifyRecurrenceWizard.class, "modify.recurrence"));
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        wizardDescriptor.putProperty("dialog", dialog); // need owner for period dialogs        

        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (cancelled) {
            return;
        }
        Modification modification = (Modification) wizardDescriptor.getProperty(Modification.PROPERTY);
        switch (modification) {
            case CHANGE_PROJECT:
                changeProject();
                break;
            case CHANGE_ACTION:
                changeAction();
                break;
            case CHANGE_RECURRENCE:
                changeRecurrence();
                break;
            case REMOVE_RECURRENCE:
                removeRecurrence();
                break;
        }
    }

    private void changeProject() {
        LOG.info("Begin");

        Project newProject = (Project) wizardDescriptor.getProperty("mod.project.newProject");
        if (newProject == null) {
            newProject = data.getRootActions(); // single actions project 

        }

        Boolean moveAll = (Boolean) wizardDescriptor.getProperty("mod.project.isMoveAll");
        if (moveAll == null) {
            LOG.severe("Move all property was not found.");
            return;
        }

        Date moveDate = null;
        if (!moveAll) {
            moveDate = (Date) wizardDescriptor.getProperty("mod.project.moveDate");
            if (moveDate == null) {
                LOG.severe("Move date property was not found.");
                return;
            }
        }

        Recurrence recurrence = action.getRecurrence();

        Project oldProject = recurrence.getProject();

        recurrence.setProject(newProject);

        LOG.log(Level.INFO, "Old Project: {0}", oldProject);
        LOG.log(Level.INFO, "New Project: {0}", newProject);

        if (moveAll) {
            LOG.info("move all recurrence actions");
            recurrence.moveRecurrenceActions(oldProject);
        } else {
            LOG.log(Level.INFO, "remove recurrence from actions before {0}", moveDate);            
            RecurrenceRemover.remove(data, recurrence.getID(), moveDate);            

            LOG.log(Level.INFO, "move recurrence actions from {0}", moveDate);
            recurrence.moveRecurrenceActions(moveDate, oldProject);
        }

        LOG.info("End");
    }

    private void changeAction() {
        LOG.info("Begin");

        Recurrence changedRecurrence = (Recurrence) wizardDescriptor.getProperty("mod.action.recurrence");
        if (changedRecurrence == null) {
            LOG.severe("Recurrence property for action change was not found.");
            return;
        }
        Boolean update = (Boolean) wizardDescriptor.getProperty("mod.action.isUpdate");
        if (update == null) {
            LOG.severe("Update property for action change was not found.");
            return;
        }
        Boolean updateAll = (Boolean) wizardDescriptor.getProperty("mod.action.isUpdateAll");
        if (update && updateAll == null) {
            LOG.severe("Update All property for action change was not found.");
            return;
        }
        Date updateDate = (Date) wizardDescriptor.getProperty("mod.action.updateDate");
        if (update && !updateAll && updateDate == null) {
            LOG.severe("Update Date property for action change was not found.");
            return;
        }

        Recurrence recurrence = action.getRecurrence();

        List<Recurrence.Property> changedProperties = new ArrayList<Recurrence.Property>();
        
        if (!Utils.equal(recurrence.getScheduleHours(), changedRecurrence.getScheduleHours())) {
            changedProperties.add(Recurrence.Property.SCHEDULED_HOURS);
        }
        if (!Utils.equal(recurrence.getScheduleMins(), changedRecurrence.getScheduleMins())) {
            changedProperties.add(Recurrence.Property.SCHEDULED_MINS);
        }
        if (!Utils.equal(recurrence.getDurationHours(), changedRecurrence.getDurationHours())) {
            changedProperties.add(Recurrence.Property.DURATION_HOURS);
        }
        if (!Utils.equal(recurrence.getDurationMins(), changedRecurrence.getDurationMins())) {
            changedProperties.add(Recurrence.Property.DURATION_MINS);
        }
        if (!Utils.equal(recurrence.getDescription(), changedRecurrence.getDescription())) {
            changedProperties.add(Recurrence.Property.DESCRIPTION);
        }
        if (!Utils.equal(recurrence.getSuccess(), changedRecurrence.getSuccess())) {
            changedProperties.add(Recurrence.Property.SUCCESS);
        }
        if (!Utils.equal(recurrence.getContext(), changedRecurrence.getContext())) {
            changedProperties.add(Recurrence.Property.CONTEXT);
        }
        if (!Utils.equal(recurrence.getTopic(), changedRecurrence.getTopic())) {
            changedProperties.add(Recurrence.Property.TOPIC);
        }
        if (!Utils.equal(recurrence.getTime(), changedRecurrence.getTime())) {
            changedProperties.add(Recurrence.Property.TIME);
        }
        if (!Utils.equal(recurrence.getEnergy(), changedRecurrence.getEnergy())) {
            changedProperties.add(Recurrence.Property.ENERGY);
        }
        if (!Utils.equal(recurrence.getPriority(), changedRecurrence.getPriority())) {
            changedProperties.add(Recurrence.Property.PRIORITY);
        }
        if (!Utils.equal(recurrence.getNotes(), changedRecurrence.getNotes())) {
            changedProperties.add(Recurrence.Property.NOTES);
        }
        if (changedProperties.isEmpty()) {
            return;
        }

        recurrence.setScheduleHours(changedRecurrence.getScheduleHours());
        recurrence.setScheduleMins(changedRecurrence.getScheduleMins());
        recurrence.setDurationHours(changedRecurrence.getDurationHours());
        recurrence.setDurationMins(changedRecurrence.getDurationMins());
        recurrence.setDescription(changedRecurrence.getDescription());
        recurrence.setSuccess(changedRecurrence.getSuccess());
        recurrence.setContext(changedRecurrence.getContext());
        recurrence.setTopic(changedRecurrence.getTopic());
        recurrence.setTime(changedRecurrence.getTime());
        recurrence.setEnergy(changedRecurrence.getEnergy());
        recurrence.setPriority(changedRecurrence.getPriority());
        recurrence.setNotes(changedRecurrence.getNotes());

        if (update) {
            if (updateAll) {
                LOG.info("Update all");
                recurrence.updateRecurrenceActions(changedProperties);
            } else {
                LOG.info("Update from update date");
                recurrence.updateRecurrenceActions(changedProperties, updateDate);
            }
        }
        LOG.info("End");
    }

    private void changeRecurrence() {
        LOG.info("Begin");

        Basis oldBasis = (Basis) wizardDescriptor.getProperty("mod.old.basis");
        if (oldBasis == null) {
            LOG.severe("Old Basis property not found.");
            return;
        }
        Basis newBasis = (Basis) wizardDescriptor.getProperty("mod.new.basis");
        if (newBasis == null) {
            LOG.severe("New Basis property not found.");
            return;
        }
        Recurrence subsequentRecurrence = (Recurrence) wizardDescriptor.getProperty("mod.recurrence.subsequent");
        if (subsequentRecurrence == null) {
            LOG.severe("Subsequent recurrence property not found.");
            return;
        }
        Recurrence regularRecurrence = (Recurrence) wizardDescriptor.getProperty("mod.recurrence.regular");
        if (regularRecurrence == null) {
            LOG.severe("Regular recurrence property not found.");
            return;
        }

        Recurrence recurrence = action.getRecurrence();

        LOG.log(Level.INFO, "Old StartDate: {0}", recurrence.getStartDate());
        LOG.log(Level.INFO, "Old Basis: {0}", oldBasis);



        boolean intervalChanged = false;
        boolean terminationChanged = false;

        if (newBasis == Basis.DONE_DATE) {
            setRecurrenceChangeProperties(recurrence, subsequentRecurrence);
        } else {
            intervalChanged = (!Utils.equal(recurrence.getStartDate(), regularRecurrence.getStartDate()) || !Utils.equal(recurrence.getFrequency(), regularRecurrence.getFrequency()) || !Utils.equal(recurrence.getPeriod(), regularRecurrence.getPeriod()));

            terminationChanged = (!Utils.equal(recurrence.getEndNbr(), regularRecurrence.getEndNbr()) || !Utils.equal(recurrence.getEndDate(), regularRecurrence.getEndDate()) || !Utils.equal(recurrence.getAdvanceNbr(), regularRecurrence.getAdvanceNbr()));

            setRecurrenceChangeProperties(recurrence, regularRecurrence);
        }

        LOG.log(Level.INFO, "New StartDate: {0}", recurrence.getStartDate());
        LOG.log(Level.INFO, "New Basis: {0}", newBasis);

        if (newBasis != oldBasis) {
            LOG.info("Basis Changed");
            if (newBasis == Basis.DONE_DATE) { // change from regular to subsequent

                LOG.info("Removing recurrence reference from occurrences before start date");
                RecurrenceRemover.remove(data, recurrence.getID(), recurrence.getStartDate());
                LOG.info("Deleting occurrences after start date");
                recurrence.removeOccurrencesAfterStartDate();
            } else { // change from subsequent to regular

                LOG.info("Removing occurrences after start date");
                recurrence.removeOccurrencesAfterStartDate();
                LOG.info("Generating occurrences from start date");
                recurrence.generateFromStartDate();
            }
        } else if (newBasis == Basis.START_DATE) {
            if (intervalChanged) {
                LOG.info("Removing occurrences after start date");
                recurrence.removeOccurrencesAfterStartDate();
                LOG.info("Generating occurrences from start date");
                recurrence.generateFromStartDate();
            } else { // interval not changed

                if (terminationChanged) {
                    LOG.info("Updating termination");
                    recurrence.updateTermination();
                }
            }
        }
        LOG.info("End");
    }

    private void setRecurrenceChangeProperties(Recurrence recurrence, Recurrence values) {
        recurrence.setBasis(values.getBasis());
        recurrence.setStartDate(values.getStartDate());
        recurrence.setEndDate(values.getEndDate());
        recurrence.setEndNbr(values.getEndNbr());
        recurrence.setFrequency(values.getFrequency());
        recurrence.setPeriod(values.getPeriod());
        recurrence.setAdvanceNbr(values.getAdvanceNbr());
    }

    private void removeRecurrence() {
        LOG.info("Removing recurrence");
        
        int recurrenceID = action.getRecurrence().getID();

        action.setRecurrence(null);
        
        RecurrenceRemover.remove(data, recurrenceID);        
    }
}
