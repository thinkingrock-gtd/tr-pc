/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.trgtd.tr.i18n;

import java.util.Properties;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        System.out.println("******");
        Properties ps = System.getProperties();
        ps.list(System.out);
        System.out.println("******");
        
        System.setProperty("user.language", "fr");
    }

}
