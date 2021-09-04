package br.com.starwars_api.service;

import br.com.starwars_api.model.Planeta;
import br.com.starwars_api.repository.PlanetaRepository;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class PlanetaService {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Autowired
    PlanetaRepository planetaRepository;

    public List<Planeta> buscaTodosPlanetas() {
        return planetaRepository.findAll();
    }

    public JSONArray buscaTodosPlanetasAPI() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://swapi.dev/api/planets/"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObj = new JSONObject(response.body());

        return jsonObj.getJSONArray("results");
    }

    public Planeta adicionaPlaneta(Planeta planeta) throws IOException, InterruptedException {
        JSONArray jsonArray = buscaTodosPlanetasAPI();

        for (int i = 0, l = jsonArray.length(); i < l; i++) {
            JSONObject jsonPlaneta = jsonArray.getJSONObject(i);
            String nome = jsonPlaneta.getString("name");

            if (planeta.getNome().equals(nome)) {
                planeta.setQuantidadeFilme(jsonPlaneta.getJSONArray("films").length());
                break;
            }
        }
        return planetaRepository.save(planeta);
    }

    public Optional<Planeta> buscarPlanetaByNome(String nome) {
        return planetaRepository.findByNome(nome);
    }

    public Optional<Planeta> buscarPlanetaById(String id) {
        return planetaRepository.findById(new ObjectId(id));
    }

    public void removePlaneta(Planeta planeta) {
        planetaRepository.delete(planeta);
    }
}
