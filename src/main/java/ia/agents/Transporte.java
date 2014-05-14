/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

/**
 * Clase que representa a una unidad de transporte
 */
@SuppressWarnings("unused")
public class Transporte {
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
}
