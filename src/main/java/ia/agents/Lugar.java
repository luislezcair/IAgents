/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import jade.core.Agent;

@SuppressWarnings("unused")
public class Lugar extends Agent {
    private String destino;
    private int cantPersonas;
    private int tipoLugar;
    private double precioPorPersona;
    private double descuento;

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getCantPersonas() {
        return cantPersonas;
    }

    public void setCantPersonas(int cantPersonas) {
        this.cantPersonas = cantPersonas;
    }

    public int getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(int tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public double getPrecioPorPersona() {
        return precioPorPersona;
    }

    public void setPrecioPorPersona(double precioPorPersona) {
        this.precioPorPersona = precioPorPersona;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    @Override
    protected void setup() {
        System.out.println("Hola, soy el agente lugar "
                + getLocalName() + " y funciono de maravillas");
        System.out.println("GUID: " + getName());
    }
}
