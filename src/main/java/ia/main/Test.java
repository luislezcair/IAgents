package ia.main;

import com.google.gson.*;
import ia.main.storage.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String args[]) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String agentsFile = readFile("initial_agents.json");

        JsonParser parser = new JsonParser();
        JsonObject jsonAgents = parser.parse(agentsFile).getAsJsonObject();

        List<JsonAgent> agents = new ArrayList<>();

        Map<String, Class<? extends JsonAgent>> agentTypes = new HashMap<>();
        agentTypes.put("turistas", JsonTurista.class);
        agentTypes.put("agencias", JsonAgencia.class);
        agentTypes.put("transportes", JsonTransporte.class);
        agentTypes.put("lugares", JsonLugar.class);

        for(Map.Entry<String, Class<? extends JsonAgent>> entry : agentTypes.entrySet()) {
            JsonArray agentArray = jsonAgents.getAsJsonArray(entry.getKey());

            for (JsonElement agentElement : agentArray) {
                JsonAgent agent = gson.fromJson(agentElement, entry.getValue());
                System.out.println(agent);
                agents.add(agent);
            }
        }
    }

    private static String readFile(String path)
    {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
