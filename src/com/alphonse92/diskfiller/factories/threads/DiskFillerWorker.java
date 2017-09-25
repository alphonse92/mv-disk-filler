/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphonse92.diskfiller.factories.threads;

import java.util.ArrayList;

/**
 *
 * @author Eliecer Alejandro Molina Vergel <alejandro_mover@hotmail.com>
 */
public class DiskFillerWorker extends Thread {

    private ArrayList<String> workLoad = null;
    private String name = null;
    private double maxBytesToWrite = 0;

    public DiskFillerWorker(String name, ArrayList<String> workLoad, double maxBytesToWrite) {
        this.workLoad = workLoad;
        this.maxBytesToWrite = maxBytesToWrite;
        System.out.println("DiskFillerWorker with name " + name + " was created, bytes to write: " + maxBytesToWrite);
    }

    @Override
    public void run() {
        /**
         * Implementar:
         *  1.dividir el numero de bytes máximo entre la cantidad de trabajo para obtener la cantidad de bytes x carga de trabajo
         *  2.iterar la cantidad de trabajo
         *  3.crear varios archivos que sumen la cantidad de bytes para esa carga de trabajo
         *  4. escribir los archivos en el disco.
         */
    }

}
