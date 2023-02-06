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
package au.com.trgtd.tr.email.task;

import au.com.trgtd.tr.email.Email;
import org.openide.util.NbBundle;

/**
 * Thread for fetching email.
 *
 * @author Jeremy Moore
 */
public final class FetchEmailThread extends Thread {

    private static final Object SYNCHRONIZE_OBJECT = new Object();

    private static final String NAME = NbBundle.getMessage(FetchEmailThread.class, "fetch.email.task");

    /**
     * Constructs a new instance.
     */
    public FetchEmailThread() {
        super(NAME);
    }

    /**
     * Run the task.
     */
    @Override
    public void run() {
//      LOG.log(LogLevel.Info, "Starting email fetch at: {0}", new Date());
        synchronized (SYNCHRONIZE_OBJECT) {
            Email.retrieve();
        }
//      LOG.log(LogLevel.Info, "Finished email fetch at: {0}", new Date());
    }
}
