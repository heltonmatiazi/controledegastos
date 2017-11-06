package senac.com.br.controledegastos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.Collection;

//Created by helton on 31/08/2017.

@DatabaseTable(tableName = "mes")
public class Mes implements Serializable{

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField
    private String nome;

    @DatabaseField
    private String ano;

    @ForeignCollectionField
    private Collection<Orcamento> listaOrcamentos;

    @DatabaseField
    private Float cartaoMesAtual;

    @DatabaseField
    private Float cartaoMesAnterior;

    @DatabaseField
    private Float renda;

    @DatabaseField
    private boolean mesAtual;

    // initializing empty constructor;
    public Mes(){

    }

    public Mes(Integer id) {
        this.id = id;
    }

    public Mes(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Mes(Integer id, String nome, String ano, Float cartaoMesAtual, Float cartaoMesAnterior, Float renda, boolean mesAtual) {
        this.id = id;
        this.nome = nome;
        this.ano = ano;
        this.cartaoMesAtual = cartaoMesAtual;
        this.cartaoMesAnterior = cartaoMesAnterior;
        this.renda = renda;
        this.mesAtual = mesAtual;
    }

    public Mes(Integer id, String nome, String ano, Collection<Orcamento> listaOrcamentos, Float cartaoMesAtual, Float cartaoMesAnterior, Float renda, boolean mesAtual) {
        this.id = id;
        this.nome = nome;
        this.ano = ano;
        this.listaOrcamentos = listaOrcamentos;
        this.cartaoMesAtual = cartaoMesAtual;
        this.cartaoMesAnterior = cartaoMesAnterior;
        this.renda = renda;
        this.mesAtual = mesAtual;
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

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Float getRenda() {
        return renda;
    }

    public void setRenda(Float renda) {
        this.renda = renda;
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

    public boolean isMesAtual() {
        return mesAtual;
    }

    public void setMesAtual(boolean mesAtual) {
        this.mesAtual = mesAtual;
    }

}