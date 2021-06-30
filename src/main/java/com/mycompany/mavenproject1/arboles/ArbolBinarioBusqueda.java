/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.arboles;

import com.mycompany.mavenproject1.excepciones.ExcepcionClaveNoExiste;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Yeri
 */
public class ArbolBinarioBusqueda<K extends Comparable<K>, V> 
    implements IArbolBusqueda<K, V> {
    
    protected NodoBinario<K,V>raiz;

    public ArbolBinarioBusqueda() {
    }
    
    

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (valorAInsertar == null) {
            throw new RuntimeException("No se permiten valores nulos");
        }
        
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(claveAInsertar, valorAInsertar);
            return;
        }
        
        NodoBinario<K,V> nodoAnterior = NodoBinario.nodoVacio();
        NodoBinario<K,V> nodoActual = this.raiz;
        
        while (!NodoBinario.esNodoVacio(nodoActual)){
            K claveActual = nodoActual.getClave()   ;
            if (claveAInsertar.compareTo(claveActual)<0) {
                nodoAnterior = nodoActual;
                nodoActual = nodoActual.getHijoIzquierdo();
            }else if (claveAInsertar.compareTo(claveActual)>0){
                nodoAnterior = nodoActual;
                nodoActual = nodoActual.getHijoDerecho();
            }else{
                nodoActual.setValor(valorAInsertar);
                return;
            }
        }
        
        NodoBinario<K,V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAInsertar);
        K claveAnterior = nodoAnterior.getClave();
        if (claveAInsertar.compareTo(claveAnterior)<0) {
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        }else {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        }
        
    }

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
    
    protected NodoBinario<K,V> buscarSucesor(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoAnterior = NodoBinario.nodoVacio();
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoAnterior;
    }

    @Override
    public V buscar(K claveABuscar) {
         NodoBinario<K,V> nodoActual = this.raiz;
         while (!NodoBinario.esNodoVacio(nodoActual)){
             K claveActual = nodoActual.getClave();
             if (claveABuscar.compareTo(claveActual)<0) {
                 nodoActual = nodoActual.getHijoIzquierdo();
             }else if (claveABuscar.compareTo(claveActual)>0){
                 nodoActual = nodoActual.getHijoDerecho();
             }else{
                 return nodoActual.getValor();
             }
         }
         return null;
    }

    @Override
    public boolean contiene(K claveABuscar) {
        return this.buscar(claveABuscar) != null;
    }

    @Override
    public int size() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantidadDeNodos = 0;
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()){
            NodoBinario<K,V> nodoActual = colaDeNodos.poll();
            cantidadDeNodos++;
            if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return cantidadDeNodos;
    }
    
    public int sizeRec(){
        return sizeRec(this.raiz);
    }
    
    private int sizeRec(NodoBinario<K,V> nodoActual){
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        
        int cantidadPorIzquierda = sizeRec(nodoActual.getHijoIzquierdo());
        int cantidadPorDerecha = sizeRec(nodoActual.getHijoDerecho());
        return cantidadPorIzquierda + cantidadPorDerecha + 1;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }
    
    public int altura(NodoBinario<K,V> nodoActual){
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        
        int alturaPorIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = altura(nodoActual.getHijoDerecho());
        return alturaPorIzquierda>alturaPorDerecha? alturaPorIzquierda+1: alturaPorDerecha+1;
    }
    
    public int alturaIt(){
        if (this.esArbolVacio()) {
            return 0;
        }
        int alturaDelArbol = 0;
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()){
            int nroDeNodosDelNivel = colaDeNodos.size();
            int posicion = 0;
            while (posicion < nroDeNodosDelNivel) {
                NodoBinario<K,V> nodoActual = colaDeNodos.poll();
                if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                posicion++;
            }
            alturaDelArbol++;
        }
        return alturaDelArbol;
    }

    @Override
    public int nivel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void nivelNumero(NodoBinario<K,V> nodoActual){
        
    }

    @Override
    public void vaciar() {
        this.raiz = NodoBinario.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(this.raiz);
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()){
            NodoBinario<K,V> nodoActual = colaDeNodos.poll();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return recorrido;
    }
    
    public List<K> recorridoEnPreOrdenRec(){
        List<K> recorrido = new LinkedList<>();
        recorridoEnPreOrdenRec(this.raiz,recorrido);
        return recorrido;
    }
    
    private void recorridoEnPreOrdenRec(NodoBinario<K,V> nodoActual, List<K> recorrido){
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return;
        }
        
        recorrido.add(nodoActual.getClave());
        recorridoEnPreOrdenRec(nodoActual.getHijoIzquierdo(), recorrido);
        recorridoEnPreOrdenRec(nodoActual.getHijoDerecho(), recorrido);
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)){
            recorrido.add(nodoActual.getClave());
            pilaDeNodos.push(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            if (!nodoActual.esVacioHijoDerecho()) {
                nodoActual = nodoActual.getHijoDerecho();
                pilaDeNodos.push(nodoActual);
                recorrido.add(nodoActual.getClave());
                if (!nodoActual.esVacioHijoIzquierdo()) {
                    nodoActual = nodoActual.getHijoIzquierdo();
                    while (!NodoBinario.esNodoVacio(nodoActual)){
                    recorrido.add(nodoActual.getClave());
                    pilaDeNodos.push(nodoActual);
                    nodoActual = nodoActual.getHijoIzquierdo();
                    }
                }
            }
            
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnInOrden() {
       List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)){
            pilaDeNodos.push(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esVacioHijoDerecho()) {
                nodoActual = nodoActual.getHijoDerecho();
                pilaDeNodos.push(nodoActual);
                if (!nodoActual.esVacioHijoIzquierdo()) {
                    nodoActual = nodoActual.getHijoIzquierdo();
                    while (!NodoBinario.esNodoVacio(nodoActual)){
                            pilaDeNodos.push(nodoActual);
                            nodoActual = nodoActual.getHijoIzquierdo();
                    }
                }
            }
            
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPostOrden() {
       List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)){
            pilaDeNodos.push(nodoActual);
            if (!nodoActual.esVacioHijoIzquierdo()) {
                nodoActual = nodoActual.getHijoIzquierdo();
            }else{
                nodoActual = nodoActual.getHijoDerecho();
            }
            
        }
        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!pilaDeNodos.isEmpty()) {
                NodoBinario<K,V> nodoDelTope = pilaDeNodos.peek();
                if (!nodoDelTope.esVacioHijoDerecho() && nodoDelTope.getHijoDerecho()!=nodoActual) {
                    nodoDelTope = nodoDelTope.getHijoDerecho();
                    while (!NodoBinario.esNodoVacio(nodoDelTope)){
                    pilaDeNodos.push(nodoDelTope);
                    if (!nodoDelTope.esVacioHijoIzquierdo()) {
                        nodoDelTope = nodoDelTope.getHijoIzquierdo();
                    }else{
                        nodoDelTope = nodoDelTope.getHijoDerecho();
                    }
                }
            }  
        }
        
        }
        return recorrido;
    }
    
    public int cantidadDeHijosVacios() {
        int hijosVacios = 0;
        if (this.esArbolVacio()) {
            return 0;
        }
        
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(this.raiz);
        while (!pilaDeNodos.isEmpty()){
            NodoBinario<K,V> nodoActual = pilaDeNodos.pop();
            
            if (!nodoActual.esVacioHijoDerecho()) {
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }else{
                hijosVacios++;
            }
            if (!nodoActual.esVacioHijoIzquierdo()) {
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }else{
                hijosVacios++;
            }
            
        }
        return hijosVacios;
    }
    
    public int cantidadDeHijosNoVacios(){
        int nodosConDosHijosNoVacios = 0;
        if (this.esArbolVacio()) {
            return 0;
        }
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push((this.raiz));
        while(!pilaDeNodos.isEmpty()){
            int contador = 0;
            NodoBinario<K,V> nodoActual = pilaDeNodos.pop();
            if (!nodoActual.esVacioHijoDerecho()) {
                contador++;
                pilaDeNodos.add(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esVacioHijoIzquierdo()) {
                contador++;
                pilaDeNodos.add(nodoActual.getHijoIzquierdo());
            }
            if (contador == 2) {
                nodosConDosHijosNoVacios++;
            }
        }
        return nodosConDosHijosNoVacios ;
    }
    
    public int cantidadDeNodoConHijosNoVaciosRec(){
        if (this.esArbolVacio()) {
            return 0;
        }
        return cantidadDeNodoConHijosNoVaciosRec(this.raiz) ;
    }
    
    private int cantidadDeNodoConHijosNoVaciosRec(NodoBinario<K,V> nodoActual){
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0 ;
        }

        int cantidadDeHijosPorIzquierda = cantidadDeNodoConHijosNoVaciosRec(nodoActual.getHijoIzquierdo());
        int cantidadDeHijosPorDerecha = cantidadDeNodoConHijosNoVaciosRec(nodoActual.getHijoDerecho());
        int cantidadTotal = cantidadDeHijosPorIzquierda + cantidadDeHijosPorDerecha;
        if (!nodoActual.esVacioHijoIzquierdo() && !nodoActual.esVacioHijoDerecho()) {
            cantidadTotal++;
        }
        return cantidadTotal ;
    }
    
    public int cantidadDeHijosEnUnNivel(int nivel){
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantidad = 0;
        int nivelActual = 0;
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty() && nivelActual <= nivel){
            int cantidadDeHijos = colaDeNodos.size();
            int posicion = 0;
            cantidad = 0;
            while (posicion < cantidadDeHijos) {
                NodoBinario<K,V> nodoActual = colaDeNodos.poll();
                if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                if (!nodoActual.esVacioHijoIzquierdo() && !nodoActual.esVacioHijoDerecho()) {
                    cantidad++;
                }
                posicion++;
            }
            nivelActual++;
        }
        return cantidad;
    }
    
    public int cantidadDeHijosHastaUnNivel(int nivel){
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantidad = 0;
        int nivelActual = 0;
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty() && nivelActual < nivel){
            int cantidadDeHijos = colaDeNodos.size();
            int posicion = 0;
            cantidad = cantidad + cantidadDeHijos;
            while (posicion < cantidadDeHijos) {
                NodoBinario<K,V> nodoActual = colaDeNodos.poll();
                if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                posicion++;
            }
            nivelActual++;
        }
        cantidad = cantidad + colaDeNodos.size() - 1 ;
        return cantidad;
    }
    
    public int cantidadDeHijosEnUnNivelRec(int nivel){
        if (this.esArbolVacio()) {
            return 0;
        }
        return cantidadDeHijosEnUnNivelRec(this.raiz,nivel);
    }
    
    private int cantidadDeHijosEnUnNivelRec(NodoBinario<K,V> nodoActual, int nivel){
        if (nivel>=0) {
            if (NodoBinario.esNodoVacio(nodoActual)) {
                return 0;
            }
            int cantidadDeNodosConDosHijosPorIzquierda = cantidadDeHijosEnUnNivelRec(nodoActual.getHijoIzquierdo(),nivel-1);
            int cantidadDeNodosConDosHijosPorDerecha = cantidadDeHijosEnUnNivelRec(nodoActual.getHijoDerecho(),nivel-1);
            int cantidadTotal = cantidadDeNodosConDosHijosPorIzquierda + cantidadDeNodosConDosHijosPorDerecha;
            if (!nodoActual.esVacioHijoIzquierdo() && !nodoActual.esVacioHijoDerecho() && nivel==0) {
                cantidadTotal++;
            }
            return cantidadTotal;
        }
        return 0;
    }
    
    public int Ejercicio7(int nivel){
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantidad = 0;
        int nivelActual = 0;
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty() && nivelActual <= nivel){
            int cantidadDeHijos = colaDeNodos.size();
            int posicion = 0;
            while (posicion < cantidadDeHijos) {
                NodoBinario<K,V> nodoActual = colaDeNodos.poll();
                if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                posicion++;
                if ((!nodoActual.esVacioHijoIzquierdo() && nodoActual.esVacioHijoDerecho()) ||
                        (!nodoActual.esVacioHijoDerecho() && nodoActual.esVacioHijoIzquierdo())) {
                    cantidad++;
                }
            }
            nivelActual++;
        }
        return cantidad;
    }
    public int Ejercicio12(){
        if (this.esArbolVacio()) {
            return 0;
        }
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)){
            pilaDeNodos.push(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        int cantidadDeNodos = 0;
        while (!pilaDeNodos.isEmpty()){
            cantidadDeNodos++;
            nodoActual = pilaDeNodos.pop();
            if (!nodoActual.esVacioHijoDerecho()) {
                nodoActual = nodoActual.getHijoDerecho();
                pilaDeNodos.push(nodoActual);
                if (!nodoActual.esVacioHijoIzquierdo()) {
                    nodoActual = nodoActual.getHijoIzquierdo();
                    while (!NodoBinario.esNodoVacio(nodoActual)){
                        pilaDeNodos.push(nodoActual);
                        nodoActual = nodoActual.getHijoIzquierdo();
                    }
                }
                
            }
        }
        return cantidadDeNodos;
    }
    
    public ArbolBinarioBusqueda(List<K> clavesPostOrden, List<V> valoresPostOrden, List<K> clavesInOrden, List<V> valoresInOrden){
        if (clavesPostOrden.size()!= valoresPostOrden.size() || clavesInOrden.size()!=valoresInOrden.size()) {
            throw new RuntimeException("claves y valores con tamaño distinto");
        }
        int posicionRaizPostOrden = clavesPostOrden.size()-1;
        K claveRaiz = clavesPostOrden.get(posicionRaizPostOrden);
        V valorRaiz = valoresPostOrden.get(posicionRaizPostOrden);
        int posicionRaizInOrden = this.posicionDeClave(claveRaiz,clavesInOrden);
        //rama por izquierda
        List<K> clavesInOrdenPorIzquierda = clavesInOrden.subList(0, posicionRaizInOrden);
        List<V> valoresInOrdenPorIzquierda = valoresInOrden.subList(0, posicionRaizInOrden);
        List<K> clavesPostOrdenPorIzquierda = clavesPostOrden.subList(0, posicionRaizInOrden);
        List<V> valoresPostOrdenPorIzquierda = valoresPostOrden.subList(0, posicionRaizInOrden);
        NodoBinario<K,V> hijoIzquierdo = reconstruccionPostOrden(clavesInOrdenPorIzquierda, valoresInOrdenPorIzquierda,
                                                                 clavesPostOrdenPorIzquierda, valoresPostOrdenPorIzquierda);
        //rama por derecha
        List<K> clavesInOrdenPorDerecha = clavesInOrden.subList(posicionRaizInOrden+1 , clavesInOrden.size());
        List<V> valoresInOrdenPorDerecha = valoresInOrden.subList(posicionRaizInOrden+1 , clavesInOrden.size());
        List<K> clavesPostOrdenPorDerecha = clavesPostOrden.subList(posicionRaizInOrden , clavesInOrden.size()-1);
        List<V> valoresPostOrdenPorDerecha = valoresPostOrden.subList(posicionRaizInOrden , clavesInOrden.size()-1);
        NodoBinario<K,V> hijoDerecho = reconstruccionPostOrden(clavesInOrdenPorDerecha, valoresInOrdenPorDerecha,
                                                                 clavesPostOrdenPorDerecha, valoresPostOrdenPorDerecha);
        NodoBinario<K,V> nodoActual = new NodoBinario<>(claveRaiz,valorRaiz);
        nodoActual.setHijoDerecho(hijoDerecho);
        nodoActual.setHijoIzquierdo(hijoIzquierdo);
        this.raiz = nodoActual;
    }
    private NodoBinario<K,V> reconstruccionPostOrden(List<K> clavesInOrden, List<V> valoresInOrden, List<K> clavesPostOrden, List<V> valoresPostOrden){
        if (clavesInOrden.isEmpty()) {
            return NodoBinario.nodoVacio();
        }
        int posicionRaizPostOrden = clavesPostOrden.size()-1;
        K claveRaiz = clavesPostOrden.get(posicionRaizPostOrden);
        V valorRaiz = valoresPostOrden.get(posicionRaizPostOrden);
        int posicionRaizInOrden = posicionDeClave(claveRaiz,clavesInOrden);
        //rama por izquierda
        List<K> clavesInOrdenPorIzquierda = clavesInOrden.subList(0, posicionRaizInOrden);
        List<V> valoresInOrdenPorIzquierda = valoresInOrden.subList(0, posicionRaizInOrden);
        List<K> clavesPostOrdenPorIzquierda = clavesPostOrden.subList(0, posicionRaizInOrden);
        List<V> valoresPostOrdenPorIzquierda = valoresPostOrden.subList(0, posicionRaizInOrden);
        NodoBinario<K,V> hijoIzquierdo = reconstruccionPostOrden(clavesInOrdenPorIzquierda, valoresInOrdenPorIzquierda,
                                                                 clavesPostOrdenPorIzquierda, valoresPostOrdenPorIzquierda);
        //rama por derecha
        List<K> clavesInOrdenPorDerecha = clavesInOrden.subList(posicionRaizInOrden+1 , clavesInOrden.size());
        List<V> valoresInOrdenPorDerecha = valoresInOrden.subList(posicionRaizInOrden+1 , clavesInOrden.size());
        List<K> clavesPostOrdenPorDerecha = clavesPostOrden.subList(posicionRaizInOrden , clavesInOrden.size()-1);
        List<V> valoresPostOrdenPorDerecha = valoresPostOrden.subList(posicionRaizInOrden , clavesInOrden.size()-1);
        NodoBinario<K,V> hijoDerecho = reconstruccionPostOrden(clavesInOrdenPorDerecha, valoresInOrdenPorDerecha,
                                                                 clavesPostOrdenPorDerecha, valoresPostOrdenPorDerecha);
        NodoBinario<K,V> nodoActual = new NodoBinario<>(claveRaiz,valorRaiz);
        nodoActual.setHijoDerecho(hijoDerecho);
        nodoActual.setHijoIzquierdo(hijoIzquierdo);
        return nodoActual;
    }
    private int posicionDeClave(K claveAEncontrar, List<K> listaDeClaves){
        for (int i = 0; i < listaDeClaves.size(); i++) {
            K claveActual = listaDeClaves.get(i);
            if (claveActual.compareTo(claveAEncontrar) == 0) {
                return i;
            }
        }
        return -1;
    }
    public NodoBinario<K,V> ejercicio14(NodoBinario<K,V> nodoActual){
        return nodoSucesorInOrden(nodoActual);
    }
    private NodoBinario<K,V> nodoSucesorInOrden(NodoBinario<K,V> nodoActual){
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return NodoBinario.nodoVacio();
        }
        List<K> recorridoInOrden = this.recorridoEnInOrden();
        int posicionClaveActual = posicionDeClave(nodoActual.getClave(), recorridoInOrden);
        K claveDelSucesor = recorridoInOrden.get(posicionClaveActual + 1);
        V valorDelSucesor = this.buscar(claveDelSucesor);
        NodoBinario<K,V> nodoSucesor = new NodoBinario<>(claveDelSucesor, valorDelSucesor);
        return nodoSucesor;        
    }
    
    private NodoBinario<K,V> nodoPredecesorInOrden(NodoBinario<K,V> nodoActual){
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return NodoBinario.nodoVacio();
        }
        K claveAEncontrar = nodoActual.getClave();
        List<K> recorridoInOrden = this.recorridoEnInOrden();
        int posicionClaveActual = posicionDeClave(claveAEncontrar, recorridoInOrden);
        K claveDelSucesor = recorridoInOrden.get(posicionClaveActual - 1);
        V valorDelSucesor = this.buscar(claveDelSucesor);
        NodoBinario<K,V> nodoSucesor = new NodoBinario<>(claveDelSucesor, valorDelSucesor);
        return nodoSucesor;        
    }
    
    public K llaveMenor(){
        if (NodoBinario.esNodoVacio(this.raiz)) {
            return null;
        }
        NodoBinario<K,V> nodoActual = this.raiz;
        K claveActual = nodoActual.getClave();
        while(!NodoBinario.esNodoVacio(nodoActual)){
            claveActual = nodoActual.getClave();
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return claveActual;
    }
    
    public boolean Ejercicio21(){
        if (NodoBinario.esNodoVacio(this.raiz)) {
            return false;
        }
       int alturaMax = this.altura() - 1;
       int alturaActual = 0;
       while(alturaActual < alturaMax){
           Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
           colaDeNodos.offer(this.raiz);
           while(!colaDeNodos.isEmpty()){
               NodoBinario<K,V> nodoActual = colaDeNodos.poll();
               if (!nodoActual.esVacioHijoIzquierdo()) {
                   colaDeNodos.offer(nodoActual);
               }else{
                   return false;
               }
               if (!nodoActual.esVacioHijoDerecho()) {
                   colaDeNodos.offer(nodoActual);
               }else
                   return false;
           }
           alturaActual++;
       }
       return true;
    }
    
    public boolean examen1(ArbolBinarioBusqueda<K,V> arbolAComparar){
        List<K> arbolACompararDatos = arbolAComparar.recorridoEnInOrden();
        List<K> arbol = this.recorridoEnInOrden();
        for (int i = 0; i < arbolACompararDatos.size() - 1; i++) {
            if (arbolACompararDatos.get(i)!= arbol.get(i)) {
                return false;
            }
        }
        return true;
    }
}
