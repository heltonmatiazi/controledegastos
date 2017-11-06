package senac.com.br.controledegastos.util;

//Created by Carlos Lohmeyer on 19/10/2017.

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;

public class RetornoDao{

    private Dao<Mes, Integer> mesDao;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Dao<Gasto, Integer> gastoDao;
    private Mes mes;
    private String nome;
    private Orcamento orcamento;
    private Gasto gasto;
    private ArrayList<Mes> meses;
    private ArrayList<Orcamento> orcamentos, orcamentos1;
    private ArrayList<Gasto> gastos, gastos1;

    public Mes mesAtual(Context context){
        meses = new ArrayList<>();
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            meses = (ArrayList<Mes>) mesDao.queryForAll();
            for(int x = 0; x < meses.size(); x++){
                if(mes.isMesAtual() == true){
                    mes = meses.get(x);
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return mes;
    }

    public ArrayList<Mes> listarMeses(Context context){

        meses = new ArrayList<>();
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            meses = (ArrayList<Mes>) mesDao.queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return meses;
    }

    public ArrayList<Orcamento> listarOrcamentos(Context context, Mes m){

        orcamento = new Orcamento();
        orcamentos = new ArrayList<>();
        orcamentos1 = new ArrayList<>();
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            orcamentos = (ArrayList<Orcamento>) orcamentoDao.queryForAll();
            for(int y = 0; y < orcamentos.size(); y++){
                if(orcamento.getMes().getId().equals(m.getId())){
                    orcamentos1.add(orcamentos.get(y));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orcamentos1;
    }

    public ArrayList<Orcamento> listarOrcamentosComGastos(Context context, Mes m){
        orcamento = new Orcamento();
        orcamentos = new ArrayList<>();
        orcamentos1 = new ArrayList<>();
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            orcamentos = (ArrayList<Orcamento>) orcamentoDao.queryForAll();
            for(int y = 0; y < orcamentos.size(); y++){
                if(orcamento.getMes().getId().equals(m.getId()) && orcamento.isGastoMultiplo() == true){
                    orcamentos1.add(orcamentos.get(y));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orcamentos1;
    }

    public ArrayList<Gasto> listarGastos(Context context, Orcamento o){
        gastos = new ArrayList<>();
        gastos1 = new ArrayList<>();
        try {
            gastoDao = MyORMLiteHelper.getInstance(context).getGastoDao();
            gastos = (ArrayList<Gasto>) gastoDao.queryForAll();
            for(int z = 0; z < gastos.size(); z++){
                if(gasto.getOrcamento().getId().equals(o.getId())){
                    gastos1.add(gastos.get(z));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gastos1;
    }

    public boolean verificarMesAtual(Mes mesBanco, String dataAtual){
        String date[] = dataAtual.split("/");
        String mes = date[1];
        String ano = date[2];
        Integer numeroMes = Integer.valueOf(mes);
        String nomeMes = retornarNomeMes(numeroMes);
        if(mesBanco.getNome().equals(nomeMes) && mesBanco.getAno().equals(ano)){
            return true;
        }else {
            return false;
        }
    }

    private String retornarNomeMes(Integer mes){
        if (mes == 1) {
            nome = "JANEIRO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 2){
            nome = "FEVEREIRO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 3){
            nome = "MARÇO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 4){
            nome = "ABRIL";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 5){
            nome = "MAIO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 6){
            nome = "JUNHO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 7){
            nome = "JULHO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 8){
            nome = "AGOSTO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 9){
            nome = "SETEMBRO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 10){
            nome = "OUTUBRO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 11){
            nome = "NOVEMBRO";//TODO adicionar os nomes do arquivo de String
        }else if(mes == 12){
            nome = "DEZEMBRO";//TODO adicionar os nomes do arquivo de String
        }
        return nome;
    }

}