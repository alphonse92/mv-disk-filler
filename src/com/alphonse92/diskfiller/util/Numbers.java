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
public class Numbers {

    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * max + min);
    }
}
