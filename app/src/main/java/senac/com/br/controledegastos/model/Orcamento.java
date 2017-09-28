package senac.com.br.controledegastos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import java.util.Collection;

/**
 * Created by helton on 31/08/2017.
 */

public class Orcamento {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String nome;

    @DatabaseField(canBeNull = false)
    private Float saldo;

    @DatabaseField(canBeNull = false)
    private Float valorInicial;

    @DatabaseField(canBeNull = false)
    private boolean brench;

    @ForeignCollectionField
    private Collection<Gasto> listaDeGastos;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Mes mes;

    // initializing empty constructor
    public Orcamento(){

    };

    public Orcamento(Integer id){

    }

    public Orcamento(String nome, Float saldo, Float valorInicial, boolean brench, Mes mes) {
        this.nome = nome;
        this.saldo = saldo;
        this.valorInicial = valorInicial;
        this.brench = brench;
        this.mes = mes;
    }

    public Orcamento(String nome, Float saldo, Float valorInicial, boolean brench, Mes mes, Collection<Gasto> listaDeGastos) {
        this.nome = nome;
        this.saldo = saldo;
        this.valorInicial = valorInicial;
        this.brench = brench;
        this.mes = mes;
        this.listaDeGastos = listaDeGastos;
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

    public boolean isBrench() {
        return brench;
    }

    public void setBrench(boolean brench) {
        this.brench = brench;
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

    //Arrumar o toString
    @Override
    public String toString() {
        return "Item de Orcamento{" +
                ", nome='" + nome + '\'' +
                ", saldo=" + saldo +
                ", valorInicial=" + valorInicial +
                ", brench=" + brench +
                ", listaDeGastos=" + listaDeGastos +
                '}';
    }
}