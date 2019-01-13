package com.hfad.botserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Nodos {

    private static Nodos ptr_nodos = null;
    private Map almacenNodos = new HashMap<Integer,Nodo>();
    private ArrayList<Nodo> almacen = new ArrayList<Nodo>();

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

    public static void setInstance(Nodos nodo)
    {
        ptr_nodos = nodo;
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

    public void processNodoData(String from, String msj)
    {
        String [] split = msj.split(" ");
        Set set = almacenNodos.entrySet();
        Iterator iterator = set.iterator();

        while(iterator.hasNext()) {
            Map.Entry mEntry = (Map.Entry)iterator.next();
            Integer key = (Integer) mEntry.getKey();
            Nodo value = (Nodo) mEntry.getValue();
            if(value.getName().equals(split[0]))
            {
                value.processDataRecibido(from , msj);
                return;
            }
        }

    }

    /*public Nodo[] getAlmacen()
    {

        Nodo[] almacen = new Nodo[almacenNodos.size()];
        int i = 0;
        Set set = almacenNodos.entrySet();
        Iterator iterator = set.iterator();

        while(iterator.hasNext()) {
            Map.Entry mEntry = (Map.Entry)iterator.next();
            Integer key = (Integer) mEntry.getKey();
            Nodo value = (Nodo) mEntry.getValue();
            almacen[i] = value;
            i++;
        }
        return almacen;
    }*/

    public ArrayList<Nodo> getAlmacen()
    {
        almacen.clear();
        int i = 0;
        Set set = almacenNodos.entrySet();
        Iterator iterator = set.iterator();

        while(iterator.hasNext()) {
            Map.Entry mEntry = (Map.Entry)iterator.next();
            Integer key = (Integer) mEntry.getKey();
            Nodo value = (Nodo) mEntry.getValue();
            almacen.add(value);
            i++;
        }
        return almacen;
    }

}
