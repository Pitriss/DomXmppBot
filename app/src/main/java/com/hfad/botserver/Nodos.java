package com.hfad.botserver;

import java.util.HashMap;
import java.util.Map;

public class Nodos {

    private static Nodos ptr_nodos = null;
    private Map almacenNodos = new HashMap<Integer,Nodo>();

    private Nodos() {
    }

    public static Nodos getInstance()
    {
        if(ptr_nodos == null)
        {
            ptr_nodos = new Nodos();
        }
        return ptr_nodos;
    }

    public boolean saveNodo(Integer id, Nodo nodo)
    {
        if(!almacenNodos.containsKey(id))
        {
            almacenNodos.put(id, nodo);
            return true;
        }
        return false;
    }

    public Nodo getNodo(Integer id)
    {
        Nodo nodo = null;
        if(almacenNodos.containsKey(id))
        {
            nodo = (Nodo) almacenNodos.get(id);
        }
        return nodo;
    }

    public boolean deleteNodo(Integer id)
    {
        if(almacenNodos.containsKey(id))
        {
            almacenNodos.remove(id);
            return true;
        }
        return false;
    }

    public Integer countNodos()
    {
        return almacenNodos.size();
    }

}
