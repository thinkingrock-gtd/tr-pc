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
package au.com.trgtd.tr.imports.thoughts;

import au.com.trgtd.tr.data.FileFilterImpl;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import au.com.trgtd.tr.imports.thoughts.prefs.ImportThoughtsPrefs;
import tr.model.Data;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import tr.model.util.Manager;

/**
 * Import thoughts from a text file with a format of one thought per line. If a
 * line has a tab then the try to match the text following the last tab with a
 * topic name and set the thought topic. Set the thought description as the text
 * up to the last tab. If a line has no tab or no topic name match then put the
 * entire line as the thought description and set the topic as none.
 *
 * @author Jeremy Moore
 */
public class ImportThoughts {
    
    private static Manager<Thought> thoughts;
    private static Manager<Topic> topics;
    
    /**
     * Start the import thoughts action.
     * @param data The data instance to import into.
     * @return false if the import is not done.
     */
    public static final boolean doImport(Data data) throws Exception {
        
        assert(data != null);
        
        thoughts = data.getThoughtManager();
        topics = data.getTopicManager();
        
        // Get user to choose the import file
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(org.openide.util.NbBundle.getMessage(ImportThoughts.class, "dialog.title"));
        String[] extns = { "txt" };
        FileFilter filter = new FileFilterImpl(org.openide.util.NbBundle.getMessage(ImportThoughts.class, "Text_Files"), extns, true);
        chooser.setFileFilter(filter);
        File def = new File(ImportThoughtsPrefs.getDefaultFilePath());
        chooser.ensureFileIsVisible(def);
        chooser.setSelectedFile(def);
        Component p = WindowManager.getDefault().getMainWindow();
        int option = chooser.showOpenDialog(p);
        if (option != JFileChooser.APPROVE_OPTION) return false;
        
        // make sure the file can be read
        String path = chooser.getSelectedFile().getPath();
        File file = new File(path);
        if (file.exists() && file.isFile() && file.canRead()) {
            System.out.println("Importing thoughts from " + path);
        } else {
            throw new Exception("The file could not be opened and read.");
        }
        
        // warning and confirmation for large files
        if (file.length() > ImportThoughtsPrefs.getWarningFileSize()) {
            String t = NbBundle.getMessage(ImportThoughts.class, "confirm.title");
//            String m = "The file you are importing is large and will result in approximately " + file.length() / 80 + " new thoughts.    \n "
//                    + "Are you sure that you want to continue?";
                        
            String m = NbBundle.getMessage(ImportThoughts.class, "confirm.message", file.length() / 80);
            
            Object[] options = {NbBundle.getMessage(ImportThoughts.class, "Yes"), NbBundle.getMessage(ImportThoughts.class, "No")};
            int n = JOptionPane.showOptionDialog(p, m, t, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n != JOptionPane.YES_OPTION) return false;
        }
        
        // process each line as a new thought
        int count = 0;
        BufferedReader input = null;
        try {
            input = getReader(file);
            
            String line = null;
            while ((line = input.readLine()) != null) {
                if ( ! line.trim().equals("") ) {
//                  System.out.println(NbBundle.getMessage(ImportThoughts.class, "Importing_line") + ": " + line);
                    Thought thought = new Thought(data.getNextID());
                    thought.setDescription(line);
                    int i = line.lastIndexOf('\t');
                    if (i > -1) {
                        String topicName = line.substring(i + 1);
                        Topic topic = getTopic(topicName);
                        if (topic != null) {
                            thought.setDescription(line.substring(0, i));
                            thought.setTopic(getTopic(topicName));
                        }
                    }
                    thoughts.add(thought);
                    count++;
                }
            }
            
            // save file path for default next time
            ImportThoughtsPrefs.setDefaultFilePath(file.getPath());
            
            notifySuccess(count);
        } catch (Exception ex) {
            notifyFailure(ex);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
            }
        }
        return true;
    }
    
    /* Gets the topic for a topic name or null if not found. */
    private static Topic getTopic(String name) {
        for (Topic topic : topics.list()) {
            if (topic.getName().equalsIgnoreCase(name)) {
                return topic;
            }
        }
        return null;
    }
    
    private static BufferedReader getReader(File file) throws Exception {
        String encoding = ImportThoughtsPrefs.getEncoding().trim();
        if (encoding == null || encoding.length() == 0 || !Charset.isSupported(encoding)) {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } else {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        }
        
    }
    
    private static void notifySuccess(final int count) {
        SwingUtilities.invokeLater(() -> {
            String t = NbBundle.getMessage(ImportThoughts.class, "Import_Thoughts");
            String m = NbBundle.getMessage(ImportThoughts.class, "imported_n_thoughts", count) + "       "; // No I18N
            Component p = WindowManager.getDefault().getMainWindow();
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private static void notifyFailure(final Exception ex) {
        SwingUtilities.invokeLater(() -> {
            String t = NbBundle.getMessage(ImportThoughts.class, "Import_Thoughts");
            String m = NbBundle.getMessage(ImportThoughts.class, "error_processing_file") + " \n" + ex.getMessage();
            Component p = WindowManager.getDefault().getMainWindow();
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);
        });
    }
    
}
