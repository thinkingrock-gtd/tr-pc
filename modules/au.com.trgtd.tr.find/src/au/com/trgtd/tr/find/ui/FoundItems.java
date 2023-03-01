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
package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoundItems implements Observable {

    private final ObservableImpl observable = new ObservableImpl();
    private final List<FoundItem> list = new ArrayList<>();
    private String text = "";

    public String getText() {
        return text;
    }

    public void reset(String text) {
        synchronized(this) {
            this.text = text;
            list.clear();
            observable.notifyObservers(this);
        }
    }

    public boolean add(FoundItem item) {
        synchronized(this) {
            if (list.add(item)) {
                observable.notifyObservers(this);
                return true;
            }
            return false;
        }
    }

    public int size() {
        synchronized(this) {
            return list.size();
        }
    }

    public List<FoundItem> getItems() {
        synchronized(this) {
            return Collections.unmodifiableList(new ArrayList<>(list));
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observable.removeObserver(observer);
    }

    @Override
    public void removeObservers() {
        observable.removeObservers();
    }

    @Override
    public void resetObservers() {
        observable.resetObservers();
    }
}
