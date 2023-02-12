/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.export.data;

import au.com.trgtd.tr.export.data.model.ActionBean;
import au.com.trgtd.tr.export.data.model.DataModel;
import au.com.trgtd.tr.export.data.model.ContactBean;
import au.com.trgtd.tr.export.data.model.ContextBean;
import au.com.trgtd.tr.export.data.model.EnergyBean;
import au.com.trgtd.tr.export.data.model.ItemBean;
import au.com.trgtd.tr.export.data.model.PriorityBean;
import au.com.trgtd.tr.export.data.model.ProjectBean;
import au.com.trgtd.tr.export.data.model.ReferenceBean;
import au.com.trgtd.tr.export.data.model.SomedayBean;
import au.com.trgtd.tr.export.data.model.ThoughtBean;
import au.com.trgtd.tr.export.data.model.TopicBean;
import au.com.trgtd.tr.extract.prefs.ExtractPrefs;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Export data as CSV files.
 */
public class ExportCSVFiles {

    private final static Logger LOGGER = Logger.getLogger("tr.export.ExportCSVFiles");
    private final static SimpleDateFormat TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Charset CHARSET = Charset.forName("UTF-8");
    private final static char BOM = '\ufeff';    
    private final static String DATE_FORMAT = "yyyy/MM/dd";
    private final static String CONTACTS = "contacts";
    private final static String CONTEXTS = "contexts";
    private final static String ENERGIES = "energies";
    private final static String PRIORITIES = "priorities";
    private final static String REFERENCES = "references";
    private final static String SOMEDAYS = "somedays";
    private final static String THOUGHTS = "thoughts";
    private final static String TOPICS = "topics";
    private final static String ACTIONS = "actions";
    private final static String PROJECTS = "projects";

    public ExportCSVFiles() {
    }

