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
package au.com.trgtd.tr.runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Thread that consumes an input stream.
 */
public class StreamConsumer extends Thread {
    
    private final InputStream is;
    private boolean terminate;
    
    /**
     * Constructs a new instance for the given input stream.
     * @param is The input stream
     */
    public StreamConsumer(InputStream is) {
        this.is = is;
    }
    
    public void terminate() {
        terminate = true;
    }
    
    /**
     * Thread run method.
     */
    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null && ! terminate) {
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
