package senac.com.br.controledegastos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

//Created by helton on 31/08/2017.

@DatabaseTable(tableName = "gasto")
public class Gasto {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = true)
    private String nome;

    @DatabaseField(canBeNull = false)
    private String local;

    @DatabaseField(canBeNull = false)
    private Enum formadePagamento;

    @DatabaseField(canBeNull = false)
    private Float valor;

    @DatabaseField(canBeNull = false)
    private Date date;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Orcamento orcamento;

    //initializing empty constructor
    public Gasto(){

    }

    public Gasto(Integer id){
        this.id = id;
    }

    public Gasto(Integer id, String nome, String local, Enum formadePagamento, Float valor, Date date, Orcamento orcamento) {
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

    public Enum getFormadePagamento() {
        return formadePagamento;
    }

    public void setFormadePagamento(Enum formadePagamento) {
        this.formadePagamento = formadePagamento;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //Arrumar o toString
    @Override
    public String toString() {
        return "Gasto{" +
                "nome='" + nome + '\'' +
                ", local='" + local + '\'' +
                ", formadePagamento=" + formadePagamento +
                ", valor=" + valor +
                ", date=" + date +
                '}';
    }
}