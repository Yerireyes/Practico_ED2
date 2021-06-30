/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.arboles;

/**
 *
 * @author Yeri
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ArbolBinarioBusqueda<Integer, String> tree = new ArbolBinarioBusqueda<>();
        tree.insertar(50, "joto");
        tree.insertar(70, "a");
        tree.insertar(30, "b");
        tree.insertar(80, "c");
        tree.insertar(25, "d");
        tree.insertar(35, "e");
        tree.insertar(55, "f");
        tree.insertar(10, "g");
        tree.insertar(11, "h");
        tree.insertar(2, "assd");
        tree.insertar(53, "sasd");
        tree.insertar(57, "fasd");
        tree.insertar(27, "fd");
        tree.insertar(90, "fda");
        tree.insertar(75, "fdd");
        
        ArbolBinarioBusqueda<Integer, String> tree2 = new ArbolBinarioBusqueda<>();
        tree2.insertar(50, "joto");
        tree2.insertar(70, "a");
        tree2.insertar(30, "b");
        tree2.insertar(80, "c");
        tree2.insertar(25, "d");
        tree2.insertar(35, "e");
        tree2.insertar(55, "f");
        tree2.insertar(10, "g");
        tree2.insertar(11, "h");
        tree2.insertar(2, "assd");
        tree2.insertar(53, "sasd");
        tree2.insertar(57, "fasd");
        tree2.insertar(27, "fd");
        tree2.insertar(90, "fda");
        tree2.insertar(75, "fdd");
        tree2.insertar(37, "fdsd");
        
        System.out.println("Altura: " + tree.altura());
        System.out.println("Cantidad De Nodos Con Dos Hijos :" + tree.cantidadDeNodoConHijosNoVaciosRec());
        System.out.println("Cantidad De Nodos Con Dos Hijos :" + tree.cantidadDeHijosNoVacios());
        System.out.println("Cantidad De Nodos con dos hijos en un nivel " + tree.cantidadDeHijosEnUnNivel(2));
        System.out.println("Cantidad de hijos hasta un nivel :" + tree.cantidadDeHijosHastaUnNivel(2));
        System.out.println("Cantidad De Hijos con dos hijos en un nivel recursivo : " + tree.cantidadDeHijosEnUnNivelRec(2));
        System.out.println("Cantidad de nodos con un hijo distinto de vacio hasta un nivel : " + tree.Ejercicio7(2));
        System.out.println("Cantidad de nodos InOrden : " + tree.Ejercicio12());
        System.out.println("Arboles iguales? " + tree.examen1(tree2));
    }
    
}
