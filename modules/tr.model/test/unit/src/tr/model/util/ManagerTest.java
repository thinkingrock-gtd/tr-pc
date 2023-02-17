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
package tr.model.util;

import static junit.framework.TestCase.assertEquals;
import org.junit.Before;
import org.junit.Test;
import tr.model.topic.Topic;

public class ManagerTest {

    private Manager sut;

    @Before
    public void setUp() {
        sut = new Manager<>();
    }

    @Test
    public void givenNewManager_sizeIsZero() {
        assertEquals(0, sut.size());        
    }
    
    @Test
    public void givenNewManager_sizeIsZero_itIsEmpty() {
        assertEquals(true, sut.isEmpty());
    }

    @Test
    public void givenNewManagerWithAnItem_sizeIsOne() {
        sut.add(Topic.getDefault());
        assertEquals(1, sut.size());        
    }
    
    @Test
    public void givenNewManagerWithAnItem_itIsNotEmpty() {
        sut.add(Topic.getDefault());        
        assertEquals(false, sut.isEmpty());
    }

    @Test
    public void givenNewManagerWithItems_afterClearing_sizeIsZero() {
        addAndRemoveTopics();        
        assertEquals(0, sut.size());        
    }
    
    @Test
    public void givenNewManagerWithItems_afterClearing_itIsEmpty() {
        addAndRemoveTopics();
        assertEquals(true, sut.isEmpty());
    }
    
    private void addAndRemoveTopics() {
        sut.add(Topic.getDefault());
        sut.add(Topic.getDefault());
        sut.removeAll();
    }

}

