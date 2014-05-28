/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.onto.*;

public class TurismoOntology extends BeanOntology {
    private static final String ONTOLOGY_NAME = "turismo-ontology";

    // Implementación del patrón Singleton
    private static Ontology instance = new TurismoOntology();

    public static Ontology getInstance() {
        return instance;
    }

    private TurismoOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try {
            // Conceptos:
            add(Paquete.class);
            add(Alojamiento.class);
            add(Transporte.class);
            add(PaqueteAgencia.class);

            // Acciones
            add(ConsultarAction.class);
            add(OfertarLugarAction.class);
            add(OfertarTransporteAction.class);
            add(OfertarPaqueteAction.class);
        } catch(OntologyException oe) {
            oe.printStackTrace();
        }
    }
}
