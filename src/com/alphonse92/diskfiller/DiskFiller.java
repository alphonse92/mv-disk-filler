/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphonse92.diskfiller;

import com.alphonse92.diskfiller.Exception.DiskFillerException;
import com.alphonse92.diskfiller.factories.ThreadFactory;
import com.alphonse92.diskfiller.factories.threads.DiskFillerWorker;
import com.alphonse92.diskfiller.util.DebugUtil;
import com.alphonse92.diskfiller.util.FileUtil;
import com.alphonse92.diskfiller.util.Numbers;
import com.alphonse92.diskfiller.util.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Eliecer Alejandro Molina Vergel <alejandro_mover@hotmail.com>
 */
public class DiskFiller {

    public static byte VERBOSE_NONE = 0;
    public static byte VERBOSE_ALL = 1;
    private String root = null;
    private boolean status = true;
    private String status_message = "";
    private Exception status_ex = null;
    private ArrayList<File> filesArray = null;
    private boolean debug = false;
    private byte verbose = 0;
    private ArrayList<String> lines = null;

    public DiskFiller(String root) {
        this.root = root;
        this.verbose = DiskFiller.VERBOSE_NONE;
    }

    public DiskFiller debug(boolean debug, byte verbose) {
        this.debug = debug;
        this.verbose = verbose;
        return this;
    }

    public DiskFiller createDirectoriesFromFilePaths(String pathToFilePaths) throws DiskFillerException {
        String method = "createDirectories";
        this.valideStatus();

        try {
            this.lines = this.lines == null ? this.getArrayOfPaths(pathToFilePaths) : this.lines;
            this.filesArray = this.getFilesFromArrayPaths(this.lines);
            boolean success = true;
            for (File file : this.filesArray) {
                success = success && file.mkdir();
                debug((success ? DebugUtil.TYPE_NORMAL : DebugUtil.TYPE_ERROR), method, "Creating directory on disk \"" + file.getAbsolutePath() + "\" " + (success ? "[OK]" : "[FAIL]"));
            }
        } catch (IOException ex) {
            this.manageException(ex.getMessage(), ex);
        }

        return this;
    }

    public DiskFiller createAleatoryPathsDirectories(int numberOfDirectories, int minDepth, int maxDepth, int maxNSubdirectories) throws DiskFillerException {
        try {
            String pathToFilePaths = "rutas";
            this.lines = getAleatoryPaths(numberOfDirectories, Numbers.getRandomInt(minDepth, maxDepth), maxNSubdirectories, 0, "", new ArrayList<String>());
            try (PrintWriter writer = new PrintWriter(pathToFilePaths, "UTF-8")) {
                this.lines.forEach((line) -> {
                    debug(DebugUtil.TYPE_NORMAL, "createDirectories", "Writting directory in file path\"" + line + "\"");
                    writer.println(line);
                });
                writer.close();
            }
        } catch (IOException e) {
            this.manageException(e.getMessage(), e);
        } catch (Exception e) {
            this.manageException(e.getMessage(), e);
        }

        return this;
    }

    /**
     * Metodo para obetener un arbol de directorios
     *
     * @param numberOfDirectories el numero total de directorios (raiz) que se
     * crearán, No es el total sumando los subdirectorios, pues estos son
     * aleatorios
     * @param depth profundidad máxima del árbol de directorios desde la carpeta
     * raiz (depth=0)''
     * @param level nivel del arbol donde se están creando los subdirectorios
     * @param root raiz actual del nivel directorio
     * @return ArrayList con todas las rutas a cada uno de los directorios y
     * subdirectorios
     */
    private ArrayList<String> getAleatoryPaths(int numberOfDirectories, int depth, int maxNDirectoriesPerLevel, int level, String root, ArrayList<String> lines) {
        if (level <= depth) {
            while (numberOfDirectories > 0) {
                //creamos el nombre del directorio
                String child = root + File.separator + StringUtil.getRandomString(10, true, true, false);
                //agregamos al array
                lines.add(child);
                //recursivamente llamamos la función, pasandole como parámetro el numero de subdirectorios que debe tener
                lines = this.getAleatoryPaths(Numbers.getRandomInt(0, maxNDirectoriesPerLevel), depth, maxNDirectoriesPerLevel, level + 1, child, lines);
                numberOfDirectories--;

            }
        }
        return lines;

    }

    private void manageException(String msg, Exception e) throws DiskFillerException {
        this.status = false;
        this.status_message = msg;
        this.status_ex = e;
        throw new DiskFillerException(e.getMessage());
    }

    private void valideStatus() throws DiskFillerException {
        if (!this.status) {
            this.status_ex.printStackTrace();
            this.manageException(this.status_message, new DiskFillerException(this.status_message));
        }
    }

    private void removeRootChildren() {
        FileUtil.purgeDirectory(new File(this.root));
    }

    private ArrayList<File> getFilesFromArrayPaths(ArrayList<String> arrayPaths) {
        ArrayList<File> out = new ArrayList();
        for (String path : arrayPaths) {
            if (path.length() > 0) {
                out.add(new File(this.root + File.separator + path));
            }
        }
        return out;
    }

    private ArrayList<String> getArrayOfPaths(String pathToFilePaths) throws FileNotFoundException, IOException {
        ArrayList<String> out = new ArrayList();
        FileReader fr = new FileReader(pathToFilePaths);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            out.add(line);
        }
        return out;
    }

    private void debug(byte type, String method, String text) {
        if (this.debug && this.verbose == DiskFiller.VERBOSE_ALL) {
            DebugUtil.debug(type, DiskFiller.class.getSimpleName(), method, text);
        }

    }

    public DiskFiller fill(int nWorkers, int size, byte measure) {

        byte index = 1;
        float bytesxworker = ((float) size) / ((float) nWorkers);
        for (ArrayList<String> workLoad : this.getWorksWorkLoad(nWorkers)) {
            DiskFillerWorker dfw = ThreadFactory.getWorker("WORKER #" + index, workLoad, bytesxworker, measure);
            dfw.start();
            index++;
        }

        return this;

    }

    private ArrayList<ArrayList<String>> getWorksWorkLoad(int nWorkers) {

        ArrayList<ArrayList<String>> out = new ArrayList();
        ArrayList<String> current_chunk = new ArrayList();
        //obtenemos el numero de cargas de trabajo para cada worker
        int sizeChunk = ((int) (this.lines.size() / nWorkers)) + 1;
        //declaramos el index
        int current_chunk_index = 0;
        for (String path : this.lines) {
            //obtenemos el indice, usamos el operador modulo para evitar 
            //cargas de trbajo desiguales
            current_chunk_index = ++current_chunk_index % sizeChunk;
            //añadios el path al chunk actual
            current_chunk.add(path);
            //si el indice ahora es 0 entonces guardamos la carga de trabajo
            //para el worker y reseteamos el chunk actual
            if (current_chunk_index == 0) {
                out.add(current_chunk);
                current_chunk = new ArrayList();
            }
        }

        return out;

    }

    //getters and setters
    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

}
