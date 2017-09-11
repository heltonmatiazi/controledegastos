package senac.com.br.controledegastos.model;

import java.util.ArrayList;

/**
 * Created by helton on 31/08/2017.
 */

public class Orcamento {
    public String nome;
    public Float saldo;
    public Float valorInicial;
    public ArrayList<Gasto> listaDeGastos;
    // initializing empty constructor
    public Orcamento(){};

    public Orcamento(String nome, Float saldo, Float valorInicial, ArrayList<Gasto> listaDeGastos) {
        this.nome = nome;
        this.saldo = saldo;
        this.valorInicial = valorInicial;
        this.listaDeGastos = listaDeGastos;
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

    public ArrayList<Gasto> getListaDeGastos() {
        return listaDeGastos;
    }

    public void setListaDeGastos(ArrayList<Gasto> listaDeGastos) {
        this.listaDeGastos = listaDeGastos;
    }
}
