package com.hfad.botserver;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterNodoItem extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Nodo> items;

    public AdapterNodoItem (Activity activity, ArrayList<Nodo> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Nodo> nodo) {
        for (int i = 0; i < nodo.size(); i++) {
            items.add(nodo.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_nodo, null);
        }

        Nodo dir = items.get(position);

        TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(dir.getName());

        TextView sensor = (TextView) v.findViewById(R.id.sensor);
        sensor.setText(dir.getSensorName());

        TextView actuador = (TextView) v.findViewById(R.id.actuador);
        actuador.setText(dir.getActuadorName());


        return v;
    }
}
