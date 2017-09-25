/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphonse92.diskfiller;

import com.alphonse92.diskfiller.Exception.DiskFillerException;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Eliecer Alejandro Molina Vergel <alejandro_mover@hotmail.com>
 */
public class bootstrap {

    private static final int CREATE_ALEATORY_FILE_PATHS = 0;
    private static final int USE_EXIST_FILE_PATHS = 1;
    private static final String PARAM_ROOT_PATH = "rootpath";
    private static final String PARAM_ROOT_PATH_DEFAULT = "diskfiller";
    private static final String PARAM_PATH_FILE = "pathfile";
    private static final String PARAM_PATH_FILE_DEFAULT = "rutas";
    private static final String PARAM_METHOD = "method";
    private static final String PARAM_METHOD_DEFAULT = "0";
    private static final String PARAM_CREATE_DIR_NDIRS = "ndirs";
    private static final String PARAM_CREATE_DIR_NDIRS_DEFAULT = "100";
    private static final String PARAM_CREATE_DIR_MINDEPTH = "mindepth";
    private static final String PARAM_CREATE_DIR_MINDEPTH_DEFAULT = "4";
    private static final String PARAM_CREATE_DIR_MAXDEPTH = "maxdepth";
    private static final String PARAM_CREATE_DIR_MAXDEPTH_DEFAULT = "5";
    private static final String PARAM_CREATE_DIR_MAXNSUBDIRECTORIES = "maxnsubdirectories";
    private static final String PARAM_CREATE_DIR_MAXNSUBDIRECTORIES_DEFAULT = "5";
    private static final String PARAM_CREATE_DIR_CREATE = "createdirectory";
    private static final String PARAM_CREATE_DIR_CREATE_DEFAULT = "true";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashMap<String, String> arguments = new HashMap();
        for (String arg : args) {
            String[] param = arg.split("=");
            String val = "true";
            if (param.length > 1) {
                val = param[1];
            }
            arguments.put(param[0], val);
        }
        try {
            File root = new File(arguments.getOrDefault(bootstrap.PARAM_ROOT_PATH, bootstrap.PARAM_ROOT_PATH_DEFAULT));

            DiskFiller df = new DiskFiller(root.getAbsolutePath())
                    .debug(true, DiskFiller.VERBOSE_ALL);
            //validamos si existe el directorio raíz, si no existe lo crea si ocurre algo dice que no se pudo
            if (root.isDirectory() || (!root.isDirectory() && root.mkdir())) {
                createDirectories(arguments, df);

            } else {
                throw new DiskFillerException("No se pudo encontrar el directorio raíz: " + root.getAbsolutePath());
            }

        } catch (DiskFillerException ex) {
            ex.printStackTrace();
        }

    }

    private static DiskFiller createDirectories(HashMap<String, String> arguments, DiskFiller df) throws DiskFillerException {
        if (arguments.getOrDefault(PARAM_CREATE_DIR_CREATE, PARAM_CREATE_DIR_CREATE_DEFAULT).equalsIgnoreCase("true")) {
            //validamos el método, el método por default es el 0
            int method = Integer.parseInt(arguments.getOrDefault(PARAM_METHOD, PARAM_METHOD_DEFAULT));
            File pathsFile = new File(arguments.getOrDefault(bootstrap.PARAM_PATH_FILE, bootstrap.PARAM_PATH_FILE_DEFAULT));
            if (method == bootstrap.USE_EXIST_FILE_PATHS) {
                if (!pathsFile.isFile()) {
                    throw new DiskFillerException("File paths wasn't found in selected path (in parameters): " + pathsFile.getAbsolutePath());
                }

            } else if (method == bootstrap.CREATE_ALEATORY_FILE_PATHS) {
                return df.createAleatoryPathsDirectories(Integer.parseInt(arguments.getOrDefault(PARAM_CREATE_DIR_NDIRS, PARAM_CREATE_DIR_NDIRS_DEFAULT)),
                        Integer.parseInt(arguments.getOrDefault(PARAM_CREATE_DIR_MINDEPTH, PARAM_CREATE_DIR_MINDEPTH_DEFAULT)),
                        Integer.parseInt(arguments.getOrDefault(PARAM_CREATE_DIR_MAXDEPTH, PARAM_CREATE_DIR_MAXDEPTH_DEFAULT)),
                        Integer.parseInt(arguments.getOrDefault(PARAM_CREATE_DIR_MAXNSUBDIRECTORIES, PARAM_CREATE_DIR_MAXNSUBDIRECTORIES_DEFAULT)));
            }

            return df.createDirectoriesFromFilePaths(pathsFile.getAbsolutePath());
        }
        return df;
    }

}
