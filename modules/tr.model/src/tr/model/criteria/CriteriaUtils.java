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
package tr.model.criteria;

import au.com.trgtd.tr.util.Utils;
import tr.model.Data;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 * Criteria utilities singleton.
 * @author Jeremy Moore
 */
public class CriteriaUtils {

    private static CriteriaUtils instance;

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static synchronized CriteriaUtils instance() {
        if (instance == null) {
            instance = new CriteriaUtils();
        }
        return instance;
    }

    // Singleton constructor.
    private CriteriaUtils() {
    }

    /**
     * Determines whether a time value is used.
     * @param data The data model.
     * @param time The time value.
     */
    public boolean isUsedTime(Data data, Value time) {
        synchronized (this) {
            if (data == null || time == null) {
                return true; // play safe
            }
            // check single actions
            if (_isUsedTime(data.getRootActions(), time)) {
                return true;
            }
            // check projects and project decendants
            if (_isUsedTime(data.getRootProjects(), time)) {
                return true;
            }
            // check future projects and future project decendants
            if (_isUsedTime(data.getRootFutures(), time)) {
                return true;
            }
            // check template projects and template project decendants
            if (_isUsedTime(data.getRootTemplates(), time)) {
                return true;
            }
            return false;
        }
    }

    /**
     * Determines whether an energy value is used.
     * @param data The data model.
     * @param energy The energy value.
     */
    public boolean isUsedEnergy(Data data, Value energy) {
        synchronized (this) {
            if (data == null || energy == null) {
                return true; // play safe
            }
            // check single actions
            if (_isUsedEnergy(data.getRootActions(), energy)) {
                return true;
            }
            // check projects and project decendants
            if (_isUsedEnergy(data.getRootProjects(), energy)) {
                return true;
            }
            // check future projects and future project decendants
            if (_isUsedEnergy(data.getRootFutures(), energy)) {
                return true;
            }
            // check template projects and template project decendants
            if (_isUsedEnergy(data.getRootTemplates(), energy)) {
                return true;
            }
            return false;
        }
    }

    /**
     * Determines whether an priority value is used.
     * @param data The data model.
     * @param priority The priority value.
     */
    public boolean isUsedPriority(Data data, Value priority) {
        synchronized (this) {
            if (data == null || priority == null) {
                return true; // play safe
            }
            // check single actions
            if (_isUsedPriority(data.getRootActions(), priority)) {
                return true;
            }
            // check projects and project decendants
            if (_isUsedPriority(data.getRootProjects(), priority)) {
                return true;
            }
            // check future projects and future project decendants
            if (_isUsedPriority(data.getRootFutures(), priority)) {
                return true;
            }
            // check template projects and template project decendants
            if (_isUsedPriority(data.getRootTemplates(), priority)) {
                return true;
            }
            return false;
        }
    }

