/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.Concept;

/**
 * Un paquete turístico compuesto por un Lugar de alojamiento y un Transporte
 */
public class PaqueteAgencia implements Concept {
    private Alojamiento alojamiento;
    private Transporte transporte;

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public Transporte getTransporte() {
        return transporte;
    }

    public void setTransporte(Transporte transporte) {
        this.transporte = transporte;
    }
}
