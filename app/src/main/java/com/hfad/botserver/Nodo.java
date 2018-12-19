package com.hfad.botserver;

public class Nodo {

    private String name;
    private String sensorName;
    private String actuadorName;

    public Nodo(String name, String sensorName, String actuadorName) {
        this.name = name;
        this.sensorName = sensorName;
        this.actuadorName = actuadorName;
    }

    public Nodo(String name) {
        this.name = name;
        this.sensorName = "sensor";
        this.actuadorName = "actuador";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getActuadorName() {
        return actuadorName;
    }

    public void setActuadorName(String actuadorName) {
        this.actuadorName = actuadorName;
    }

    public void processData(String data)
    {
        if(data.equalsIgnoreCase("/sensor1\n"))
        {
            String msj;
            msj = this.name + " SENSOR 1 ON";
            System.out.println("ENVIAR ESTADO SENSOR");
            XMPPCliente.getInstance().sendMsj(msj);
        }
    }
}
