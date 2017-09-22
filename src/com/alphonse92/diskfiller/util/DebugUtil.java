/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphonse92.diskfiller.util;

/**
 *
 * @author Eliecer Alejandro Molina Vergel <alejandro_mover@hotmail.com>
 */
public class DebugUtil {

    public static void debug(String name, String method, String message) {
        System.out.println(name + "." + method + " -> " + message);
    }
}
