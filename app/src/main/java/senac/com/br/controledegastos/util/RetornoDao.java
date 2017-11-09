package senac.com.br.controledegastos.util;

//Created by Carlos Lohmeyer on 19/10/2017.

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedUpdate;

import java.sql.SQLException;
import java.util.ArrayList;
import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.activities.NovoItemActivity;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;
import senac.com.br.controledegastos.R;

public class RetornoDao{

    private Dao<Mes, Integer> mesDao;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Dao<Gasto, Integer> gastoDao;
    private Mes mes, mesTeste1, mesTeste2;
    private String nome;
    private ArrayList<Mes> meses;
    private ArrayList<Orcamento> orcamentos, orcamentos1;
    private ArrayList<Gasto> gastos, gastos1;

    //RETORNA APENAS O MÊS ATUAL
    public Mes retornaMesAtual(Context context){
        iniciar();
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            meses = (ArrayList<Mes>) mesDao.queryForAll();
            for(int x = 0; x < meses.size(); x++){
                if(meses.get(x).isMesAtual() == true){
                    mes = meses.get(x);
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return mes;
    }

    //RETORNA A LISTA DE MESES DENTRO DO BANCO
    public ArrayList<Mes> retornaListaDeMeses(Context context){
        iniciar();
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            meses = (ArrayList<Mes>) mesDao.queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return meses;
    }

    //RETORNA A LISTA DE ITENS DE ORÇAMENTO INDEPENDENTE SE O MÊS É O ATUAL OU NÃO
    public ArrayList<Orcamento> retornaListaDeOrcamentos(Context context, Mes m){
        iniciar();
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            orcamentos = (ArrayList<Orcamento>) orcamentoDao.queryForAll();
            for(int y = 0; y < orcamentos.size(); y++){
                if(orcamentos.get(y).getMes().getId().equals(m.getId())){
                    orcamentos1.add(orcamentos.get(y));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orcamentos1;
    }

    //RETORNA A LISTA DE ITENS DE ORÇAMENTO QUE TENHAM GASTOS INDEPENDENTE SE O MÊS É O ATUAL OU NÃO
    public ArrayList<Orcamento> retornarListaDeOrcamentosComGastos(Context context, Mes m){
        iniciar();
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            orcamentos = (ArrayList<Orcamento>) orcamentoDao.queryForAll();
            for(int w = 0; w < orcamentos.size(); w++){
                if(orcamentos.get(w).getMes().getId().equals(m.getId()) && orcamentos.get(w).isGastoMultiplo() == true){
                    orcamentos1.add(orcamentos.get(w));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orcamentos1;
    }

    //RETORNA A LISTA DE GASTOS DENTRO DE UM ITEM DE ORÇAMENTO ESPECÍFICO
    public ArrayList<Gasto> retornaListaDeGastos(Context context, Orcamento o){
        iniciar();
        try {
            gastoDao = MyORMLiteHelper.getInstance(context).getGastoDao();
            gastos = (ArrayList<Gasto>) gastoDao.queryForAll();
            for(int z = 0; z < gastos.size(); z++){
                if(gastos.get(z).getOrcamento().getId().equals(o.getId())){
                    gastos1.add(gastos.get(z));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gastos1;
    }

    //RETORNA A LISTA DE ITENS DE ORÇAMENTO APENAS DO MÊS ATUAL
    public ArrayList<Orcamento> retornaListaDeOrcamentosAtuais(Context context){
        iniciar();
        mes = retornaMesAtual(context);
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            orcamentos = (ArrayList<Orcamento>) orcamentoDao.queryForAll();
            for(int y = 0; y < orcamentos.size(); y++){
                if(orcamentos.get(y).getMes().getId().equals(mes.getId())){
                    orcamentos1.add(orcamentos.get(y));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orcamentos1;
    }

    //RETORNA A LISTA DE ITENS DE ORÇAMENTO APENAS DO MÊS ATUAL E QUE TENHAM GASTOS
    public ArrayList<Orcamento> retornarListaDeOrcamentosComGastosAtuais(Context context){
        iniciar();
        mes = retornaMesAtual(context);
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            orcamentos = (ArrayList<Orcamento>) orcamentoDao.queryForAll();
            for(int w = 0; w < orcamentos.size(); w++){
                if(orcamentos.get(w).getMes().getId().equals(mes.getId()) && orcamentos.get(w).isGastoMultiplo() == true){
                    orcamentos1.add(orcamentos.get(w));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orcamentos1;
    }

    //TRAZ A DATA FORMATADA EM STRING E COMPARA COM O NOME E ANO DO MÊS ATUAL
    public boolean verificarMesAtual(Mes mesBanco, String dataAtual, Context context){
        String date[] = dataAtual.split("/");
        String mes = date[1];
        String ano = date[2];
        Integer numeroMes = Integer.valueOf(mes);
        String nomeMes = retornarNomeMes(numeroMes, context);
        if(mesBanco.getNome().equals(nomeMes) && mesBanco.getAno().equals(ano)){
            return true;
        }else {
            return false;
        }
    }

    //MÉTODO QUE RECEBE O NÚMERO DO MÊS DA DATA FORMATADA E  RETORNA O NOME DO MÊS EM STRING
    private String retornarNomeMes(Integer mes, Context context){
        if (mes == 1) {
            nome = context.getString(R.string.mes_jan);
        }else if(mes == 2){
            nome = context.getString(R.string.mes_fev);
        }else if(mes == 3){
            nome = context.getString(R.string.mes_mar);
        }else if(mes == 4){
            nome = context.getString(R.string.mes_abr);
        }else if(mes == 5){
            nome = context.getString(R.string.mes_mai);
        }else if(mes == 6){
            nome = context.getString(R.string.mes_jun);
        }else if(mes == 7){
            nome = context.getString(R.string.mes_jul);
        }else if(mes == 8){
            nome = context.getString(R.string.mes_ago);
        }else if(mes == 9){
            nome = context.getString(R.string.mes_set);
        }else if(mes == 10){
            nome = context.getString(R.string.mes_out);
        }else if(mes == 11){
            nome = context.getString(R.string.mes_nov);
        }else if(mes == 12){
            nome = context.getString(R.string.mes_dez);
        }
        return nome;
    }
    //INICIA AS VARIÁVEIS QUE A CLASSE TRABALHA
    public void iniciar(){
        gastos = new ArrayList<Gasto>();
        gastos1 = new ArrayList<Gasto>();
        orcamentos = new ArrayList<Orcamento>();
        orcamentos1 = new ArrayList<Orcamento>();
        meses = new ArrayList<Mes>();
        mes = new Mes();
        mesTeste1 = new Mes();
        mesTeste2 = new Mes();
    }
    /*public ArrayList<Orcamento> stringsParaSpinner(Context context){
        System.out.println(" ENTROU NO METODO QUE POPULA O ARRAY ");
        orcs = new ArrayList<Orcamento>();
        Orcamento o1 = new Orcamento("PPPPPP");
        Orcamento o2 = new Orcamento("CCCCCCCC");
        Orcamento o3 = new Orcamento("MMMMMMMM");
        Orcamento o4 = new Orcamento("AAAAAAA");
        Orcamento o5 = new Orcamento("BBBBBBB");
        Orcamento o6 = new Orcamento(context.getString(R.string.cifrao));
        orcs.add(o1);
        orcs.add(o2);
        orcs.add(o3);
        orcs.add(o4);
        orcs.add(o5);
        orcs.add(o6);
        for(int a = 0; a < orcs.size(); a++){
            System.out.println("IMPRIMINDO ESSA MERDA NO PRIMEIRO MÃ‰TODO " + orcs.get(a).getNome());
        }
        return orcs;
    }*/
    public void inserirMesParaTeste(Context context) throws SQLException {
        iniciar();
        mesTeste1.setNome("OUTUBRO");
        mesTeste1.setAno("2017");
        mesTeste1.setMesAtual(true);
        mesTeste1.setCartaoMesAnterior(null);
        mesTeste1.setCartaoMesAtual(null);
        mesTeste1.setRenda(null);
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();

        Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mesTeste1);
        if(res.isCreated()){
            System.out.println("OBJETO mesTeste1 CRIADO COM SUCESSO!!!" + "\n");
        }else if(res.isUpdated()){
            System.out.println("OBJETO mesTeste1 ATUALIZADO COM SUCESSO!!!" + "\n");
        }else{
            System.out.println("OBJETO mesTeste1 ERRO INESPERADO!!!" + "\n");
        }
        inserirMesParaTeste2(context, mesTeste1);
        inserirMesParaTeste3(context);
    }
    public void inserirMesParaTeste2(Context context, Mes m) throws SQLException {
        iniciar();
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        m.setMesAtual(false);

        Dao.CreateOrUpdateStatus res1 = mesDao.createOrUpdate(m);
        if(res1.isCreated()){
            System.out.println("OBJETO mesTeste1 CRIADO COM SUCESSO!!!" + "\n");
        }else if(res1.isUpdated()){
            System.out.println("OBJETO mesTeste1 ATUALIZADO COM SUCESSO!!!" + "\n");
        }else{
            System.out.println("OBJETO mesTeste1 ERRO INESPERADO!!!" + "\n");
        }
    }
    public void inserirMesParaTeste3(Context context) throws SQLException {
        iniciar();
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        mesTeste2.setNome("NOVEMBRO");
        mesTeste2.setAno("2017");
        mesTeste2.setMesAtual(true);
        mesTeste2.setCartaoMesAnterior(null);
        mesTeste2.setCartaoMesAtual(null);
        mesTeste2.setRenda(null);
        Dao.CreateOrUpdateStatus res2 = mesDao.createOrUpdate(mesTeste2);
        if(res2.isCreated()){
            System.out.println("OBJETO mesTeste2 CRIADO COM SUCESSO!!!" + "\n");
        }else if(res2.isUpdated()){
            System.out.println("OBJETO mesTeste2 ATUALIZADO COM SUCESSO!!!" + "\n");
        }else{
            System.out.println("OBJETO mesTeste2 ERRO INESPERADO!!!" + "\n");
        }
    }

}