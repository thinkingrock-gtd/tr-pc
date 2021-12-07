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
package au.com.trgtd.tr.export.data.model;

public final class ContactBean {
    
    private final int id;
    private final String name;
    private final String email;
    private final boolean inactive;
    private final boolean team;

    public ContactBean(int id, String name, String email, boolean team, boolean inactive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.team = team;
        this.inactive = inactive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isInactive() {
        return inactive;
    }

    public boolean isTeam() {
        return team;
    }
    
}
