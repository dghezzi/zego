/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

/**
 *
 * @author Lu
 */
public class Pair<K,E> {
    
    
    private K k;
    private E e;
    
    public Pair(E _e, K _k) {
        k = _k;
        e = _e;
    }
    public void setFirst(K _k) {
        k = _k;
    }
    
    public void setSecond(E _e) {
        e = _e;
    }
    
    
    public E getSecond() {
        return e;
    }
    
    public K getFirst() {
        return k;
    }
    
}
