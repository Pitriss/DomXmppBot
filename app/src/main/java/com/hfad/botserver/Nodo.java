package com.hfad.botserver;

import android.app.Service;
import android.text.Editable;

import org.jxmpp.stringprep.XmppStringprepException;

import java.io.OutputStream;
import java.io.PrintStream;

public class Nodo {

    private String name;
    private String sensorName;
    private String actuadorName;
    private String mac;
    private PrintStream printStream;

    public Nodo(String name, String sensorName, String actuadorName, String mac, PrintStream printStream) {
        this.name = name;
        this.sensorName = sensorName;
        this.actuadorName = actuadorName;
        this.printStream = printStream;
        this.mac = mac;
    }

    public Nodo(String name, String mac, PrintStream printStream) {
        this.name = name;
        this.sensorName = "sensor";
        this.actuadorName = "actuador";
        this.mac = mac;
        this.printStream = printStream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replaceAll("\\s+","");
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName.replaceAll("\\s+","");
    }

    public String getActuadorName() {
        return actuadorName;
    }

    public void setActuadorName(String actuadorName) {
        this.actuadorName = actuadorName.replaceAll("\\s+","");
    }

    public void processData(String data) throws XmppStringprepException {
        if((data.equalsIgnoreCase("/sensor1\n"))&&(XMPPCliente.getInstance().isAlarmaArmada())) {
            String msj;
            msj = this.name + " " + this.sensorName + " ON";
            System.out.println("ENVIAR ESTADO SENSOR");
            XMPPCliente.getInstance().sendMsj(msj);
            new Thread(new sendPicture()).start();
        }
    }

    private class sendPicture extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                XMPPCliente.getInstance().sendFile(Server.getInstance().takePhoto());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void processDataRecibido(String from,String msj)
    {
        printStream.print(msj);
    }

    public String getMac()
    {
        return mac;
    }

    public void updateDbInfo()
    {
        Server.getInstance().updateNodoDBInfo(this);
    }
}
