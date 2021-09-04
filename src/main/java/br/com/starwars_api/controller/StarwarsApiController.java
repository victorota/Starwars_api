package br.com.starwars_api.controller;

import br.com.starwars_api.model.Planeta;
import br.com.starwars_api.service.PlanetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/v1")
public class StarwarsApiController {

    @Autowired
    PlanetaService planetaService;

    @GetMapping("")
    public ResponseEntity<HttpStatus> root() {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/planeta")
    public ResponseEntity<?> adicionaPlaneta(@RequestBody Planeta planeta) {
        try {
            if (planeta.isSomeEmpty())
                throw new Exception("O plenta precisa ter um nome, clima e terreno.");
            return new ResponseEntity<>(planetaService.adicionaPlaneta(planeta), HttpStatus.CREATED);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/planeta/api")
    public ResponseEntity<?> buscaTodosPlanetasAPI() {
        try {
            return ResponseEntity.ok(planetaService.buscaTodosPlanetasAPI().toString());
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    @GetMapping("/planeta/all")
    public ResponseEntity<List<Planeta>> buscaTodosPlanetas() {
        return ResponseEntity.ok(planetaService.buscaTodosPlanetas());
    }

    @GetMapping("/planeta")
    public ResponseEntity<?> buscarPlanetaByNome(@RequestParam(required = false) String nome,
                                                 @RequestParam(required = false) String id) {
        if (nome != null) {
            Optional<Planeta> planeta = planetaService.buscarPlanetaByNome(nome);
            if (planeta.isPresent()) {
                return ResponseEntity.ok(Collections.singletonList(planeta.get()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível encontrar um planeta com o nome " + nome);
            }
        } else if (id != null) {
            Optional<Planeta> planeta = planetaService.buscarPlanetaById(id);
            if (planeta.isPresent()) {
                return ResponseEntity.ok(Collections.singletonList(planeta.get()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível encontrar um planeta com o id " + id);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\"Id\" ou \"Nome\" é necessário para realizar a busca");
        }
    }

    @DeleteMapping("/planeta")
    public ResponseEntity<?> removePlaneta(@RequestParam(required = true) String id) {
        Optional<Planeta> planeta = planetaService.buscarPlanetaById(id);
        if (planeta.isPresent()) {
            planetaService.removePlaneta(planeta.get());
            return ResponseEntity.ok("Planeta removido!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível encontrar um planeta com o id " + id);
        }
    }
}
