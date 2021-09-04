package br.com.starwars_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "planeta")
public class Planeta {

    @Id
    private String id;
    private String nome;
    private String clima;
    private String terreno;
    private Integer quantidadeFilme;

    public Planeta(String nome, String clima, String terreno) {
        this.nome = nome;
        this.clima = clima;
        this.terreno = terreno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getTerreno() {
        return terreno;
    }

    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }

    public Integer getQuantidadeFilme() {
        return quantidadeFilme;
    }

    public void setQuantidadeFilme(Integer quantidadeFilme) {
        this.quantidadeFilme = quantidadeFilme;
    }

    public boolean isSomeEmpty() {
        return nome == null || clima == null || terreno == null;
    }
}
