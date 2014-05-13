/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main.ui;

import javax.swing.*;

public class UIAgentManager {
    private JFrame mainFrame;

    public UIAgentManager() {
        mainFrame = new JFrame("Sistema multi-agente - "
                             + " Inteligencia Artificial");
    }

    public void setupUi() {
        // Cargar el contenido de la ventana ...
        // ...

        // Mostrar
        mainFrame.setVisible(true);
    }
}