    public void export() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOGGER.log(Level.SEVERE, "ThingkingRock data not found.");
            return;
        }

        DataModel dm = new DataModel(data);

        File dir = dir();
        if (!dir.isDirectory()) {
            LOGGER.log(Level.SEVERE, "Invalid directory: {0}", dir.getPath());
            return;
        }

        LOGGER.log(Level.INFO, "Exporting CSV files to {0}", dir.getPath());

        String ts = TIMESTAMP.format(Calendar.getInstance().getTime());

        exportContacts(file(dir, CONTACTS, ts), dm.contacts);
        exportContexts(file(dir, CONTEXTS, ts), dm.contexts);
        exportEnergies(file(dir, ENERGIES, ts), dm.energies);
        exportPriorities(file(dir, PRIORITIES, ts), dm.priorities);
        exportReferences(file(dir, REFERENCES, ts), dm.references);
        exportSomedays(file(dir, SOMEDAYS, ts), dm.somedays);
        exportThoughts(file(dir, THOUGHTS, ts), dm.thoughts);
        exportTopics(file(dir, TOPICS, ts), dm.topics);
        exportActions(file(dir, ACTIONS, ts), dm.items);
        exportProjects(file(dir, PROJECTS, ts), dm.items);

        LOGGER.log(Level.INFO, "Completed exporting CSV files");
    }

    private File dir() {
        File extDir = new File(ExtractPrefs.getPath());
        if (!extDir.isDirectory()) {
            return extDir;
        }
        File csvDir = new File(extDir, "tr_export_csv");
        if (!csvDir.isDirectory() && !csvDir.mkdir()) {
            LOGGER.log(Level.SEVERE, "Failed to make directory: {0}", csvDir.getPath());
        }
        return csvDir;
    }

    private File file(File dir, String name, String ts) {
        return new File(dir, ts + "-" + name + ".csv");
    }

    private void exportContacts(File csvFile, List<ContactBean> contacts) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = {
                "id",
                "name",
                "email",
                "team",
                "inactive"
            };
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // ID (must be unique)
                new NotNull(), // name
                new Optional(), // email
                new NotNull(new FmtBool("Y", "N")), // team
                new NotNull(new FmtBool("Y", "N")) // inactive
            };
            beanWriter.writeHeader(header);
            for (ContactBean contact : contacts) {
                beanWriter.write(contact, header, processors);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export contacts.", ex);
        }
    }

    private void exportContexts(File csvFile, List<ContextBean> contexts) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = {
                "id",
                "title",
                "descr"
            };
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // id
                new NotNull(), // title
                new Optional() // descr
            };
            beanWriter.writeHeader(header);
            for (ContextBean context : contexts) {
                beanWriter.write(context, header, processors);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export contexts.", ex);
        }
    }

    private void exportEnergies(File csvFile, List<EnergyBean> energies) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = new String[]{"id", "title", "order"};
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // id
                new NotNull(), // title
                new NotNull() // order
            };
            beanWriter.writeHeader(header);
            for (EnergyBean energy : energies) {
                beanWriter.write(energy, header, processors);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export energy values.", ex);
        }
    }

    private void exportPriorities(File csvFile, List<PriorityBean> priorities) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = {
                "id",
                "title",
                "order"
            };
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // id
                new NotNull(), // title
                new NotNull() // order
            };
            beanWriter.writeHeader(header);
            for (PriorityBean priority : priorities) {
                beanWriter.write(priority, header, processors);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export priority values.", ex);
        }
    }

    private void exportReferences(File csvFile, List<ReferenceBean> references) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = {
                "id",
                "title",
                "notes",
                "topicID",
                "created"
            };
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // id
                new NotNull(), // title
                new Optional(), // notes
                new Optional(), // topicID
                new FmtDate(DATE_FORMAT), // created date
            };
            beanWriter.writeHeader(header);
            for (ReferenceBean reference : references) {
                beanWriter.write(reference, header, processors);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export reference values.", ex);
        }
    }

    private void exportSomedays(File csvFile, List<SomedayBean> list) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = {
                "id",
                "title",
                "notes",
                "thoughtID",
                "topicID",
                "created",
                "tickle"
            };
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // id
                new NotNull(), // title
                new Optional(), // notes
                new Optional(), // thoughtID
                new Optional(), // topicID
                new FmtDate(DATE_FORMAT), // created date
                new Optional(new FmtDate(DATE_FORMAT)) // tickle date
            };
            beanWriter.writeHeader(header);
            for (SomedayBean item : list) {
                beanWriter.write(item, header, processors);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export someday values.", ex);
        }
    }

    private void exportThoughts(File csvFile, List<ThoughtBean> list) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = {
                "id",
                "title",
                "notes",
                "topicID",
                "actionID",
                "created",
                "processed",
                "delegationType",
                "delegationActionID",
                "delegationDone",
                "delegationReply"
            };
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // id
                new NotNull(), // title
                new Optional(), // notes
                new Optional(), // topicID
                new Optional(), // actionID
                new FmtDate(DATE_FORMAT), // created
                new NotNull(new FmtBool("Y", "N")), // processed                 
                new Optional(), // delegationType
                new Optional(), // delegationActionID
                new Optional(new FmtDate(DATE_FORMAT)), // delegationDone
                new Optional() // delegationReply
            };
            beanWriter.writeHeader(header);
            for (ThoughtBean item : list) {
                beanWriter.write(item, header, processors);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export thoughts.", ex);
        }
    }

    private void exportTopics(File csvFile, List<TopicBean> list) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = {
                "id",
                "title",
                "descr",
                "bgRGB",
                "fgRGB"
            };
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // id
                new NotNull(), // title
                new Optional(), // descr
                new Optional(), // bgRGB
                new Optional() // bgRGB
            };
            beanWriter.writeHeader(header);
            for (TopicBean item : list) {
                beanWriter.write(item, header, processors);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export topics.", ex);
        }
    }

    private void exportActions(File csvFile, List<ItemBean> list) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = {
                "id",
                "title",
                "notes",
                "success",
                "order",
                "projectID",
                "thoughtID",
                "topicID",
                "contextID",
                "energyID",
                "priorityID",
                "time",
                "created",
                "startDate",
                "dueDate",
                "doneDate",
                "state",
                "delegateID",
                "delegateName",
                "delegateFollow"
            };
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // id
                new NotNull(), // title
                new Optional(), // notes
                new Optional(), // success
                new NotNull(), // order
                new Optional(), // projectID
                new Optional(), // thoughtID
                new Optional(), // topicID
                new Optional(), // contextID
                new Optional(), // energyID
                new Optional(), // priorityID
                new Optional(), // time
                new FmtDate(DATE_FORMAT), // created
                new Optional(new FmtDate(DATE_FORMAT)), // startDate
                new Optional(new FmtDate(DATE_FORMAT)), // dueDate
                new Optional(new FmtDate(DATE_FORMAT)), // doneDate
                new NotNull(), // state
                new Optional(), // delegateID
                new Optional(), // delegateName
                new Optional(new FmtDate(DATE_FORMAT)) // delegateFollow
            };
            beanWriter.writeHeader(header);
            for (ItemBean item : list) {
                if (item instanceof ActionBean bean) {
                    beanWriter.write(bean, header, processors);
                }
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export actions.", ex);
        }
    }

    private void exportProjects(File csvFile, List<ItemBean> list) {
        try (ICsvBeanWriter beanWriter = csvBeanWriter(csvFile)) {
            final String[] header = {
                "id",
                "title",
                "notes",
                "purpose",
                "vision",
                "brainstorm",
                "organising",
                "order",
                "projectID",
                "thoughtID",
                "topicID",
                "priorityID",
                "created",
                "startDate",
                "dueDate",
                "doneDate"
            };
            final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // id
                new NotNull(), // title
                new Optional(), // notes
                new Optional(), // purpose
                new Optional(), // vision
                new Optional(), // brainstorm
                new Optional(), // organising                
                new NotNull(), // order                
                new Optional(), // projectID
                new Optional(), // thoughtID
                new Optional(), // topicID
                new Optional(), // priorityID
                new FmtDate(DATE_FORMAT), // created
                new Optional(new FmtDate(DATE_FORMAT)), // startDate
                new Optional(new FmtDate(DATE_FORMAT)), // dueDate
                new Optional(new FmtDate(DATE_FORMAT)) // doneDate
            };
            beanWriter.writeHeader(header);
            for (ItemBean item : list) {
                if (item instanceof ProjectBean bean) {
                    beanWriter.write(bean, header, processors);
                }
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to export projects.", ex);
        }
    }

    private ICsvBeanWriter csvBeanWriter(File file) throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), CHARSET);
        writer.write(BOM); // for crappy MS Windows Excel
        return new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
    }

}
