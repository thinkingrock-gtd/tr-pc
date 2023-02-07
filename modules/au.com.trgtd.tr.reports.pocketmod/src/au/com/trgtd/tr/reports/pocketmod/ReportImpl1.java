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
package au.com.trgtd.tr.reports.pocketmod;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;
import au.com.trgtd.tr.extract.ExtractPocketMod;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamsDialog;
import tr.model.Data;
import au.com.trgtd.tr.util.DateUtils;
import tr.extract.reports.PaperSize;

/**
 * Extract implementation.
 * 
 * @author Jeremy Moore
 */
public class ReportImpl1 extends au.com.trgtd.tr.extract.Extract {

    private static final String ID = "tr.reports.pocketmod.1";

    private enum FromDateType {
        TODAY(), TOMORROW();
        public Long getTime() {
            switch (this) {
                case TODAY:
                    return getTime(0);
                case TOMORROW:
                    return getTime(1);
                default:
                    return getTime(0);
            }
        }
        private Long getTime(int daysDifference) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, daysDifference);
            return Long.valueOf(DateUtils.getStart(calendar.getTime()).getTime());
        }
    }

    private ParamList paramPaper;
    private ParamList paramDate;
    private ParamBoolean paramCriteria;
    private ParamBoolean paramProject;    
    private ParamList paramPage1;
    private ParamList paramPage2;
    private ParamList paramPage3;
    private ParamList paramPage4;
    private ParamList paramPage5;
    private ParamList paramPage6;
    private ParamList paramPage7;
    private ParamList paramPage8;

    /** Overridden to return the report ID. */
    public String getID() {
        return ID;
    }

    /** Overridden to return the report name. */
    public String getName() {
        return getString("CTL_ReportAction1");
    }

    /** Overridden to return report parameters. */
    public List<Param> getParams() {
        List<Param> params = new Vector<>();

        List<Item> formatItems = new Vector<>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        params.add(paramPaper);

        List<Item> dateItems = new Vector<>();
        dateItems.add(new Item(getString("today"), FromDateType.TODAY.getTime().toString()));
        dateItems.add(new Item(getString("tomorrow"), FromDateType.TOMORROW.getTime().toString()));
        paramDate = new ParamList("date", getString("param-date"), dateItems);
        params.add(paramDate);
        
        paramCriteria = new ParamBoolean("paramCriteria", getString("param-criteria"));
        paramProject = new ParamBoolean("paramProject", getString("param-project"));
        params.add(paramCriteria);
        params.add(paramProject);
                
        List<Item> pageItems = new Vector<>();

        pageItems.add(new Item(getString("doasap-all-1"), "doasap-all-1"));
        pageItems.add(new Item(getString("doasap-all-2"), "doasap-all-2"));
        pageItems.add(new Item(getString("doasap-all-3"), "doasap-all-3"));
        pageItems.add(new Item(getString("doasap-all-4"), "doasap-all-4"));
        pageItems.add(new Item(getString("doasap-all-5"), "doasap-all-5"));
        pageItems.add(new Item(getString("doasap-all-6"), "doasap-all-6"));
        pageItems.add(new Item(getString("doasap-all-7"), "doasap-all-7"));
        pageItems.add(new Item(getString("doasap-all-8"), "doasap-all-8"));
 
        pageItems.add(new Item(getString("doasap-due-1"), "doasap-due-1"));
        pageItems.add(new Item(getString("doasap-due-2"), "doasap-due-2"));
        pageItems.add(new Item(getString("doasap-due-3"), "doasap-due-3"));
        pageItems.add(new Item(getString("doasap-due-4"), "doasap-due-4"));
        pageItems.add(new Item(getString("doasap-due-5"), "doasap-due-5"));
        pageItems.add(new Item(getString("doasap-due-6"), "doasap-due-6"));
        pageItems.add(new Item(getString("doasap-due-7"), "doasap-due-7"));
        pageItems.add(new Item(getString("doasap-due-8"), "doasap-due-8"));

        pageItems.add(new Item(getString("doasap-no-due-1"), "doasap-no-due-1"));
        pageItems.add(new Item(getString("doasap-no-due-2"), "doasap-no-due-2"));
        pageItems.add(new Item(getString("doasap-no-due-3"), "doasap-no-due-3"));
        pageItems.add(new Item(getString("doasap-no-due-4"), "doasap-no-due-4"));
        pageItems.add(new Item(getString("doasap-no-due-5"), "doasap-no-due-5"));
        pageItems.add(new Item(getString("doasap-no-due-6"), "doasap-no-due-6"));
        pageItems.add(new Item(getString("doasap-no-due-7"), "doasap-no-due-7"));
        pageItems.add(new Item(getString("doasap-no-due-8"), "doasap-no-due-8"));

        pageItems.add(new Item(getString("today-1"), "today-1"));
        pageItems.add(new Item(getString("today-2"), "today-2"));
        pageItems.add(new Item(getString("today-3"), "today-3"));
        pageItems.add(new Item(getString("today-4"), "today-4"));
        pageItems.add(new Item(getString("today-5"), "today-5"));
        pageItems.add(new Item(getString("today-6"), "today-6"));
        pageItems.add(new Item(getString("today-7"), "today-7"));
        pageItems.add(new Item(getString("today-8"), "today-8"));

        pageItems.add(new Item(getString("scheduled-1"), "scheduled-1"));
        pageItems.add(new Item(getString("scheduled-2"), "scheduled-2"));
        pageItems.add(new Item(getString("scheduled-3"), "scheduled-3"));
        pageItems.add(new Item(getString("scheduled-4"), "scheduled-4"));
        pageItems.add(new Item(getString("scheduled-5"), "scheduled-5"));
        pageItems.add(new Item(getString("scheduled-6"), "scheduled-6"));
        pageItems.add(new Item(getString("scheduled-7"), "scheduled-7"));
        pageItems.add(new Item(getString("scheduled-8"), "scheduled-8"));

        pageItems.add(new Item(getString("delegated-1"), "delegated-1"));
        pageItems.add(new Item(getString("delegated-2"), "delegated-2"));
        pageItems.add(new Item(getString("delegated-3"), "delegated-3"));
        pageItems.add(new Item(getString("delegated-4"), "delegated-4"));
        pageItems.add(new Item(getString("delegated-5"), "delegated-5"));
        pageItems.add(new Item(getString("delegated-6"), "delegated-6"));
        pageItems.add(new Item(getString("delegated-7"), "delegated-7"));
        pageItems.add(new Item(getString("delegated-8"), "delegated-8"));

        pageItems.add(new Item(getString("this-week-1"), "this-week-1"));
        pageItems.add(new Item(getString("this-week-2"), "this-week-2"));
        pageItems.add(new Item(getString("this-week-3"), "this-week-3"));
        pageItems.add(new Item(getString("this-week-4"), "this-week-4"));
        pageItems.add(new Item(getString("this-week-5"), "this-week-5"));
        pageItems.add(new Item(getString("this-week-6"), "this-week-6"));
        pageItems.add(new Item(getString("this-week-7"), "this-week-7"));
        pageItems.add(new Item(getString("this-week-8"), "this-week-8"));

        pageItems.add(new Item(getString("overdue-1"), "overdue-1"));
        pageItems.add(new Item(getString("overdue-2"), "overdue-2"));
        pageItems.add(new Item(getString("overdue-3"), "overdue-3"));
        pageItems.add(new Item(getString("overdue-4"), "overdue-4"));
        pageItems.add(new Item(getString("overdue-5"), "overdue-5"));
        pageItems.add(new Item(getString("overdue-6"), "overdue-6"));
        pageItems.add(new Item(getString("overdue-7"), "overdue-7"));
        pageItems.add(new Item(getString("overdue-8"), "overdue-8"));

        pageItems.add(new Item(getString("thoughts-1"), "thoughts-1"));
        pageItems.add(new Item(getString("thoughts-2"), "thoughts-2"));
        pageItems.add(new Item(getString("thoughts-3"), "thoughts-3"));
        pageItems.add(new Item(getString("thoughts-4"), "thoughts-4"));
        pageItems.add(new Item(getString("thoughts-5"), "thoughts-5"));
        pageItems.add(new Item(getString("thoughts-6"), "thoughts-6"));
        pageItems.add(new Item(getString("thoughts-7"), "thoughts-7"));
        pageItems.add(new Item(getString("thoughts-8"), "thoughts-8"));
        
        pageItems.add(new Item(getString("blank"), "blank"));
        pageItems.add(new Item(getString("new-thoughts"), "new-thoughts"));
        pageItems.add(new Item(getString("new-thoughts-spaced"), "new-thoughts-spaced"));

        paramPage1 = new ParamList("page-1", getString("param-page-1"), pageItems);
        paramPage2 = new ParamList("page-2", getString("param-page-2"), pageItems);
        paramPage3 = new ParamList("page-3", getString("param-page-3"), pageItems);
        paramPage4 = new ParamList("page-4", getString("param-page-4"), pageItems);
        paramPage5 = new ParamList("page-5", getString("param-page-5"), pageItems);
        paramPage6 = new ParamList("page-6", getString("param-page-6"), pageItems);
        paramPage7 = new ParamList("page-7", getString("param-page-7"), pageItems);
        paramPage8 = new ParamList("page-8", getString("param-page-8"), pageItems);
        
        params.add(paramPage1);
        params.add(paramPage2);
        params.add(paramPage3);
        params.add(paramPage4);
        params.add(paramPage5);
        params.add(paramPage6);
        params.add(paramPage7);
        params.add(paramPage8);

        return params;
    }

    /** Overriden to process the report. */
    public void process(Data data) throws Exception {
        String title = getDialogTitleReport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), getParams());
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }

        Date date = new Date(Long.parseLong(paramDate.getValue()));
        
        String[] pages = new String[8];
        pages[0] = paramPage1.getValue();
        pages[1] = paramPage2.getValue();
        pages[2] = paramPage3.getValue();
        pages[3] = paramPage4.getValue();
        pages[4] = paramPage5.getValue();
        pages[5] = paramPage6.getValue();
        pages[6] = paramPage7.getValue();
        pages[7] = paramPage8.getValue();

        boolean includeProject = Boolean.parseBoolean(paramProject.getValue());
        boolean includeCriteria = Boolean.parseBoolean(paramCriteria.getValue());        
        
        File xmlfile = getTmpFile("PocketMod.xml");
        ExtractPocketMod.process(data, xmlfile, pages, date, includeCriteria, includeProject);

        Map<String, Object> rparams = new HashMap<>();

        PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
        File rptfile = (paper == PaperSize.Letter) ? Resources.FILE_RPT_POCKETMOD_LTR : Resources.FILE_RPT_POCKETMOD_A4;
        InputStream reportStream = new FileInputStream(rptfile);

        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/pages");

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);

        JasperViewer.viewReport(jasperPrint, false);
    }

}
