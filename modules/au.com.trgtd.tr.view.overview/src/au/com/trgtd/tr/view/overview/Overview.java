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
package au.com.trgtd.tr.view.overview;

import au.com.trgtd.tr.data.NewAction;
import java.awt.Cursor;
import javax.swing.Action;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherAdapter;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherEvent;
import org.openide.util.actions.SystemAction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.view.collect.CollectThoughtsAction;
import au.com.trgtd.tr.view.actns.RAAction;
import au.com.trgtd.tr.view.contexts.ContextsAction;
import au.com.trgtd.tr.view.criteria.screen.CriteriaAction;
import au.com.trgtd.tr.view.process.ProcessThoughtsAction;
import au.com.trgtd.tr.view.projects.ProjectsAction;
import au.com.trgtd.tr.view.reference.ReferencesAction;
import au.com.trgtd.tr.view.someday.FuturesAction;
import au.com.trgtd.tr.view.topics.TopicsAction;

/**
 * Overview screen which notifies listeners when the user makes a selection.
 *
 * @author Jeremy Moore
 */
public class Overview extends ObservableImpl {
    
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private static final Cursor NORMAL_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    
    private final JSVGCanvas canvas;
    
    /** Screen selection enumeration type. */
    public static enum Screen {
        NEW("new", (Action)SystemAction.get(NewAction.class)),
        CONTEXTS("contexts", (Action)SystemAction.get(ContextsAction.class)),
        TOPICS("topics", (Action)SystemAction.get(TopicsAction.class)),
        CRITERIA("criteria", (Action)SystemAction.get(CriteriaAction.class)),
        COLLECT("collect", (Action)SystemAction.get(CollectThoughtsAction.class)),
        PROCESS("process", (Action)SystemAction.get(ProcessThoughtsAction.class)),
        INFORMATION("info", (Action)SystemAction.get(ReferencesAction.class)),
        FUTURE("future", (Action)SystemAction.get(FuturesAction.class)),
        ACTIONS("actions", (Action)SystemAction.get(RAAction.class)),
        PROJECTS("projects", (Action)SystemAction.get(ProjectsAction.class)),
        DOASAP("doasap", (Action)SystemAction.get(RAAction.class)),
        DELEGATED("delegated", (Action)SystemAction.get(RAAction.class)),
        SCHEDULED("scheduled", (Action)SystemAction.get(RAAction.class)),
        DONE("done", (Action)SystemAction.get(RAAction.class));
        final String id;
        final Action action;
        Screen(String id, Action action) {
            this.id = id;
            this.action = action;
        }
    }
    
    /** Constructs a new instance. */
    public Overview() {
        canvas = new JSVGCanvas();
        canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        canvas.addSVGLoadEventDispatcherListener(new SVGLoadEventDispatcherAdapter() {
            @Override
            public void svgLoadEventDispatchStarted(SVGLoadEventDispatcherEvent e) {
                registerListeners(canvas.getSVGDocument());
            }
        });
    }
    
