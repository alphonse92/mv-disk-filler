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
            File root = new File(arguments.getOrDefault("rootpath", "diskfiller"));
            File pathsFile = new File(arguments.getOrDefault("pathfile", "rutas"));

            if (!pathsFile.isFile()) {
                throw new DiskFillerException("No se pudo encontrar el archivo de rutas especificado: " + pathsFile.getAbsolutePath());
            }

            if (root.isDirectory()
                    || (!root.isDirectory() && root.mkdir())) {

                new DiskFiller(root.getAbsolutePath())
                        .debug(true, DiskFiller.VERBOSE_ALL)
                        .createDirectories(Integer.parseInt(arguments.getOrDefault("ndirs", "500")),
                                Integer.parseInt(arguments.getOrDefault("mindepth", "3")),
                                Integer.parseInt(arguments.getOrDefault("mindepth", "10")));
            } else {
                throw new DiskFillerException("No se pudo encontrar el directorio raíz: " + root.getAbsolutePath());
            }

        } catch (DiskFillerException ex) {
            ex.printStackTrace();
        }

    }

}
