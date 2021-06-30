/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.arboles;

import com.mycompany.mavenproject1.excepciones.ExcepcionClaveNoExiste;
import com.mycompany.mavenproject1.excepciones.ExcepcionOrdenInvalido;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Yeri
 */
public class ArbolMViasBusqueda<K extends Comparable<K>, V> 
    implements IArbolBusqueda<K, V> {
    protected NodoMVias<K,V> raiz;
    protected int orden;
    protected int POSICION_INVALIDA = -1;
    
    public ArbolMViasBusqueda(){
        this.orden = 3;
    }
    
    public ArbolMViasBusqueda(int orden) throws ExcepcionOrdenInvalido{
        if (orden < 3) {
            throw new ExcepcionOrdenInvalido();
        }
        
        this.orden = orden;
 
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (valorAInsertar == null) {
            throw new RuntimeException("No se permite insertar valores nulos");
        }

        if (claveAInsertar == null){
            throw new RuntimeException("No se permite insertar claves nulas ");
        }
        if (this.esArbolVacio()){
            this.raiz = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
            return;
        }

        NodoMVias<K,V> nodoActual = this.raiz;

        while (!NodoMVias.esNodoVacio(nodoActual)){
            int posicionDeClave = this.obtenerPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClave != POSICION_INVALIDA){
                nodoActual.setValor(posicionDeClave, valorAInsertar);
                nodoActual = NodoMVias.nodoVacio();
            } else  {
                if (nodoActual.esHoja()){
                    if (nodoActual.estanClavesLlenas()){
                        int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                        NodoMVias<K,V> nuevoHijo = new NodoMVias<>(this.orden,claveAInsertar,valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar,nuevoHijo);
                    } else {
                        this.insertarClaveYValorOrdenadaEnNodo(nodoActual,claveAInsertar, valorAInsertar);
                    }
                    nodoActual = NodoMVias.nodoVacio();
                }else {
                    int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                    if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                        NodoMVias<K,V> nuevoHijo = new NodoMVias<>(this.orden,claveAInsertar,valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                        nodoActual = NodoMVias.nodoVacio();
                    }else{
                        nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                    }
                }
            }
        }
    }
    private int obtenerPosicionDeClave(NodoMVias<K,V> nodoActual, K claveAInsertar){
        for (int i = 0; i < this.orden ; i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAInsertar.compareTo(claveActual) < 0) {
                return i;
            }
        }
        return -1;
    }
    
    private int obtenerPosicionPorDondeBajar(NodoMVias<K,V> nodoActual, K claveAInsertar){
        for (int i = 0; i < this.orden ; i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAInsertar.compareTo(claveActual) < 0) {
                return i;
            }
        }
        return -1;
    }
    
    private int insertarClaveYValorOrdenadaEnNodo(NodoMVias<K,V> nodoActual, K claveAInsertar, V valorAInsertar){
        for (int i = 0; i < this.orden ; i++) {
            if (nodoActual.esClaveVacia(i)) {
                nodoActual.setClave(i, claveAInsertar);
                nodoActual.setValor(i, valorAInsertar);
            }
            K claveActual = nodoActual.getClave(i);
            V valorActual = nodoActual.getValor(i);
            if (claveAInsertar.compareTo(claveActual) < 0) {
                return i;
            }
            
        }
    }
            
    

    @Override
    public V eliminar(K claveAEliminar) throws ExcepcionClaveNoExiste {
        return null;
    }

    @Override
    public V buscar(K claveABuscar) {
        
        NodoMVias<K,V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)){
            boolean huboCambioDeNodoActual = false;
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias() &&
                                !huboCambioDeNodoActual; i++) {
               K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual.getValor(i);
                }
                if (claveABuscar.compareTo(claveActual) < 0) {
                    nodoActual = nodoActual.getHijo(i);
                    huboCambioDeNodoActual = true;
                    
                }
            }
            
            if (!huboCambioDeNodoActual) {
                nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias());
            }
        }
        return (V) NodoMVias.datoVacio();
    }

    @Override
    public boolean contiene(K claveABuscar) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }
    
    private int altura(NodoMVias<K,V> nodoActual){
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaMayor = 0;
        for (int i = 0; i < this.orden; i++) {
            int alturaActual = altura(nodoActual.getHijo(i));
            if (alturaActual > alturaMayor) {
                alturaMayor = alturaActual;
            }
        }
        
        return alturaMayor + 1;
       
    }

    @Override
    public int nivel() {
        return 0;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        
        Queue<NodoMVias<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()){
            NodoMVias<K,V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
                recorrido.add(nodoActual.getClave(i));
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }
            
            if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacias())) {
                colaDeNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()));
            }
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnPreOrden(this.raiz,recorrido);
        return recorrido;
    }
    
    private void recorridoEnPreOrden(NodoMVias<K,V> nodoActual, List<K> recorrido){
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorrido.add(nodoActual.getClave(i));
            recorridoEnPreOrden(nodoActual.getHijo(i), recorrido);
        }
        
        recorridoEnPreOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), recorrido);
    
    }

    @Override
    public List<K> recorridoEnInOrden() {
        return null;
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnPostOrden(this.raiz,recorrido);
        return recorrido;
    }
    
    private void recorridoEnPostOrden(NodoMVias<K,V> nodoActual, List<K> recorrido){
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        
        recorridoEnPostOrden(nodoActual.getHijo(0),recorrido);
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorridoEnPostOrden(nodoActual.getHijo(i+1), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
        
       }

    public K claveMayor (){
        if (NodoMVias.esNodoVacio(this.raiz)) {
            return null;
        }
        NodoMVias<K,V> nodoActual = this.raiz;
        while(!nodoActual.esHijoVacio(2)){
            nodoActual = nodoActual.getHijo(2);
        }
        K claveActual = nodoActual.getClave(0);
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias()-1; i++) {
            K claveSiguiente = nodoActual.getClave(i+1);
            if (claveActual.compareTo(claveSiguiente)<0) {
                claveActual = claveSiguiente;
            }
        }
        return claveActual;
    }
    
}
