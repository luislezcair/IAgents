/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import jade.core.Agent;

@SuppressWarnings("unused")
public class Transporte extends Agent {
    private String destino;
    private int tipo;
    private int capacidadDisp;
    private double descuento;

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getCapacidadDisp() {
        return capacidadDisp;
    }

    public void setCapacidadDisp(int capacidadDisp) {
        this.capacidadDisp = capacidadDisp;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    @Override
    protected void setup() {
        System.out.println("Hola, soy el agente transporte "
                + getLocalName() + " y funciono de maravillas");
        System.out.println("GUID: " + getName());
    }
}
