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
package au.com.trgtd.tr.reports.someday;

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
import au.com.trgtd.tr.extract.ExtractSomedayMaybe;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamTopic;
import au.com.trgtd.tr.extract.ParamsDialog;
import tr.extract.reports.PaperSize;
import tr.model.Data;

/**
 * Extract implementation.
 * 
 * @author Jeremy Moore
 */
public class ReportImpl extends au.com.trgtd.tr.extract.Extract {

    private ParamList paramPaper;
    private ParamTopic paramTopic;
    private ParamBoolean paramGroup;
    private ParamBoolean paramNotes;

    /** Overridden to return the report ID. */
    public String getID() {
        return "au.com.trgtd.tr.reports.someday";
    }

    /** Overridden to return the report name. */
    public String getName() {
        return getString("CTL_ReportAction");
    }

    /** Overridden to return report parameters. */
    public List<Param> getParams() {
        List<Item> formatItems = new Vector<>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        paramTopic = new ParamTopic("topic", getString("param-topic"), FormatType.XML);
        paramGroup = new ParamBoolean("group", getString("param-group"));
        paramNotes = new ParamBoolean("notes", getString("param-notes"));
        List<Param> params = new Vector<>();
        params.add(paramPaper);
        params.add(paramTopic);
        params.add(paramGroup);
        params.add(paramNotes);
        return params;
    }

    /** Overridden to process the report. */
    public void process(Data data) throws Exception {

        List<Param> extractParams = getParams();

        String title = getDialogTitleReport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), extractParams);
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }

        Map<String, Object> rparams = new HashMap<>();
        rparams.put("param-notes", Boolean.parseBoolean(paramNotes.getValue()));
        rparams.put("param-topic", paramTopic.getValue());

        File rptfile;
        if (paramGroup.getValue().equals("true")) {
            PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
            rptfile = (paper == PaperSize.Letter) ? Resources.FILE_RPT_SOMEDAY_GROUP_LTR : Resources.FILE_RPT_SOMEDAY_GROUP_A4;
        } else {
            PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
            rptfile = (paper == PaperSize.Letter) ? Resources.FILE_RPT_SOMEDAY_LTR : Resources.FILE_RPT_SOMEDAY_A4;
        }

        InputStream reportStream = new FileInputStream(rptfile);

        File xmlfile = getTmpFile("SomedayMaybe.xml");
        ExtractSomedayMaybe.process(data, xmlfile);
        
        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/somedays/someday");        
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);
        JasperViewer.viewReport(jasperPrint, false);
    }

}