    /* SVG element id and corresponding screen array. */
    private static final Object[][] screenIDs = new Object[][] {
        {Screen.NEW.id, Screen.NEW},
        {Screen.NEW.id + "-text", Screen.NEW},
        {Screen.NEW.id + "-icon", Screen.NEW},
        {Screen.CONTEXTS.id, Screen.CONTEXTS},
        {Screen.CONTEXTS.id + "-text", Screen.CONTEXTS},
        {Screen.CONTEXTS.id + "-icon", Screen.CONTEXTS},
        {Screen.TOPICS.id, Screen.TOPICS},
        {Screen.TOPICS.id + "-text", Screen.TOPICS},
        {Screen.TOPICS.id + "-icon", Screen.TOPICS},
        {Screen.CRITERIA.id, Screen.CRITERIA},
        {Screen.CRITERIA.id + "-text", Screen.CRITERIA},
        {Screen.CRITERIA.id + "-icon", Screen.CRITERIA},
        {Screen.COLLECT.id, Screen.COLLECT},
        {Screen.COLLECT.id + "-text", Screen.COLLECT},
        {Screen.COLLECT.id + "-icon", Screen.COLLECT},
        {Screen.INFORMATION.id, Screen.INFORMATION},
        {Screen.INFORMATION.id + "-text", Screen.INFORMATION},
        {Screen.INFORMATION.id + "-icon", Screen.INFORMATION},
        {Screen.PROCESS.id, Screen.PROCESS},
        {Screen.PROCESS.id + "-text", Screen.PROCESS},
        {Screen.PROCESS.id + "-icon", Screen.PROCESS},
        {Screen.FUTURE.id, Screen.FUTURE},
        {Screen.FUTURE.id + "-text", Screen.FUTURE},
        {Screen.FUTURE.id + "-icon", Screen.FUTURE},
        {Screen.ACTIONS.id, Screen.ACTIONS},
        {Screen.ACTIONS.id + "-text", Screen.ACTIONS},
        {Screen.ACTIONS.id + "-icon", Screen.ACTIONS},
        {Screen.PROJECTS.id, Screen.PROJECTS},
        {Screen.PROJECTS.id + "-text", Screen.PROJECTS},
        {Screen.PROJECTS.id + "-icon", Screen.PROJECTS},
        {Screen.DOASAP.id, Screen.DOASAP},
        {Screen.DOASAP.id + "-text", Screen.DOASAP},
        {Screen.DOASAP.id + "-icon", Screen.DOASAP},
        {Screen.DELEGATED.id, Screen.DELEGATED},
        {Screen.DELEGATED.id + "-text", Screen.DELEGATED},
        {Screen.DELEGATED.id + "-icon", Screen.DELEGATED},
        {Screen.SCHEDULED.id, Screen.SCHEDULED},
        {Screen.SCHEDULED.id + "-text", Screen.SCHEDULED},
        {Screen.SCHEDULED.id + "-icon", Screen.SCHEDULED},
        {Screen.DONE.id, Screen.DONE},
        {Screen.DONE.id + "-text", Screen.DONE},
        {Screen.DONE.id + "-icon", Screen.DONE},
    };
    
    /* Register click listeners for SVG elements using id and screen array. */
    private void registerListeners(Document document) {
        for (int i = 0; i < screenIDs.length; i++) {
            String id = (String)screenIDs[i][0];
            Screen s = (Screen)screenIDs[i][1];
            Element e = document.getElementById(id);
            if (e instanceof EventTarget et) {
                et.addEventListener("click", new OnClickAction(s), false);
                et.addEventListener("mouseover", new OnMouseOverAction(s), false);
                et.addEventListener("mousemove", new OnMouseMoveAction(s), false);
                et.addEventListener("mouseout", new OnMouseOutAction(), false);
            }
        }
    }
    
    /* Listener for clicks. */
    private class OnClickAction implements EventListener {
        private final Screen screen;
        public OnClickAction(Screen screen) {
            super();
            this.screen = screen;
        }
        @Override
        public void handleEvent(Event evt) {
            Data data = DataLookup.instance().lookup(Data.class);
            if (screen == Screen.NEW || data != null) {
                notifyObservers(Overview.this, screen);
            }
        }
    }
    
    /* Listener for mouse over events. */
    private class OnMouseOverAction implements EventListener {
        private final Screen screen;
        public OnMouseOverAction(Screen screen) {
            super();
            this.screen = screen;
        }
        @Override
        public void handleEvent(Event evt) {
            Data data = DataLookup.instance().lookup(Data.class);
            if (screen == Screen.NEW || data != null) {
                canvas.setCursor(HAND_CURSOR);
            }
        }
    }
    
    /* Listener for mouse move (while over) events. */
    private class OnMouseMoveAction implements EventListener {
        private final Screen screen;
        public OnMouseMoveAction(Screen screen) {
            super();
            this.screen = screen;
        }
        @Override
        public void handleEvent(Event evt) {
            Data data = DataLookup.instance().lookup(Data.class);
            if (screen == Screen.NEW || data != null) {
                canvas.setCursor(HAND_CURSOR);
            }
        }
    }
    
    /* Listener for mouse out events. */
    private class OnMouseOutAction implements EventListener {
        @Override
        public void handleEvent(Event evt) {
            canvas.setCursor(NORMAL_CURSOR);
        }
    }
    
    /**
     * Gets the SVG canvas component.
     * @return the canvas.
     */
    public JSVGCanvas getSVGCanvas() {
        return canvas;
    }
    
}
