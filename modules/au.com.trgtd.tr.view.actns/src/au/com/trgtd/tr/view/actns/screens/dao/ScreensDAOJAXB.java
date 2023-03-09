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
package au.com.trgtd.tr.view.actns.screens.dao;

import au.com.thinkingrock.xsd.tr.view.actions.screens.*;
import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.actns.screens.ActionsScreen;
import au.com.trgtd.tr.view.actns.screens.ActionsScreens;
import au.com.trgtd.tr.view.actns.screens.columns.ActionsColumn;
import au.com.trgtd.tr.view.actns.screens.filters.ActionsFilter;
import au.com.trgtd.tr.view.actns.screens.filters.FilterFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Actions screens DAO using JAXB.
 *
 * @author Jeremy Moore
 */
final class ScreensDAOJAXB implements ScreensDAO {

    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    private static final String FILENAME = "ReviewActions.xml";
    private static final String ENCODING = "UTF-8";
    private static ScreensDAO instance;
    private ActionsScreens actionsScreens = null;

    public static ScreensDAO getInstance() {
        if (instance == null) {
            instance = new ScreensDAOJAXB();
        }
        return instance;
    }

    /* Singleton private constructor. */
    private ScreensDAOJAXB() {
    }

    public ActionsScreens getData() {
        LOG.info("ScreensDAOJAXB.getData()");        
        
        if (actionsScreens == null) {
            if (hasPersistantData()) {
                try {
                    restore();
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Failed to restore review actions screens. {0}", ex.getMessage());
                }
            }
            if (actionsScreens == null) {
                actionsScreens = new ActionsScreens();
            }
        }
        
        return actionsScreens;
    }    

    public boolean hasPersistantData() {
        File file = getFile();
        return file != null && file.exists();
    }

    private File getFile() {
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds != null) {
            try {
                File parent = (new File(ds.getPath())).getParentFile();
                return new File(parent, FILENAME);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Failed to get file. {0}", ex.getMessage());
            }
        }
        return null;
    }

    public void persist() throws Exception {
        LOG.info("ScreensDAOJAXB.persist()");
        
        if (actionsScreens == null) {
            // do not persist if screens have not been restored.
            return;
        }
        File file = getFile();
        if (file == null) {
            LOG.severe("File is null.");
            return;
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        if (!file.canWrite()) {
            LOG.log(Level.SEVERE, "Can not write to file: {0}", file);
            return;
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), ENCODING));
        JAXBContext jaxbContext = JAXBContext.newInstance(Screens.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(toXMLScreens(actionsScreens), writer);
        writer.close();
    }

    public void restore() throws Exception {
        File file = getFile();
        if (file == null || !file.exists()) {
            LOG.log(Level.SEVERE, "File does not exist: {0}", file);
            return;
        }
        
        LOG.log(Level.INFO, "ScreensDAOJAXB.restore() - Reading file: {0}", file);        
        
        Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
        JAXBContext jaxbContext = JAXBContext.newInstance(Screens.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Screens screens = (Screens) unmarshaller.unmarshal(reader);
        reader.close();

        actionsScreens = toActionsScreens(screens);
    }

    private Screens toXMLScreens(ActionsScreens actionScreens) {
        if (actionsScreens == null) {
            return new Screens();
        }
        Screens screens = new Screens();

        for (ActionsScreen as : actionScreens.getScreens().list()) {

            String name = StringEscapeUtils.escapeXml10(as.getKey());

            Screen screen = new Screen(name);

            // columns
            for (byte columnIndex : as.getColumnOrder()) {
                ActionsColumn ac = as.getColumns().get(columnIndex);
                if (ac.isVisible()) {
                    Column c = new Column();
                    c.setColumnID(ColumnID.fromValue(ac.getColumnID()));
                    c.setWidth(ac.getWidth());
                    screen.getColumns().getColumns().add(c);
                }
            }

            // sort columns
            byte[] sortColumns = as.getSortColumns();
            byte[] sortStates = as.getSortStates();
            for (int i = 0; i < sortColumns.length; i++) {
                SortColumn sc = new SortColumn();
                sc.setColumnID(ColumnID.fromValue(ActionsColumn.getColumnID(sortColumns[i])));
                sc.setDescending(sortStates[i] == 2);
                screen.getSortColumns().getSortColumns().add(sc);
            }
            // filters
            screen.getFilters().setShow(as.isShowFilters());

            for (ActionsFilter af : as.getFilters()) {
                if (af.isUsed()) {
                    Filter f = new Filter();
                    f.setFilterID(FilterID.fromValue(FilterFactory.getID(af.getIndex())));
                    f.setVisible(af.isShown());
                    f.setExcludeNulls(af.isExcludeNulls());
                    if (af.getSerialValues() != null) {
                        f.getValues().addAll(Arrays.asList(af.getSerialValues()));
                    }
                    screen.getFilters().getFilters().add(f);
                }
            }
            screens.getScreens().add(screen);
        }

        return screens;
    }

    private ActionsScreens toActionsScreens(Screens screens) {

        ActionsScreens scrns = new ActionsScreens();

        for (Screen screen : screens.getScreens()) {

            String name = StringEscapeUtils.unescapeXml(screen.getName());

            ActionsScreen actionsScreen = ActionsScreen.create(name);

            // columns
            List<Byte> columnOrderList = new ArrayList<>();
            Columns columns = screen.getColumns();
            if (columns != null) {
                for (Column column : columns.getColumns()) {
                    byte index = column.getColumnID().index();
                    columnOrderList.add(index);
                    ActionsColumn actionsColumn = actionsScreen.getColumns().get(index);
                    actionsColumn.setVisible(true);
                    actionsColumn.setWidth(column.getWidth());
                }
            }
            actionsScreen.setColumnOrder(Utils.byteArray(columnOrderList));

            // filters
            Filters filters = screen.getFilters();
            if (filters != null) {
                actionsScreen.setShowFilters(filters.isShow());
                for (Filter filter : filters.getFilters()) {
                    byte index = filter.getFilterID().index();
                    ActionsFilter actionsFilter = actionsScreen.getFilters().get(index);
                    actionsFilter.setUsed(true);
                    actionsFilter.setShown(filter.isVisible());
                    actionsFilter.setExcludeNulls(filter.isExcludeNulls());
                    actionsFilter.setSerialValues(filter.getValues().toArray(new String[0]));
                }
            }

            // sort columns
            List<Byte> sortColumnList = new ArrayList<>();
            List<Byte> sortStatusList = new ArrayList<>();
            SortColumns sortColumns = screen.getSortColumns();
            if (sortColumns != null) {
                for (SortColumn sortColumn : screen.getSortColumns().getSortColumns()) {
                    sortColumnList.add(sortColumn.getColumnID().index());
                    sortStatusList.add(sortColumn.isDescending() ? (byte) 2 : (byte) 1);
                }
            }
            actionsScreen.setSortColumns(Utils.byteArray(sortColumnList));
            actionsScreen.setSortStatus(Utils.byteArray(sortStatusList));

            scrns.getScreens().add(actionsScreen);
        }

        return scrns;
    }

    public void delete() {
        File file = getFile();
        if (file != null) {
            file.delete();
        }
    }

    public void reset() {
        LOG.info("ScreensDAOJAXB.reset()");
        
        actionsScreens = null;
    }
}
