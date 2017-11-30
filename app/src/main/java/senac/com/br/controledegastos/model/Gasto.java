package senac.com.br.controledegastos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.Date;

//Created by Helton Matiazi on 31/08/2017.

@DatabaseTable(tableName = "gasto")
public class Gasto implements Serializable {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField
    private String nome;

    @DatabaseField
    private String local;

    @DatabaseField
    private String formadePagamento;

    @DatabaseField
    private Float valor;

    @DatabaseField
    private String date;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Orcamento orcamento;

    //initializing empty constructor
    public Gasto(){

    }

    public Gasto(Integer id){
        this.id = id;
    }

    public Gasto(String nome, String local, String formadePagamento, Float valor, String date, Orcamento orcamento) {
        this.nome = nome;
        this.local = local;
        this.formadePagamento = formadePagamento;
        this.valor = valor;
        this.date = date;
        this.orcamento = orcamento;
    }

    public Gasto(Integer id, String nome, String local, String formadePagamento, Float valor, String date, Orcamento orcamento) {
        this.id = id;
        this.nome = nome;
        this.local = local;
        this.formadePagamento = formadePagamento;
        this.valor = valor;
        this.date = date;
        this.orcamento = orcamento;
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getFormadePagamento() {
        return formadePagamento;
    }

    public void setFormadePagamento(String formadePagamento) {
        this.formadePagamento = formadePagamento;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }
}