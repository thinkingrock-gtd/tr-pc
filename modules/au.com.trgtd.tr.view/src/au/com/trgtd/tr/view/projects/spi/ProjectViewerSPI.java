package au.com.trgtd.tr.view.projects.spi;

import tr.model.project.Project;

/**
 * SPI for switching to the review projects mode and selecting a project.
 */
public interface ProjectViewerSPI {

    public void view(Project project);

}
