/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.excepciones;

/**
 *
 * @author Yeri
 */
public class ExcepcionClaveNoExiste extends Exception {

    /**
     * Creates a new instance of <code>ExcepcionClaveNoExiste</code> without
     * detail message.
     */
    public ExcepcionClaveNoExiste() {
        super("Clave no existente en el árbol");
    }

    /**
     * Constructs an instance of <code>ExcepcionClaveNoExiste</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ExcepcionClaveNoExiste(String msg) {
        super(msg);
    }
}
