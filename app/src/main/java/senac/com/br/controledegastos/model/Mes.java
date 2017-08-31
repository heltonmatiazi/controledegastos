package senac.com.br.controledegastos.model;

import java.util.ArrayList;

/**
 * Created by helton on 31/08/2017.
 */

public class Mes {
    public String nome;
    public ArrayList<Orcamento> listaOrcamentos;
    public Float cartaoMesAtual;
    public Float cartaoMesAnterior;
    // initializing empty constructor;
    public Mes(){

    };
    public Mes(String nome, ArrayList<Orcamento> listaOrcamentos, Float cartaoMesAtual, Float cartaoMesAnterior) {
        this.nome = nome;
        this.listaOrcamentos = listaOrcamentos;
        this.cartaoMesAtual = cartaoMesAtual;
        this.cartaoMesAnterior = cartaoMesAnterior;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Orcamento> getListaOrcamentos() {
        return listaOrcamentos;
    }

    public void setListaOrcamentos(ArrayList<Orcamento> listaOrcamentos) {
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


}
