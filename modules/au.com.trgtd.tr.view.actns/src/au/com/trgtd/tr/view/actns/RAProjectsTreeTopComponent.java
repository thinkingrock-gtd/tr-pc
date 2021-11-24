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
package au.com.trgtd.tr.view.actns;

import au.com.trgtd.tr.view.projects.ProjectsTreeTopComponent;

/**
 * Top component for the review actions projects tree.
 *
 * @author Jeremy Moore
 */
public final class RAProjectsTreeTopComponent extends ProjectsTreeTopComponent {
    
    private static final String PREFERRED_ID = "RAProjectsTopComponent";
    
    private static ProjectsTreeTopComponent instance;
    
    /** Singleton constructor */
    private RAProjectsTreeTopComponent() {
        super();
    }
    
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized ProjectsTreeTopComponent getDefault() {
        if (instance == null) {
            instance = new RAProjectsTreeTopComponent();
        }
        return instance;
    }
    
    /**
     * Obtain the ProjectsTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ProjectsTreeTopComponent findInstance() {
//        TopComponent tc = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
//        if (tc == null) {
//            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find projects tree component. It will not be located properly in the window system.");
//            return getDefault();
//        }
//        if (tc instanceof RAProjectsTreeTopComponent) {
//            return (RAProjectsTreeTopComponent)tc;
//        }
//        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }
    
    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
    
    
//    final static class ResolvableHelper implements Serializable {
//        private static final long serialVersionUID = 1L;
//        public Object readResolve() {
//            return RAProjectsTreeTopComponent.getDefault();
//        }
//    }
    
}
