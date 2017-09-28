package senac.com.br.controledegastos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import java.util.Collection;

/**
 * Created by helton on 31/08/2017.
 */

public class Mes {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String nome;

    @ForeignCollectionField
    private Collection<Orcamento> listaOrcamentos;

    @DatabaseField(canBeNull = false)
    private Float cartaoMesAtual;

    @DatabaseField(canBeNull = false)
    private Float cartaoMesAnterior;

    // initializing empty constructor;
    public Mes(){

    }

    public Mes(Integer id) {
        this.id = id;
    }

    public Mes(Integer id, String nome, Collection<Orcamento> listaOrcamentos, Float cartaoMesAtual, Float cartaoMesAnterior) {
        this.id = id;
        this.nome = nome;
        this.listaOrcamentos = listaOrcamentos;
        this.cartaoMesAtual = cartaoMesAtual;
        this.cartaoMesAnterior = cartaoMesAnterior;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Collection<Orcamento> getListaOrcamentos() {
        return listaOrcamentos;
    }

    public void setListaOrcamentos(Collection<Orcamento> listaOrcamentos) {
        this.listaOrcamentos = listaOrcamentos;
    }

    public Float getCartaoMesAtual() {
        return cartaoMesAtual;
    }

    public void setCartaoMesAtual(Float cartaoMesAtual) {
        this.cartaoMesAtual = cartaoMesAtual;
    }

    public Float getCartaoMesAnterior() {
        return cartaoMesAnterior;
    }

    public void setCartaoMesAnterior(Float cartaoMesAnterior) {
        this.cartaoMesAnterior = cartaoMesAnterior;
    }

    @Override
    public String toString() {
        return "Mes{" +
                ", nome='" + nome + '\'' +
                ", listaOrcamentos=" + listaOrcamentos +
                ", cartaoMesAtual=" + cartaoMesAtual +
                ", cartaoMesAnterior=" + cartaoMesAnterior +
                '}';
    }

}