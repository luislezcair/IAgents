/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

/**
 * Clase que representa un lugar de alojamiento para los turistas
 */
@SuppressWarnings("unused")
public class Alojamiento {
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
}
