package senac.com.br.controledegastos.util;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.types.StringBytesType;

import java.sql.SQLException;
import java.util.ArrayList;
import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;
import senac.com.br.controledegastos.R;

// Created by Carlos Lohmeyer on 19/10/2017.

public class RetornoDao{

    private Dao<Mes, Integer> mesDao;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Dao<Gasto, Integer> gastoDao;
    private Mes mes, mesTeste1, mesTeste2, mesRetorno;
    private String nome;
    private ArrayList<Mes> meses;
    private ArrayList<Orcamento> orcamentos, orcamentos1;
    private ArrayList<Gasto> gastos, gastos1;
    private Float gastoTotal, saldoMes, saldoOrcamento, rendaMes, cartaoMes, cartaoOrcamento, valorOrcamentos, diferenca;
    private boolean condicional;

    //RETORNA APENAS O MÊS ATUAL
    public void instanciarMes(Context context, String data) throws SQLException {
        iniciar();
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        meses = (ArrayList<Mes>) mesDao.queryForAll();
        if (meses.isEmpty()) {
            inserirPrimeiroMes(context, data);
        }else {
                return;
        }
    }

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

    //RETORNA O VALOR TOTAL DE GASTOS NO CARTÃO DE CRÉDITO DE UM ORÇAMENTO, SE HOUVER SENÃO RETORNA ZERO
    public Float retornarOrcamentoCredito(Context context, Orcamento orcamento){
        iniciar();
        cartaoOrcamento = Float.valueOf("0");
        gastos = retornaListaDeGastos(context, orcamento);
        for(int x = 0; x < gastos.size(); x++){
            if(gastos.get(x).getFormadePagamento().equals(context.getString(R.string.credito))){
                cartaoOrcamento = cartaoOrcamento + gastos.get(x).getValor();
            }
        }
        return cartaoOrcamento;
    }

    //RETORNA O VALOR TOTAL DE GASTOS DE UM ORÇAMENTO SEM SER CREDITO, SE HOUVER SENÃO RETORNA ZERO
    public Float retornarOrcamentoValorGastos(Context context, Orcamento orcamento){
        iniciar();
        cartaoOrcamento = Float.valueOf("0");
        gastos = retornaListaDeGastos(context, orcamento);
        for(int x = 0; x < gastos.size(); x++){
            if(!gastos.get(x).getFormadePagamento().equals(context.getString(R.string.credito))){
                cartaoOrcamento = cartaoOrcamento + gastos.get(x).getValor();
            }
        }
        return cartaoOrcamento;
    }

    //RETORNA UM BOOLEANO PARA DIZER SE HA OU NAO UM GASTO PAGO NO CREDITO
    public boolean retornarIsCredito(Context context, Orcamento orcamento){
        iniciar();
        condicional = false;
        gastos = retornaListaDeGastos(context, orcamento);
        for(int x = 0; x < gastos.size(); x++){
            if(gastos.get(x).getFormadePagamento().equals(context.getString(R.string.credito))){
                condicional = true;
            }
        }
        return condicional;
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
        mesRetorno = new Mes();
    }

