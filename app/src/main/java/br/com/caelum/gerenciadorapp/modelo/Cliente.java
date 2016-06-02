package br.com.caelum.gerenciadorapp.modelo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by matheus on 02/05/16.
 */
public class Cliente implements Serializable {


    private Long id;
    private String nome;
    private List<String> telefones;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<String> telefones) {
        this.telefones = telefones;
    }


    @Override
    public String toString() {
        return nome;
    }
}
