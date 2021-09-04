package br.com.starwars_api.repository;

import br.com.starwars_api.model.Planeta;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PlanetaRepository extends MongoRepository<Planeta, ObjectId> {

    public Optional<Planeta> findByNome(String nome);
}
