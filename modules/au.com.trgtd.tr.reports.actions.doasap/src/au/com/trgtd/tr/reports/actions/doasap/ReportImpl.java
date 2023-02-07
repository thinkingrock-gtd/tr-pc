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
package au.com.trgtd.tr.reports.actions.doasap;

import au.com.trgtd.tr.extract.ExtractActions;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamContext;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamsDialog;
import java.util.logging.Level;
import java.util.logging.Logger;
import tr.extract.reports.PaperSize;
import tr.model.Data;

/**
 * Extract implementation.
 * 
 * @author Jeremy Moore
 */
public class ReportImpl extends au.com.trgtd.tr.extract.Extract {

    private static final Logger LOG = Logger.getLogger("au.com.trgtd.tr.reports");
    private ParamList paramPaper;
    private ParamList paramGroup;    
    private ParamContext paramContext;
    private ParamBoolean paramSuccess;
    private ParamBoolean paramCriteria;
    private ParamBoolean paramProject;
    private ParamBoolean paramNotes;

    /** Overridden to return the report ID. */
    @Override
    public String getID() {
        return "tr.reports.actions.doasap";
    }

    /** Overridden to return the report name. */
    @Override
    public String getName() {
        return getString("CTL_ReportAction");
    }

    /** Overridden to return report parameters. */
    @Override
    public List<Param> getParams() {
        List<Item> formatItems = new Vector<>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        List<Item> groupItems = new Vector<>();
        groupItems.add(new Item(getString("param-group-context-only"), "context-only"));
        groupItems.add(new Item(getString("param-group-context-time"), "context-time"));
        groupItems.add(new Item(getString("param-group-context-energy"),"context-energy"));
        groupItems.add(new Item(getString("param-group-context-priority"),"context-priority"));        
        paramGroup = new ParamList("group", getString("param-group"), groupItems);
        paramContext = new ParamContext("paramContext", getString("param-context"), FormatType.XML);
        paramCriteria = new ParamBoolean("paramCriteria", getString("param-criteria"));
        paramSuccess = new ParamBoolean("paramSuccess", getString("param-success"));
        paramProject = new ParamBoolean("paramProject", getString("param-project"));
        paramNotes = new ParamBoolean("paramNotes", getString("param-notes"));
        List<Param> params = new Vector<>();
        params.add(paramPaper);
        params.add(paramGroup);
        params.add(paramContext);
        params.add(paramCriteria);
        params.add(paramSuccess);
        params.add(paramProject);
        params.add(paramNotes);
        return params;
    }

    /** Overridden to process the report. */
    @Override
    public void process(Data data) throws Exception {
        String title = getDialogTitleReport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), getParams());
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }
        Map<String, Object> rparams = new HashMap<>();
        rparams.put("paramContext", paramContext.getValue());
        rparams.put("paramCriteria", Boolean.parseBoolean(paramCriteria.getValue()));
        rparams.put("paramSuccess", Boolean.parseBoolean(paramSuccess.getValue()));
        rparams.put("paramProject", Boolean.parseBoolean(paramProject.getValue()));
        rparams.put("paramNotes", Boolean.parseBoolean(paramNotes.getValue()));

        PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
        String groupBy = paramGroup.getValue();
        
        InputStream reportStream;
        if (groupBy.equals("context-time")) {
            reportStream = new FileInputStream(Resources.getFileDoASAPByContextTime(paper));
        } else if (groupBy.equals("context-energy")) {
            reportStream = new FileInputStream(Resources.getFileDoASAPByContextEnergy(paper));
        } else if (groupBy.equals("context-priority")) {
            reportStream = new FileInputStream(Resources.getFileDoASAPByContextPriority(paper));
        } else {
            reportStream = new FileInputStream(Resources.getFileDoASAPByContextOnly(paper));
        }
        
        File xmlfile = getTmpFile("Actions.xml");

        LOG.log(Level.INFO, "Extract file: {0}", xmlfile.getPath());
        
        ExtractActions.process(data, xmlfile);
                            
        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/data/actions/action");        
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);

        JasperViewer.viewReport(jasperPrint, false);
    }
    
}
