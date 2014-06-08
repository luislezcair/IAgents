/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.util;

import ia.agents.ontology.TurismoOntology;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

import java.util.List;

/**
 * Clase con métodos estáticos para facilitar el registro y deregistro de
 * agentes en el DF.
 */
public class DFRegisterer {
    public static void register(Agent agent, String serviceType,
                            List<Property> properties) {
        agent.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                String language = new SLCodec().getName();
                String ontology = TurismoOntology.getInstance().getName();

                DFAgentDescription ad = new DFAgentDescription();
                ad.setName(agent.getAID());
                ad.addLanguages(language);
                ad.addOntologies(ontology);

                ServiceDescription sd = new ServiceDescription();
                sd.setType(serviceType);
                sd.setName(serviceType + "-" + agent.getLocalName());
                sd.addOntologies(ontology);
                sd.addLanguages(language);

                if(properties != null && !properties.isEmpty())
                    properties.forEach(sd::addProperties);

                ad.addServices(sd);

                try {
                    DFService.register(agent, ad);
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void deregister(Agent agent) {
        try {
            DFService.deregister(agent);
        } catch (FIPAException e) {
            System.out.println(agent.getLocalName() +
                    " no pudo deregistrarse del DF");
        }
    }
}