    //MÉTODOS ABAIXO PARA INSERIR DADOS DE MES NO BANCO
    public void inserirPrimeiroMes(Context context, String data) throws SQLException {
        iniciar();
        String date[] = data.split("/");
        String mes = date[1];
        String ano = date[2];
        Integer numeroMes = Integer.valueOf(mes);
        String nomeMes = retornarNomeMes(numeroMes, context);
        if(meses.isEmpty()){
            mesTeste1.setNome(nomeMes);
            mesTeste1.setAno(ano);
            mesTeste1.setMesAtual(true);
            mesTeste1.setCartaoMesAnterior(null);
            mesTeste1.setCartaoMesAtual(null);
            mesTeste1.setRenda(null);
            mesTeste1.setSaldoMensal(null);
            mesTeste1.setNotificar(null);
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mesTeste1);
            if(res.isCreated()){
                System.out.println("OBJETO mesTeste1 CRIADO COM SUCESSO!!!" + "\n");
            }else if(res.isUpdated()){
                System.out.println("OBJETO mesTeste1 ATUALIZADO COM SUCESSO!!!" + "\n");
            }else{
                System.out.println("OBJETO mesTeste1 ERRO INESPERADO!!!" + "\n");
            }
        }
    }

    public void fecharMes(Context context, Mes m) throws SQLException {
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

    //MARCA O MES QUE TERMINOU COM FALSE NA VARIAVEL MESATUAL E INICIA UM MES NOVO E UNICO
    public void iniciarNovoMes(Context context, String data) throws SQLException {
        iniciar();
        mesRetorno = retornaMesUnico(context,data);
        mes = retornaMesAtual(context);
        fecharMes(context, mes);
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        if(mesRetorno == null){
            String date[] = data.split("/");
            String mesData = date[1];
            String ano = date[2];
            Integer numeroMes = Integer.valueOf(mesData);
            String nomeMes = retornarNomeMes(numeroMes, context);
            mesTeste2.setNome(nomeMes);
            mesTeste2.setAno(ano);
            mesTeste2.setMesAtual(true);
            mesTeste2.setCartaoMesAnterior(mes.getCartaoMesAtual());
            mesTeste2.setCartaoMesAtual(null);
            mesTeste2.setSaldoMensal(null);
            mesTeste2.setRenda(mes.getRenda());
            mesTeste2.setNotificar(mes.getNotificar());
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mesTeste2);
            if(res.isCreated()){
                System.out.println("OBJETO mesTeste2 CRIADO COM SUCESSO!!!" + "\n");
            }else if(res.isUpdated()){
                System.out.println("OBJETO mesTeste2 ERRO NA BAGAÇA!!!" + "\n");
            }else{
                System.out.println("OBJETO mesTeste2 ERRO INESPERADO!!!" + "\n");
            }
        }else if(mesRetorno != null){
            mesRetorno.setMesAtual(true);
            Dao.CreateOrUpdateStatus res1 = mesDao.createOrUpdate(mesRetorno);
            if(res1.isCreated()){
                System.out.println("OBJETO mesRetorno ERRO NA BAGAÇA!!!" + "\n");
            }else if(res1.isUpdated()){
                System.out.println("OBJETO mesRetorno ATUALIZADO COM SUCESSO!!!" + "\n");
            }else{
                System.out.println("OBJETO mesRetorno ERRO INESPERADO!!!" + "\n");
            }
        }

    }

    //TODO AQUI A MAGICA COMECA PARA EVITAR DOIS MESES COM MESMO NOME
    public Mes retornaMesUnico(Context context, String data){
        iniciar();
        meses = retornaListaDeMeses(context);
        mes = retornaMesAtual(context);
        String date[] = data.split("/");
        String mesData = date[1];
        String ano = date[2];
        Integer numeroMes = Integer.valueOf(mesData);
        String nomeMes = retornarNomeMes(numeroMes, context);
        condicional = false;
        for(int zz = 0; zz < meses.size(); zz++){
            if(meses.get(zz).getNome().equals(nomeMes) && meses.get(zz).getAno().equals(ano)){
                condicional = true;
                mesRetorno = meses.get(zz);
            }
        }
        if(condicional == false){
            mesRetorno = null;
        }
        return mesRetorno;
    }

    //ATUALIZA O SALDO MENSAL QUANDO UM ORÇAMENTO FOR ADICIONADO
    public void atualizaSaldoMensal(Context context, Float valor) throws SQLException {
        iniciar();
        mes = retornaMesAtual(context);
        saldoMes = mes.getSaldoMensal();
        saldoMes = saldoMes - valor;
        mes.setSaldoMensal(saldoMes);
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
        if(res.isCreated()){
            System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
        }else if(res.isUpdated()){
            System.out.println("ORÇAMENTO ATUALIZADO COM SUCESSO");
            System.out.println("MES ATUALIZADO COM SUCESSO");
        }else{
            System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
        }
    }

    //ATUALIZA O SALDO MENSAL QUANDO UM ORÇAMENTO FOR ATUALIZADO O ANTIGO ERA CREDITO O NOVO NÃO
    public void atualizaSaldoMensalSemCartaoOrc(Context context, Float valor, Float valorCartao) throws SQLException {
        iniciar();
        mes = retornaMesAtual(context);
        saldoMes = mes.getSaldoMensal();
        System.out.println("SALDO DO MES ANTES DA ATUALAIZACAO " + saldoMes);
        cartaoMes = mes.getCartaoMesAtual();
        System.out.println("CARTAO ATUAL ANTES DA ATUALAIZACAO " + cartaoMes);
        saldoMes = saldoMes - valor;
        System.out.println("SALDO DO MES depois DA ATUALAIZACAO " + saldoMes);
        cartaoMes = cartaoMes - valorCartao;
        System.out.println("CARTAO ATUAL DEPOIS DA ATUALAIZACAO " + cartaoMes);
        mes.setSaldoMensal(saldoMes);
        mes.setCartaoMesAtual(cartaoMes);
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
        if(res.isCreated()){
            System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
        }else if(res.isUpdated()){
            System.out.println("ORÇAMENTO CRIADO COM SUCESSO");
            System.out.println("MES ATUALIZADO COM SUCESSO");
        }else{
            System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
        }
    }

    //ATUALIZA O CARTÃO ATUAL QUANDO UM ORÇAMENTO FOR ATUALIZADO O ANTIGO NÃO ERA CREDITO O NOVO SIM
    public void atualizaSaldoMensalComCartao(Context context, Float valor, Float valorCartao) throws SQLException {
        iniciar();
        mes = retornaMesAtual(context);
        saldoMes = mes.getSaldoMensal();
        cartaoMes = mes.getCartaoMesAtual();
        if (cartaoMes == null){
            cartaoMes = Float.valueOf("0");
        }
        saldoMes = saldoMes + valor;
        cartaoMes = cartaoMes + valorCartao;
        mes.setSaldoMensal(saldoMes);
        mes.setCartaoMesAtual(cartaoMes);
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
        if(res.isCreated()){
            System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
        }else if(res.isUpdated()){
            System.out.println("ORÇAMENTO ATUALIZADO COM SUCESSO");
            System.out.println("MES ATUALIZADO COM SUCESSO");
        }else{
            System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
        }
    }

    //ATUALIZA O SALDO MENSAL QUANDO UM ORÇAMENTO FOR ATUALIZADO O ANTIGO ERA OUTROS O NOVO CREDITO
    public void atualizaCartaoComSaldoMensalOrc(Context context, Float valorCartao, Float valor) throws SQLException {
        iniciar();
        mes = retornaMesAtual(context);
        saldoMes = mes.getSaldoMensal();
        System.out.println("SALDO DO MES ANTES DA ATUALAIZACAO " + saldoMes);
        cartaoMes = mes.getCartaoMesAtual();
        System.out.println("CARTAO ATUAL ANTES DA ATUALAIZACAO " + cartaoMes);
        saldoMes = saldoMes + valor;
        System.out.println("SALDO DO MES DEPOIS DA ATUALAIZACAO " + saldoMes);
        cartaoMes = cartaoMes + valorCartao;
        System.out.println("CARTAO ATUAL DEPOIS DA ATUALAIZACAO " + cartaoMes);
        mes.setSaldoMensal(saldoMes);
        mes.setCartaoMesAtual(cartaoMes);
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
        if(res.isCreated()){
            System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
        }else if(res.isUpdated()){
            System.out.println("ORÇAMENTO ATUALIZADO COM SUCESSO");
            System.out.println("MES ATUALIZADO COM SUCESSO");
        }else{
            System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
        }
    }

    //ATUALIZA O SALDO MENSAL QUANDO UM ORÇAMENTO FOR ATUALIZADO
    public void atualizaSaldoMensalOrc(Context context, Float valorNovo, Float valorAntigo) throws SQLException {
        iniciar();
        mes = retornaMesAtual(context);
        saldoMes = mes.getSaldoMensal();
        if(valorAntigo < valorNovo){
            diferenca = valorNovo - valorAntigo;
            saldoMes = saldoMes - diferenca;
        }else if(valorAntigo > valorNovo){
            diferenca = valorAntigo - valorNovo;
            saldoMes = saldoMes + diferenca;
        }else if(valorAntigo == valorNovo){
            saldoMes = saldoMes + Float.valueOf("0");
        }
        mes.setSaldoMensal(saldoMes);
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
        if(res.isCreated()){
            System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
        }else if(res.isUpdated()){
            System.out.println("ORÇAMENTO CRIADO COM SUCESSO");
            System.out.println("MES ATUALIZADO COM SUCESSO");
        }else{
            System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
        }
    }

    //INSERI VALORES NO CARTÃO DE CRÉDITO ATUAL
    public void adicionaCartaoAtual(Context context, Float valor) throws SQLException {
        iniciar();
        mes = retornaMesAtual(context);
        cartaoMes = mes.getCartaoMesAtual();
        if(cartaoMes == null){
            cartaoMes = Float.valueOf("0");
        }
        cartaoMes = cartaoMes + valor;
        mes.setCartaoMesAtual(cartaoMes);
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
        if(res.isCreated()){
            System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
        }else if(res.isUpdated()){
            System.out.println("MES ATUALIZADO COM SUCESSO");
        }else{
            System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
        }
    }

    //EDITA O VALOR ATUAL DO CARTÃO DE CRÉDITO QUANDO O ORÇAMENTO FOR EDITADO
    public void editaCartaoAtualOrc(Context context, Float valorAntigo, Float valorNovo) throws SQLException {
        iniciar();
        mes = retornaMesAtual(context);
        cartaoMes = mes.getCartaoMesAtual();
        if(valorAntigo < valorNovo){
            diferenca = valorNovo - valorAntigo;
            cartaoMes = cartaoMes + diferenca;
        }else if(valorAntigo > valorNovo){
            diferenca = valorAntigo - valorNovo;
            cartaoMes = cartaoMes - diferenca;
        }else if(valorAntigo == valorNovo){
            cartaoMes = cartaoMes + Float.valueOf("0");
        }
        mes.setCartaoMesAtual(cartaoMes);
        mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
        Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
        if(res.isCreated()){
            System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
        }else if(res.isUpdated()){
            System.out.println("RENDA DO MES ATUALIZADA COM SUCESSO");
        }else{
            System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
        }
    }

    //RETORNA O SALDO MENSAL QUANDO A RENDA FOR EDITADA
    public Float retornaSaldoMensal(Context context, Float valor) throws SQLException {
        iniciar();
        Float diferencaMaior, diferencamenor;
        mes = retornaMesAtual(context);
        rendaMes = mes.getRenda();
        saldoMes = mes.getSaldoMensal();
        if(rendaMes < valor){
            diferencaMaior = valor - rendaMes;
            saldoMes = saldoMes + diferencaMaior;
        }else if(rendaMes > valor){
            diferencamenor = rendaMes - valor;
            saldoMes = saldoMes - diferencamenor;
        }else if(rendaMes == valor){
            saldoMes = saldoMes + Float.valueOf("0");
        }
        return saldoMes;
    }

    //RETORNA O SALDO DE UM ORCAMENTO QUANDO VAI DELETAR UM GASTO
    public Float retornaSaldoOrcamento( Context context, Integer id){
        iniciar();
        orcamentos = retornaListaDeOrcamentosAtuais(context);
        for(int x = 0; x < meses.size(); x++){
            if(orcamentos.get(x).getId().equals(id)){
                diferenca = orcamentos.get(x).getSaldo();
            }
        }
        return diferenca;
    }

    //RETORNA UM ORCAMENTO A PARTIR DE UM GASTO
    public Orcamento retornaOrcamento( Context context, Integer id){
        iniciar();
        Orcamento orc = new Orcamento();
        orcamentos = retornaListaDeOrcamentosAtuais(context);
        for(int x = 0; x < orcamentos.size(); x++){
            if(orcamentos.get(x).getId().equals(id)){
                orc = orcamentos.get(x);
            }
        }
        return orc;
    }

    //NA EDIÇÃO DO GASTO A FORMA ANTIGA ERA CREDITO E A NOVA CREDITO - VALOR A MAIS
    public void attGastoCredMais(Context context, Float valor){
        mes = retornaMesAtual(context);
        cartaoMes = mes.getCartaoMesAtual();
        cartaoMes = cartaoMes + valor;
        mes.setCartaoMesAtual(cartaoMes);
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
            if(res.isCreated()){
                System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
            }else if(res.isUpdated()){
                System.out.println("RENDA DO MES ATUALIZADA COM SUCESSO");
            }else{
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //NA EDIÇÃO DO GASTO A FORMA ANTIGA ERA CREDITO E A NOVA CREDITO - VALOR A MENOS
    public void attGastoCredMenos(Context context, Float valor){
        mes = retornaMesAtual(context);
        cartaoMes = mes.getCartaoMesAtual();
        cartaoMes = cartaoMes - valor;
        mes.setCartaoMesAtual(cartaoMes);
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
            if(res.isCreated()){
                System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
            }else if(res.isUpdated()){
                System.out.println("RENDA DO MES ATUALIZADA COM SUCESSO");
            }else{
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //NA EDIÇÃO DO GASTO A FORMA ANTIGA ERA CREDITO E A NOVA OUTROS
    public void attGastoCredParaSaldo(Context context, Float valor, Float valorCartao, Orcamento orcamento){
        mes = retornaMesAtual(context);
        cartaoMes = mes.getCartaoMesAtual();
        cartaoMes = cartaoMes - valorCartao;
        saldoOrcamento = orcamento.getSaldo();
        saldoOrcamento = saldoOrcamento - valor;
        orcamento.setSaldo(saldoOrcamento);
        mes.setCartaoMesAtual(cartaoMes);
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
            if(res.isCreated()){
                System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
            }else if(res.isUpdated()){
                System.out.println("RENDA DO MES ATUALIZADA COM SUCESSO");
            }else{
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            Dao.CreateOrUpdateStatus resOrc = orcamentoDao.createOrUpdate(orcamento);
            if(resOrc.isCreated()){
                System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
            }else if(resOrc.isUpdated()){
                System.out.println("RENDA DO MES ATUALIZADA COM SUCESSO");
            }else{
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //NA EDIÇÃO DO GASTO A FORMA ANTIGA ERA OUTROS E A NOVA OUTROS E O VALOR DO GASTO É MAIOR
    public void attGastoOutrosdMais(Context context, Float valor, Orcamento orcamento){
        mes = retornaMesAtual(context);
        saldoOrcamento = orcamento.getSaldo();
        saldoOrcamento = saldoOrcamento - valor;
        orcamento.setSaldo(saldoOrcamento);
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            Dao.CreateOrUpdateStatus resOrc = orcamentoDao.createOrUpdate(orcamento);
            if(resOrc.isCreated()){
                System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
            }else if(resOrc.isUpdated()){
                System.out.println("RENDA DO MES ATUALIZADA COM SUCESSO");
            }else{
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //NA EDIÇÃO DO GASTO A FORMA ANTIGA ERA OUTROS E A NOVA OUTROS E O VALOR DO GASTO É MENOR
    public void attGastoOutrosdMenos(Context context, Float valor, Orcamento orcamento){
        mes = retornaMesAtual(context);
        saldoOrcamento = orcamento.getSaldo();
        saldoOrcamento = saldoOrcamento + valor;
        diferenca = orcamento.getSaldo() * -1;
        if(orcamento.getSaldo() < 0 && saldoOrcamento >= 0){
            atualizaSaldoMesOrcamentoPositivo(context, diferenca);
        }else if(orcamento.getSaldo() < 0 && saldoOrcamento < 0 && orcamento.getSaldo() < saldoOrcamento){
            Float x = saldoOrcamento - orcamento.getSaldo();
            atualizaSaldoMesOrcamentoPositivo(context, x);
        }
        orcamento.setSaldo(saldoOrcamento);
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            Dao.CreateOrUpdateStatus resOrc = orcamentoDao.createOrUpdate(orcamento);
            if(resOrc.isCreated()){
                System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
            }else if(resOrc.isUpdated()){
                System.out.println("RENDA DO MES ATUALIZADA COM SUCESSO");
            }else{
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //NA EDIÇÃO DO GASTO A FORMA ANTIGA ERA OUTROS A NOVA CREDITO
    public void attGastoSaldoParaCred(Context context, Float valor, Float valorCartao, Orcamento orcamento){
        mes = retornaMesAtual(context);
        cartaoMes = mes.getCartaoMesAtual();
        cartaoMes = cartaoMes + valorCartao;
        saldoMes = mes.getSaldoMensal();
        saldoOrcamento = orcamento.getSaldo();
        saldoOrcamento = saldoOrcamento + valor;
        saldoMes = saldoMes - diferenca;
        orcamento.setSaldo(saldoOrcamento);
        mes.setCartaoMesAtual(cartaoMes);
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
            if(res.isCreated()){
                System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
            }else if(res.isUpdated()){
                System.out.println("RENDA DO MES ATUALIZADA COM SUCESSO");
            }else{
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(context).getOrcamentoDao();
            Dao.CreateOrUpdateStatus resOrc = orcamentoDao.createOrUpdate(orcamento);
            if(resOrc.isCreated()){
                System.out.println("ALGO ESTA ESTRANHO NESSA BAGACA");
            }else if(resOrc.isUpdated()){
                System.out.println("RENDA DO MES ATUALIZADA COM SUCESSO");
            }else{
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ATUALIZA O SALDO DO MES QUANDO UM ORCAMENTO FOR ATUALIZADO
    public Float retornaEAtualizaTotalDeGastos(Context context){
        mes = retornaMesAtual(context);
        orcamentos = retornaListaDeOrcamentosAtuais(context);
        valorOrcamentos = Float.valueOf("0");
        for(int x = 0; x < orcamentos.size(); x++){
            if(!orcamentos.get(x).getFormadePagamento().equals(context.getResources().getString(R.string.credito))){
                if(orcamentos.get(x).getSaldo() < 0){
                    valorOrcamentos = valorOrcamentos + (orcamentos.get(x).getValorInicial() + (orcamentos.get(x).getSaldo() * -1));
                }else {
                    valorOrcamentos = valorOrcamentos + orcamentos.get(x).getValorInicial();
                }
            }
        }
        return valorOrcamentos;
    }

    //ATUALIZA O SALDO DO MES QUANDO UM ORCAMENTO FOR ATUALIZADO E O SALDO ANTIGO ERA POSITIVO E O NOVO FOR NEGATIVO
    public void atualizaSaldoMesOrcamentoNegativo(Context context, Float saldoAntigo, Float saldoNovo){
        mes = retornaMesAtual(context);
        Float diferencaDeSaldo, diferenca, mesSaldo;
        mesSaldo = mes.getSaldoMensal();
        if(saldoAntigo > 0){
            diferencaDeSaldo = saldoAntigo;
        }else {
            diferencaDeSaldo = Float.valueOf("0");
        }
        diferenca = (saldoAntigo - saldoNovo) - diferencaDeSaldo;
        mesSaldo = mesSaldo - diferenca;
        mes.setSaldoMensal(mesSaldo);
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
            if(res.isUpdated()){
                System.out.println("ATUALIZOU O SALDO DO MES COM SUCESSO");
            }else {
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ATUALIZA O SALDO DO MES QUANDO UM ORCAMENTO FOR ATUALIZADO E O SALDO ANTIGO ERA NEGATIVO E O NOVO FOR POSITIVO
    public void atualizaSaldoMesOrcamentoPositivo(Context context, Float valorNovo){
        mes = retornaMesAtual(context);
        Float mesSaldo;
        mesSaldo = mes.getSaldoMensal();
        mesSaldo = mesSaldo + valorNovo;
        mes.setSaldoMensal(mesSaldo);
        try {
            mesDao = MyORMLiteHelper.getInstance(context).getMesDao();
            Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
            if(res.isUpdated()){
                System.out.println("ATUALIZOU O SALDO DO MES COM SUCESSO");
            }else {
                System.out.println("ERRO AO ATUALIZAR O SALDO DO MES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //APAGA OS GASTOS DE UM ITEM DE ORCAMENTO
    public void apagarGastos(Context context, Orcamento orcamento){
        gastos = retornaListaDeGastos(context, orcamento);
        try {
            gastoDao = MyORMLiteHelper.getInstance(context).getGastoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int x = 0; x < gastos.size(); x++){
            try {
                gastoDao.delete(gastos.get(x));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}