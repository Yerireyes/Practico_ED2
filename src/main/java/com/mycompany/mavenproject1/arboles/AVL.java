/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.arboles;

import com.mycompany.mavenproject1.excepciones.ExcepcionClaveNoExiste;

/**
 *
 * @author Yeri
 * @param <K>
 * @param <V>
 */
public class AVL<K extends Comparable<K>, V> extends ArbolBinarioBusqueda<K,V> {
    private static final byte DIFERENCIA_PERMITIDA = 1;
    
    @Override
    public V eliminar(K claveAEliminar) throws ExcepcionClaveNoExiste {
        V valorAEliminar = this.buscar(claveAEliminar);
        if (valorAEliminar == null) {
            throw new ExcepcionClaveNoExiste();
        }
        
        this.raiz = eliminar(this.raiz, claveAEliminar);
        return valorAEliminar;
    }
    
    private NodoBinario<K,V> eliminar(NodoBinario<K,V> nodoActual, K claveAEliminar){
        K claveActual = nodoActual.getClave();
        
        if (claveAEliminar.compareTo(claveActual) < 0) {
            NodoBinario<K,V> supuestoNuevoHijoIzq = eliminar(nodoActual.getHijoIzquierdo(),claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzq);
            return nodoActual;
        }
        if (claveAEliminar.compareTo(claveActual) > 0) {
            NodoBinario<K,V> supuestoNuevoHijoDer = eliminar(nodoActual.getHijoDerecho(),claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoDer);
            return nodoActual;
        }
        //CASO 1
        if (nodoActual.esHoja()) {
            return NodoBinario.nodoVacio();
        }
        
        //CASO 2
        //2.1
        if (!nodoActual.esVacioHijoIzquierdo() &&
                nodoActual.esVacioHijoDerecho()) {
            return nodoActual.getHijoIzquierdo();
        }
        //2.2
        if (nodoActual.esVacioHijoIzquierdo() &&
                !nodoActual.esVacioHijoDerecho()) {
            return nodoActual.getHijoDerecho();
        }
        //CASO 3
        NodoBinario<K,V> nodoDelSucesor = buscarSucesor(nodoActual.getHijoDerecho());
        NodoBinario<K,V> supuestoNuevoHijo = eliminar(nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());
        nodoActual.setHijoDerecho(supuestoNuevoHijo);
        nodoActual.setClave(nodoDelSucesor.getClave());
        nodoActual.setValor(nodoDelSucesor.getValor());
        return nodoActual;
    }
    @Override
    public void insertar(K claveAInsertar, V valorAInsertar){
        if (valorAInsertar == null) {
            throw new RuntimeException("No se permiten valores nulos");
        }
        this.raiz = insertar(this.raiz, claveAInsertar, valorAInsertar);
    }
    
    private NodoBinario<K,V> insertar(NodoBinario<K,V> nodoActual, K claveAInsertar, V valorAInsertar){
        if (NodoBinario.esNodoVacio(nodoActual)) {
            NodoBinario<K,V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAInsertar);
            return nuevoNodo;
        }
        K claveAtcual = nodoActual.getClave();
        if (claveAInsertar.compareTo(claveAtcual)<0) {
            NodoBinario<K,V> nuevoSupuestoHijo = insertar(nodoActual.getHijoIzquierdo(),claveAInsertar, valorAInsertar);
            nodoActual.setHijoIzquierdo(nuevoSupuestoHijo);
            return balancear(nodoActual);
        }
        
        if (claveAInsertar.compareTo(claveAtcual)>0) {
            NodoBinario<K,V> nuevoSupuestoHijo = insertar(nodoActual.getHijoDerecho(),claveAInsertar, valorAInsertar);
            nodoActual.setHijoDerecho(nuevoSupuestoHijo);
            return balancear(nodoActual);
        }
        
        nodoActual.setValor(valorAInsertar);
        return nodoActual;
    }
    
    private NodoBinario<K,V> balancear(NodoBinario<K,V> nodoActual){
        int alturaPorIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = altura(nodoActual.getHijoDerecho());
        int diferenciaDeAltura = alturaPorIzquierda - alturaPorDerecha;
        if (diferenciaDeAltura > DIFERENCIA_PERMITIDA) {
            //Rotacion a Derecha
            NodoBinario<K,V> hijoIzquierdoDelActual = nodoActual.getHijoIzquierdo();
            alturaPorIzquierda = altura(hijoIzquierdoDelActual.getHijoIzquierdo());
            alturaPorDerecha = altura(hijoIzquierdoDelActual.getHijoDerecho());
            if (alturaPorDerecha > alturaPorIzquierda) {
                return rotacionDobleADerecha(nodoActual);
            }
            return rotacionSimpleADerecha(nodoActual);
        }else if (diferenciaDeAltura < -DIFERENCIA_PERMITIDA) {
            //Rotacion a Izquierda
            NodoBinario<K,V> hijoDerechoDelActual = nodoActual.getHijoDerecho();
            alturaPorIzquierda = altura(hijoDerechoDelActual.getHijoIzquierdo());
            alturaPorDerecha = altura(hijoDerechoDelActual.getHijoDerecho());
            if (alturaPorDerecha < alturaPorIzquierda) {
                return rotacionDobleAIzquierda(nodoActual);
            }
            return rotacionSimpleAIzquierda(nodoActual);
        }
        return nodoActual;
    }
    
    private NodoBinario<K,V> rotacionSimpleADerecha(NodoBinario<K,V> nodoActual){
       NodoBinario<K,V> nodoQueRota = nodoActual.getHijoIzquierdo();
       nodoActual.setHijoIzquierdo(nodoQueRota.getHijoDerecho());
       nodoQueRota.setHijoDerecho(nodoActual);
       return nodoQueRota;
    }
    
    private NodoBinario<K,V> rotacionSimpleAIzquierda(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoQueRota = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(nodoQueRota.getHijoIzquierdo());
        nodoQueRota.setHijoIzquierdo(nodoActual);
        return nodoQueRota;
    }
    
    private NodoBinario<K,V> rotacionDobleADerecha(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoQueRotaPrimero = rotacionSimpleAIzquierda(nodoActual.getHijoIzquierdo());
        nodoActual.setHijoIzquierdo(nodoQueRotaPrimero);
        return rotacionSimpleADerecha(nodoActual);
        
    }
    
    private NodoBinario<K,V> rotacionDobleAIzquierda(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoQueRotaPrimero = rotacionSimpleADerecha(nodoActual.getHijoDerecho());
        nodoActual.setHijoDerecho(nodoQueRotaPrimero);
        return rotacionSimpleAIzquierda(nodoActual);
    }
}
