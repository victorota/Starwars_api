package br.com.starwars_api.service;

import br.com.starwars_api.model.Planeta;
import br.com.starwars_api.repository.PlanetaRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class PlanetaServiceTests {

    @Autowired
    PlanetaService planetaService;

    @MockBean
    PlanetaRepository planetaRepository;

    private Planeta mockPlaneta1 = new Planeta("Teste1", "Teste1", "Teste1");
    private Planeta mockPlaneta2 = new Planeta("Teste2", "Teste2", "Teste2");

    @Test
    void DeveBuscarTodosPlanetas() {
        when(planetaRepository.findAll()).thenReturn(Stream.of(mockPlaneta1, mockPlaneta2).collect(Collectors.toList()));
        assertEquals(2, planetaService.buscaTodosPlanetas().size());
    }

    @Test
    void NaoDeveAcharNenhumPlaneta() {
        when(planetaRepository.findAll()).thenReturn(new ArrayList<>());
        assertNotEquals(2, planetaService.buscaTodosPlanetas().size());
    }

    @Test
    void DeveAdicionarPlaneta() throws IOException, InterruptedException {
        Planeta mockPlaneta = new Planeta("Tatooine", "Teste_Clima", "Teste_Terreno");
        when(planetaRepository.save(mockPlaneta)).thenReturn(mockPlaneta);
        assertEquals(mockPlaneta, planetaService.adicionaPlaneta(mockPlaneta));
    }

    @Test
    void DeveBuscarPlanetaByNome() {
        when(planetaRepository.findByNome(Mockito.anyString())).thenReturn(Optional.of(mockPlaneta1));
        assertEquals(mockPlaneta1, planetaService.buscarPlanetaByNome(Mockito.anyString()).get());
    }

    @Test
    void NaoDeveAcharNenhumPlanetaByNome() {
        when(planetaRepository.findByNome(Mockito.anyString())).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), planetaService.buscarPlanetaByNome(Mockito.anyString()));
    }

    @Test
    void DeveBuscarPlanetaById() {
        when(planetaRepository.findById(new ObjectId("61329cc26ed2765e78be9ab4"))).thenReturn(Optional.of(mockPlaneta1));
        assertEquals(mockPlaneta1, planetaService.buscarPlanetaById("61329cc26ed2765e78be9ab4").get());
    }

    @Test
    void NaoDeveAcharNenhumPlanetaById() {
        when(planetaRepository.findById(new ObjectId("61329cc26ed2765e78be9ab4"))).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), planetaService.buscarPlanetaById("61329cc26ed2765e78be9ab4"));
    }
}