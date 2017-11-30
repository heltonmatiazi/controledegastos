package senac.com.br.controledegastos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.Collection;

//Created by Helton Matiazi on 31/08/2017.

@DatabaseTable(tableName = "orcamento")
public class Orcamento implements Serializable {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField
    private String nome;

    @DatabaseField
    private Float saldo;

    @DatabaseField
    private Float valorInicial;

    @DatabaseField
    private boolean gastoMultiplo;

    @ForeignCollectionField
    private Collection<Gasto> listaDeGastos;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Mes mes;

    @DatabaseField
    private String formadePagamento;

    // initializing empty constructor
    public Orcamento(){
        mes = null;
    };

    public Orcamento(Integer id){
        this.id = id;
    }

    public Orcamento(String nome, Float saldo, Float valorInicial, boolean gastoMultiplo, Mes mes, String formadePagamento) {
        this.nome = nome;
        this.saldo = saldo;
        this.valorInicial = valorInicial;
        this.gastoMultiplo = gastoMultiplo;
        this.mes = mes;
        this.formadePagamento = formadePagamento;
    }

    public Orcamento(Integer id, String nome, Float saldo, Float valorInicial, boolean gastoMultiplo, Mes mes, String formadePagamento) {
        this.id = id;
        this.nome = nome;
        this.saldo = saldo;
        this.valorInicial = valorInicial;
        this.gastoMultiplo = gastoMultiplo;
        this.mes = mes;
        this.formadePagamento = formadePagamento;
    }

    public Orcamento(Integer id, String nome, Float saldo, Float valorInicial, boolean gastoMultiplo, Mes mes, Collection<Gasto> listaDeGastos, String formadePagamento) {
        this.id = id;
        this.nome = nome;
        this.saldo = saldo;
        this.valorInicial = valorInicial;
        this.gastoMultiplo = gastoMultiplo;
        this.mes = mes;
        this.listaDeGastos = listaDeGastos;
        this.formadePagamento = formadePagamento;
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

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }

    public Float getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(Float valorInicial) {
        this.valorInicial = valorInicial;
    }

    public boolean isGastoMultiplo() {
        return gastoMultiplo;
    }

    public void setGastoMultiplo(boolean gastoMultiplo) {
        this.gastoMultiplo = gastoMultiplo;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Collection<Gasto> getListaDeGastos() {
        return listaDeGastos;
    }

    public void setListaDeGastos(Collection<Gasto> listaDeGastos) {
        this.listaDeGastos = listaDeGastos;
    }

    public String getFormadePagamento() {
        return formadePagamento;
    }

    public void setFormadePagamento(String formadePagamento) {
        this.formadePagamento = formadePagamento;
    }

}