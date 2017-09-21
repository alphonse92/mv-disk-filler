/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphonse92.diskfiller;

import com.alphonse92.diskfiller.Exception.DiskFillerException;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eliecer Alejandro Molina Vergel <alejandro_mover@hotmail.com>
 */
public class bootstrap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashMap<String, String> arguments = new HashMap();
        for (String arg : args) {
            String[] param = arg.split("=");
            arguments.put(param[0], param[1]);
        }
        try {
            String root = new File(arguments.getOrDefault("rootpath", "test-folder")).getAbsolutePath();
            new DiskFiller(root)
                    .createDirectories(root + File.separator + "rutas.txt");
        } catch (DiskFillerException ex) {
            ex.printStackTrace();
        }

    }

}
