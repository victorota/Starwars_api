package br.com.starwars_api.controller;

import br.com.starwars_api.model.Planeta;
import br.com.starwars_api.service.PlanetaService;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest
class StarwarsApiControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetaService planetaService;

    private Planeta mockPlaneta = new Planeta("Teste_Nome", "Teste_Clima", "Teste_Terreno");

    @Test
    void DeveBuscarPlanetaPeloNome() throws Exception {
        when(planetaService.buscarPlanetaByNome(Mockito.anyString())).thenReturn(Optional.of(mockPlaneta));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/planeta?nome=Teste_Nome")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{nome: Teste_Nome, clima: Teste_Clima, terreno: Teste_Terreno}]";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void DeveBuscarPlanetaPeloId() throws Exception {
        when(planetaService.buscarPlanetaById(Mockito.anyString())).thenReturn(Optional.of(mockPlaneta));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/planeta?id=61327dbd296b7f332a260780")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{nome: Teste_Nome, clima: Teste_Clima, terreno: Teste_Terreno}]";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void NaoDeveBuscarPlanetaPeloId() throws Exception {
        when(planetaService.buscarPlanetaById(Mockito.anyString())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/planeta?id=61327dbd296b7f332a260780")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void NaoDeveBuscarPlanetaPeloNome() throws Exception {
        when(planetaService.buscarPlanetaById(Mockito.anyString())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/planeta?nome=Terra")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void DeveBuscarTodosOsPlanetas() throws Exception {
        when(planetaService.buscaTodosPlanetas()).thenReturn(Arrays.asList(mockPlaneta));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/planeta/all");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{nome: Teste_Nome, clima: Teste_Clima, terreno: Teste_Terreno}]";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void DeveBuscarTodosOsPlanetasAPI() throws Exception {
        when(planetaService.buscaTodosPlanetasAPI()).thenReturn(new JSONArray("[{name: Tatooine, rotation_period: 23}]"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/planeta/api");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{name: Tatooine}]";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    @Test
    void DeveAdicionarPlanetas() throws Exception {
        when(planetaService.adicionaPlaneta(mockPlaneta)).thenReturn(mockPlaneta);
        String jsonPlaneta = "{\"nome\":\"Teste_Nome\",\"clima\":\"Teste_Clima\",\"terreno\":\"Teste_Terreno\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/planeta")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonPlaneta)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    @Test
    void NaoDeveAdicionarPlanetas() throws Exception {
        when(planetaService.adicionaPlaneta(mockPlaneta)).thenReturn(mockPlaneta);
        String jsonPlaneta = "{\"nome\":\"Teste_Nome\",\"clima\":\"Teste_Clima\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/planeta")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonPlaneta)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void DeveRemoverPlanetas() throws Exception {
        when(planetaService.buscarPlanetaById(Mockito.anyString())).thenReturn(Optional.of(mockPlaneta));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/v1/planeta?id=613227496f9dd52e84d57acf");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    void NaoDeveRemoverPlanetas() throws Exception {
        when(planetaService.buscarPlanetaById(Mockito.anyString())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/v1/planeta?id=613227496f9dd52e84d57acf");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }
}