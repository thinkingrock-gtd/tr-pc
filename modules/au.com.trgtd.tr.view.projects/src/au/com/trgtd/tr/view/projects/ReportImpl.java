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
package au.com.trgtd.tr.view.projects;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.sf.jasperreports.engine.JasperFillManager; 
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.view.JasperViewer;
import org.w3c.dom.Document;
import au.com.trgtd.tr.extract.ExtractAction;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamsDialog;
import au.com.trgtd.tr.prefs.projects.ProjectsPrefs;
import javax.swing.JOptionPane;
import tr.extract.reports.PaperSize;
import tr.model.Data;
import tr.model.action.Action;

/**
 * Extract implementation.
 * 
 * @author Jeremy Moore
 */
public class ReportImpl extends au.com.trgtd.tr.extract.Extract {

    private static final String ID = "tr.reports.action";
    private ParamList paramPaper;

    /** Overridden to return the report ID. */
    @Override
    public String getID() {
        return ID;
    }

    /** Overridden to return the report name. */
    @Override
    public String getName() {
        return "";
    }

    /** Overridden to return report parameters. */
    @Override
    public List<Param> getParams() {
        List<Param> params = new Vector<>();
        List<Item> formatItems = new Vector<>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        params.add(paramPaper);
        return params;
    }

    /** Overriden to process the report. */
    @Override
    public void process(Data data) throws Exception {        
    }
    
    
    /** Overriden to process the report. */
    public void process(Action action) throws Exception {

        PaperSize paper;
        if (ProjectsPrefs.getPageSizeChoiceName().equals("Prompt")) {
            String title = getDialogTitleReport(getName());
            ParamsDialog dlg = new ParamsDialog(title, getID(), getParams());
            if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
                return;
            }
            paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
        } else {
            paper = ProjectsPrefs.getPageSizeChoiceName().equals("A4") ? PaperSize.A4 : PaperSize.Letter;
        }

        Map<String, Object> rparams = new HashMap<>();

        File xmlfile = getTmpFile("Action.xml");

        ExtractAction.process(action, xmlfile);

        Document document = JRXmlUtils.parse(JRLoader.getURLInputStream(xmlfile.toURI().toString()));
        rparams.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);

        File rptfile = (paper == PaperSize.Letter) ? Resources.FILE_RPT_ACTION_LETTER : Resources.FILE_RPT_ACTION_A4;

//      InputStream reportStream = new FileInputStream(Resources.FILE_RPT_ACTION);
        InputStream reportStream = new FileInputStream(rptfile);

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams);

        JasperViewer.viewReport(jasperPrint, false);
    }
}