    // Recursively check whether time value is in use in a project.
    private boolean _isUsedTime(Project project, Value time) {
        // check project children
        for (Item child : project.getChildren()) {
            if (child instanceof Action action) {
                if (Utils.equal(action.getTime(), time)) {
                    return true;
                }
            } else if (child instanceof Project prj) {
                // recursive call for child project
                if (_isUsedTime(prj, time)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Recursively check whether energy value is in use in a project. */
    private boolean _isUsedEnergy(Project project, Value energy) {
        // check project children
        for (Item child : project.getChildren()) {
            if (child instanceof Action action) {
                if (Utils.equal(action.getEnergy(), energy)) {
                    return true;
                }
            } else if (child instanceof Project prj) {
                // recursive call for child project
                if (_isUsedEnergy(prj, energy)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Recursively check whether priority value is in use in a project. */
    private boolean _isUsedPriority(Project project, Value priority) {
        // check project children
        for (Item child : project.getChildren()) {
            if (child instanceof Action action) {
                if (Utils.equal(action.getPriority(), priority)) {
                    return true;
                }
            } else if (child instanceof Project prj) {
                // recursive call for child project
                if (_isUsedPriority(prj, priority)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes a specific time value replacing it with a different time value.
     * @param oldTime The time value to replace.
     * @param newTime The time value replacement.
     */
    public boolean replaceTime(Data data, Value oldTime, Value newTime) {
        synchronized (this) {
            if (data == null || oldTime == null) {
                return false;
            }
            if (oldTime.equals(newTime)) {
                return false;
            }
            // replace in single actions
            _replaceTime(data.getRootActions(), oldTime, newTime);

            // replace in projects and project decendants
            _replaceTime(data.getRootProjects(), oldTime, newTime);

            // replace in future projects and future project decendants
            _replaceTime(data.getRootFutures(), oldTime, newTime);

            // replace in template projects and template project decendants
            _replaceTime(data.getRootTemplates(), oldTime, newTime);

            return true;
        }
    }

    // Recursively replace time in a project and decendants.
    private void _replaceTime(Project project, Value oldTime, Value newTime) {
        for (Item child : project.getChildren()) {
            if (child instanceof Action action) {
                if (Utils.equal(action.getTime(), oldTime)) {
                    action.setTime(newTime);
                }
            } else if (child instanceof Project prj) {
                _replaceTime(prj, oldTime, newTime);
            }
        }
    }
    
    /**
     * Removes a specific energy value replacing it with a different value.
     * @param oldEnergy The energy value to replace.
     * @param newEnergy The energy value replacement.
     */
    public boolean replaceEnergy(Data data, Value oldEnergy, Value newEnergy) {
        synchronized (this) {
            if (data == null || oldEnergy == null) {
                return false;
            }
            if (oldEnergy.equals(newEnergy)) {
                return false;
            }
            // replace in single actions
            _replaceEnergy(data.getRootActions(), oldEnergy, newEnergy);

            // replace in projects and project decendants
            _replaceEnergy(data.getRootProjects(), oldEnergy, newEnergy);

            // replace in future projects and future project decendants
            _replaceEnergy(data.getRootFutures(), oldEnergy, newEnergy);

            // replace in template projects and template project decendants
            _replaceEnergy(data.getRootTemplates(), oldEnergy, newEnergy);

            return true;
        }
    }

    // Recursively replace energy in a project and decendants.
    private void _replaceEnergy(Project project, Value oldEnergy, Value newEnergy) {
        for (Item child : project.getChildren()) {
            if (child instanceof Action action) {
                if (Utils.equal(action.getEnergy(), oldEnergy)) {
                    action.setEnergy(newEnergy);
                }
            } else if (child instanceof Project prj) {
                _replaceEnergy(prj, oldEnergy, newEnergy);
            }
        }
    }

    
    /**
     * Removes a specific priority value replacing it with a different value.
     * @param oldPriority The priority value to replace.
     * @param newPriority The priority value replacement.
     */
    public boolean replacePriority(Data data, Value oldPriority, Value newPriority) {
        synchronized (this) {
            if (data == null || oldPriority == null) {
                return false;
            }
            if (oldPriority.equals(newPriority)) {
                return false;
            }
            // replace in single actions
            _replacePriority(data.getRootActions(), oldPriority, newPriority);

            // replace in projects and project decendants
            _replacePriority(data.getRootProjects(), oldPriority, newPriority);

            // replace in future projects and future project decendants
            _replacePriority(data.getRootFutures(), oldPriority, newPriority);

            // replace in template projects and template project decendants
            _replacePriority(data.getRootTemplates(), oldPriority, newPriority);

            return true;
        }
    }

    // Recursively replace priority in a project and decendants.
    private void _replacePriority(Project project, Value oldPriority, Value newPriority) {
        for (Item child : project.getChildren()) {
            if (child instanceof Action action) {
                if (Utils.equal(action.getPriority(), oldPriority)) {
                    action.setPriority(newPriority);
                }
            } else if (child instanceof Project prj) {
                _replacePriority(prj, oldPriority, newPriority);
            }
        }
    }
    
}
