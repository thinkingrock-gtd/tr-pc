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
package au.com.trgtd.tr.sync.device;

import java.beans.PropertyChangeSupport;

/**
 * Sync state and progress properties.
 *
 * @author Jeremy Moore
 */
public final class SyncState extends PropertyChangeSupport {

    public static final String PROP_STATE = "state";
    public static final String PROP_PROGRESS = "progress";

    public static enum State {
        None, 
        Connecting,
        ConnectFailed,
        HandshakeFailed,
        Syncing,
        Cancelled,
        Failed,
        Finished,
        Aborted
    }

    private State state = State.None;

    public SyncState(Object source) {
        super(source);
    }

    public synchronized void setState(State newValue) {
        if (state == newValue) {
            return;
        }

        State oldValue = state;
        state = newValue;

        updateProgress(state);

        firePropertyChange(PROP_STATE, oldValue, newValue);
    }

    public State getState() {
        return state;
    }


    private static enum ProgressState {
        Unknown,
        Minimum,
        Percent,
        Maximum,
    }

    private ProgressState progressState;
    private int nTodo = 0;
    private int nDone = 0;

    public void setProgressValues(int nTodo, int nDone) {
        this.nDone = nDone;
        this.nTodo = nTodo;
        super.firePropertyChange(PROP_PROGRESS, false, true);
    }

    public void incDone() {
        if (nDone < nTodo) {
            nDone++;
            super.firePropertyChange(PROP_PROGRESS, false, true);
        }
    }

    public int getDone() {
        return nDone;
    }

    public int getTodo() {
        return nTodo;
    }

    private void updateProgress(State state) {
        switch (state) {
            case None:
            case Connecting:
                progressState = ProgressState.Unknown;
                break;
            case ConnectFailed:
                progressState = ProgressState.Minimum;
                break;
            case HandshakeFailed:
                progressState = ProgressState.Minimum;
                break;
            case Syncing:
                progressState = ProgressState.Percent;
                break;
            case Finished:
                progressState = ProgressState.Maximum;
                break;
            case Cancelled:
            case Failed:
            case Aborted:
            default:
                return;
        }
        super.firePropertyChange(PROP_PROGRESS, false, true);
    }

    public int getPercentDone() {
        switch (progressState) {
            case Minimum:
                return 0;
            case Maximum:
                return 100;
            case Percent: 
                return nTodo == 0 ? 0 : (nDone * 100) / nTodo;
            case Unknown:
            default:
                return -1;
        }
    }

}
