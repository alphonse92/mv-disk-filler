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

    private static int CREATE_ALEATORY_FILE_PATHS = 0;
    private static int USE_EXIST_FILE_PATHS = 1;

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
            DiskFiller df = new DiskFiller(root.getAbsolutePath())
                    .debug(true, DiskFiller.VERBOSE_ALL);
            //validamos si existe el directorio raíz, si no existe lo crea si ocurre algo dice que no se pudo
            if (root.isDirectory() || (!root.isDirectory() && root.mkdir())) {
                //validamos el método, el método por default es el 0
                int method = Integer.parseInt(arguments.getOrDefault("method", "0"));
                if (method == bootstrap.USE_EXIST_FILE_PATHS) {
                    if (!pathsFile.isFile()) {
                        throw new DiskFillerException("No se pudo encontrar el archivo de rutas especificado: " + pathsFile.getAbsolutePath());
                    }

                } else if (method == bootstrap.CREATE_ALEATORY_FILE_PATHS) {
                    df = df.createDirectories(Integer.parseInt(arguments.getOrDefault("ndirs", "100")),
                            Integer.parseInt(arguments.getOrDefault("mindepth", "1")),
                            Integer.parseInt(arguments.getOrDefault("maxdepth", "5")),
                            Integer.parseInt(arguments.getOrDefault("maxnsubdirectories", "5")));
                }

                df.createDirectories(pathsFile.getAbsolutePath());

            } else {
                throw new DiskFillerException("No se pudo encontrar el directorio raíz: " + root.getAbsolutePath());
            }

        } catch (DiskFillerException ex) {
            ex.printStackTrace();
        }

    }

}
