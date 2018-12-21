package com.hfad.botserver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String NODOS_TABLE_CREATE = "CREATE TABLE nodos(_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, sensor TEXT, actuador TEXT, mac TEXT)";
    private static final String DB_NAME = "nodos.sqlite";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;

    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db=this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NODOS_TABLE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Insertar un nuevo comentario
    public void insertar(String nombre,String sensor,String actuador,String mac){
        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        cv.put("sensor", sensor);
        cv.put("actuador", actuador);
        cv.put("mac", mac);
        db.insert("nodos", null, cv);
    }

    //Borrar un comentario a partir de su id
    public void borrar(int id){
        String[] args = new String[]{String.valueOf(id)};
        db.delete("comments", "_id=?", args);
    }

    //Obtener la lista de comentarios en la base de datos
    public ArrayList<Nodo> getNodos(){
        //Creamos el cursor
        ArrayList<Nodo> lista=new ArrayList<Nodo>();
        Cursor c = db.rawQuery("select _id, nombre,sensor,actuador,mac from nodos", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                String nombre = c.getString(c.getColumnIndex("nombre"));
                String sensor = c.getString(c.getColumnIndex("sensor"));
                String actuador = c.getString(c.getColumnIndex("actuador"));
                String mac = c.getString(c.getColumnIndex("mac"));
                int id=c.getInt(c.getColumnIndex("_id"));
                //Nodo com =new Nodo(id,user,comment);
                //Añadimos el comentario a la lista
                //lista.add(com);
            } while (c.moveToNext());
        }

        //Cerramos el cursor
        c.close();
        return lista;
    }

    public Nodo getNodoFromMac(String mac, PrintStream ps)
    {
        String[] args = new String[]{mac};
        Cursor c = db.rawQuery("select _id, nombre,sensor,actuador,mac from nodos where mac=?", args);
        Nodo nodo = null;
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                String nombre = c.getString(c.getColumnIndex("nombre"));
                String sensor = c.getString(c.getColumnIndex("sensor"));
                String actuador = c.getString(c.getColumnIndex("actuador"));
                mac = c.getString(c.getColumnIndex("mac"));
                int id=c.getInt(c.getColumnIndex("_id"));
                nodo = new Nodo(nombre,sensor,actuador,mac,ps);
                //Añadimos el comentario a la lista
                //lista.add(com);
            } while (c.moveToNext());
        }

        //Cerramos el cursor
        c.close();
        return nodo;
    }

    public void actualizar(String name, String sensorName, String actuadorName, String mac) {
        String[] args = new String[]{mac};
        Cursor c = db.rawQuery("select _id, nombre,sensor,actuador,mac from nodos where mac=?", args);
        int id = 0;
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                //String nombre = c.getString(c.getColumnIndex("nombre"));
                //String sensor = c.getString(c.getColumnIndex("sensor"));
                //String actuador = c.getString(c.getColumnIndex("actuador"));
                //mac = c.getString(c.getColumnIndex("mac"));
                id=c.getInt(c.getColumnIndex("_id"));
                //Añadimos el comentario a la lista
                //lista.add(com);
            } while (c.moveToNext());
        }

        if(id != 0)
        {
            ContentValues cv = new ContentValues();
            cv.put("nombre", name);
            cv.put("sensor", sensorName);
            cv.put("actuador", actuadorName);
            cv.put("mac", mac);
            String[] argsUpdate = new String[]{String.valueOf(id)};
            db.update("nodos", cv,"_id=?",argsUpdate);
        }
        //Cerramos el cursor
        c.close();
    }
}