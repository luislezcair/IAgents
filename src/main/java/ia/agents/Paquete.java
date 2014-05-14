/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import java.util.Date;

/**
 * Clase que epresenta a un paquete de viaje
 */
@SuppressWarnings("unused")
public class Paquete {
	private int cantPersonas;
    private Date fecha;
    private String tipoLugar;
    private String destino;
    private int cantDias;
    private double importeMaxPP;
    private int formaPago;

    public int getCantPersonas() {
        return cantPersonas;
    }

    public void setCantPersonas(int cantPersonas) {
        this.cantPersonas = cantPersonas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getCantDias() {
        return cantDias;
    }

    public void setCantDias(int cantDias) {
        this.cantDias = cantDias;
    }

    public double getImporteMaxPP() {
        return importeMaxPP;
    }

    public void setImporteMaxPP(double importeMaxPP) {
        this.importeMaxPP = importeMaxPP;
    }

    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }
}