package senac.com.br.controledegastos.model;

import java.util.Date;

/**
 * Created by helton on 31/08/2017.
 */

public class Gasto {
    public String nome;
    public String local;
    public Enum formadePagamento;
    public Float valor;
    public Date date;
    //initializing empty constructor
    public Gasto(){

    };

    public Gasto(String nome, String local, Enum formadePagamento, Float valor, Date date) {
        this.nome = nome;
        this.local = local;
        this.formadePagamento = formadePagamento;
        this.valor = valor;
        this.date = date;
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
}
