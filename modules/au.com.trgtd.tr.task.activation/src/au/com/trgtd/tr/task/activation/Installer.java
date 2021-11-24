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
package au.com.trgtd.tr.task.activation;

import org.openide.modules.ModuleInstall;

/**
 * Module installer to start the activator task scheduler.
 */
public class Installer extends ModuleInstall {
    
    @Override
    public void restored() {
        ActivatorTaskScheduler.instance().runTask(0, 1, 0);
        ActivatorTaskScheduler.instance().startScheduler();
    }

}
