package senac.com.br.controledegastos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.AdapterSpinnerOrcamento;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;
import senac.com.br.controledegastos.util.ActivityHelper;
import senac.com.br.controledegastos.util.RetornoDao;

import static java.lang.Float.parseFloat;

//Created by Carlos Lohmeyer.

public class NovoGastoActivity extends AppCompatActivity {

    private EditText nomeGasto, localGasto, valorGasto;
    private TextView capturaData;
    private Spinner formaPagto, orcamentoSpinner;
    private Mes mes;
    private Orcamento orcamento;
    private Gasto gasto;
    private ArrayList<Orcamento> orcamentos;
    private RetornoDao retornoDao;
    private Dao<Mes, Integer> mesDao;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Dao<Gasto, Integer> gastoDao;
    private AdapterSpinnerOrcamento adapterSpinner;
    private Float valor, valorCartao, valorSaldo, diferenca, valorSaldoMes, saldoOrcamento, diferencaParaZero;
    private String dataFormatada, formaAntiga;
    private Bundle params;
    private int indice, indiceForma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_gasto);
        iniciar();
        mes = retornoDao.retornaMesAtual(this);
        orcamentos = retornoDao.retornarListaDeOrcamentosComGastosAtuais(this);
        adapterSpinner = new AdapterSpinnerOrcamento(this, orcamentos);
        orcamentoSpinner.setAdapter(adapterSpinner);
        if(mes.getRenda() == null){
            Toast.makeText(this, R.string.msg_add_renda, Toast.LENGTH_SHORT).show();
            Intent it = new Intent(NovoGastoActivity.this, EditarRendaActivity.class);
            startActivity(it);
        }
        if(mes.getRenda() != null && orcamentos.isEmpty()){
            Toast.makeText(this,
                    R.string.msg_add_orcamento_multiplos, Toast.LENGTH_LONG).show();
            Intent it = new Intent(NovoGastoActivity.this, NovoItemActivity.class);
            startActivity(it);
        }
        try {
            mesDao = MyORMLiteHelper.getInstance(this).getMesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(this).getOrcamentoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            gastoDao = MyORMLiteHelper.getInstance(this).getGastoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dataFormatada = df.format(c.getTime());
        capturaData.setText(dataFormatada.toString());
        params = getIntent().getExtras();
        try{
            //Extrair dados enviados do parametro
            gasto = (Gasto) params.getSerializable("gasto");
            if(gasto.getId() != null){
                for(int x = 0; x < orcamentos.size(); x++){
                    if(orcamentos.get(x).getId().equals(gasto.getOrcamento().getId())){
                        indice = x;
                    }
                }
                if(gasto.getFormadePagamento().equals(getString(R.string.dinheiro))){
                    indiceForma = 1;
                }else if(gasto.getFormadePagamento().equals(getString(R.string.credito))){
                    indiceForma = 2;
                }else if(gasto.getFormadePagamento().equals(getString(R.string.debito))){
                    indiceForma = 3;
                }
                nomeGasto.setText(gasto.getNome());
                localGasto.setText(gasto.getLocal());
                valorGasto.setText(String.valueOf(gasto.getValor()));
                capturaData.setText(gasto.getDate());
                orcamentoSpinner.setSelection(indice);
                formaPagto.setSelection(indiceForma);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void iniciar(){
        nomeGasto = (EditText) findViewById(R.id.nomeGasto);
        localGasto = (EditText) findViewById(R.id.localGasto);
        valorGasto = (EditText) findViewById(R.id.valorGasto);
        capturaData = (TextView) findViewById(R.id.capturaData);
        formaPagto = (Spinner) findViewById(R.id.SpinnerFormaPagamento);
        orcamentoSpinner = (Spinner) findViewById(R.id.orcamentoSpinner);
        mes = new Mes();
        orcamento = new Orcamento();
        orcamentos = new ArrayList<Orcamento>();
        retornoDao = new RetornoDao();
        params = new Bundle();
    }

    public void salvarGasto(View view){

        if(mes.getRenda() == null){
            Toast.makeText(this, R.string.msg_add_renda, Toast.LENGTH_LONG).show();
            Intent it = new Intent(NovoGastoActivity.this, EditarRendaActivity.class);
            startActivity(it);
        }else {
            if(orcamentos.isEmpty()){
                Toast.makeText(this, R.string.msg_add_orcamento_multiplos, Toast.LENGTH_LONG).show();
                Intent it = new Intent(NovoGastoActivity.this, NovoItemActivity.class);
                startActivity(it);
            }
        }
        // santa validacao Batman
        String nome = nomeGasto.getText().toString().toUpperCase();
        String local = localGasto.getText().toString().toUpperCase();
        String forma = formaPagto.getSelectedItem().toString();
        if(valorGasto.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.toast_gasto_valor, Toast.LENGTH_LONG).show();
            return;
        }
        valor = Float.valueOf(valorGasto.getText().toString());

        // reunindo todas as strings em uma lista para deixar a validação mais elegante
        List<String> controleDeCampos = new ArrayList<String>();
        controleDeCampos.add(local);
        // checando campos vazios
        for (int i = 0; i < controleDeCampos.size();i++){
            Object item = controleDeCampos.get(i);
            if(item == null || item == " "){
                Toast.makeText(this, R.string.toast_gasto_campos, Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(forma.equals(getString(R.string.forma))){
            Toast.makeText(NovoGastoActivity.this, R.string.toast_pagamento, Toast.LENGTH_LONG).show();
            return;
        }
        // testando o campo 'valor'
        if(valor == null || valor <= 0){
            Toast.makeText(this, R.string.toast_gasto_valor, Toast.LENGTH_LONG).show();
            return;
        }
        orcamento = (Orcamento) orcamentoSpinner.getSelectedItem();
        if(gasto == null){
            gasto = new Gasto();
            gasto.setNome(nome);
            gasto.setFormadePagamento(forma);
            gasto.setLocal(local);
            gasto.setValor(valor);
            gasto.setDate(dataFormatada);
            gasto.setOrcamento(orcamento);
            if(forma.equals(getString(R.string.credito))){
                valorCartao = mes.getCartaoMesAtual();
                if(valorCartao == null){
                    valorCartao = Float.valueOf("0");
                }
                valorCartao = valorCartao + valor;
                mes.setCartaoMesAtual(valorCartao);
                try {
                    mesDao = MyORMLiteHelper.getInstance(this).getMesDao();
                    Dao.CreateOrUpdateStatus resMes = mesDao.createOrUpdate(mes);
                    if(resMes.isCreated()){
                        Toast.makeText(NovoGastoActivity.this, R.string.toast_salvo, Toast.LENGTH_LONG).show();
                    }else if(resMes.isUpdated()){
                        Toast.makeText(NovoGastoActivity.this, R.string.toast_atualizado, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(NovoGastoActivity.this, R.string.toast_erro, Toast.LENGTH_LONG).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else if(!forma.equals(getString(R.string.credito))){
                valorSaldoMes = mes.getSaldoMensal();
                saldoOrcamento = orcamento.getSaldo();
                if(saldoOrcamento > 0){
                    diferencaParaZero = saldoOrcamento;
                }else {
                    diferencaParaZero = Float.valueOf("0");
                }
                valorSaldo = orcamento.getSaldo();
                valorSaldo = valorSaldo - valor;
                if(valorSaldo < 0){
                    diferenca = (saldoOrcamento - valorSaldo) - diferencaParaZero;
                    valorSaldoMes = valorSaldoMes - diferenca;
                    mes.setSaldoMensal(valorSaldoMes);
                    try {
                        mesDao = MyORMLiteHelper.getInstance(this).getMesDao();
                        Dao.CreateOrUpdateStatus resMes1 = mesDao.createOrUpdate(mes);
                        if(resMes1.isCreated()){
                            Toast.makeText(NovoGastoActivity.this, R.string.toast_salvo, Toast.LENGTH_LONG).show();
                        }else if(resMes1.isUpdated()){
                            Toast.makeText(NovoGastoActivity.this, R.string.toast_atualizado, Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(NovoGastoActivity.this, R.string.toast_erro, Toast.LENGTH_LONG).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                orcamento.setSaldo(valorSaldo);
                try {
                    orcamentoDao = MyORMLiteHelper.getInstance(this).getOrcamentoDao();
                    Dao.CreateOrUpdateStatus resOrc = orcamentoDao.createOrUpdate(orcamento);
                    if(resOrc.isCreated()){
                        Toast.makeText(NovoGastoActivity.this, R.string.toast_salvo, Toast.LENGTH_LONG).show();
                    }else if(resOrc.isUpdated()){
                        Toast.makeText(NovoGastoActivity.this, R.string.toast_atualizado, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(NovoGastoActivity.this, R.string.toast_erro, Toast.LENGTH_LONG).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                Dao.CreateOrUpdateStatus res = gastoDao.createOrUpdate(gasto);
                if(res.isCreated()){
                    Toast.makeText(NovoGastoActivity.this, R.string.toast_salvo, Toast.LENGTH_LONG).show();
                }else if(res.isUpdated()){
                    Toast.makeText(NovoGastoActivity.this, R.string.toast_atualizado, Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(NovoGastoActivity.this, R.string.toast_erro, Toast.LENGTH_LONG).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(gasto != null){
            formaAntiga = gasto.getFormadePagamento();
            if(formaAntiga.equals(getString(R.string.credito))){
                if(forma.equals(getString(R.string.credito))){
                    if(gasto.getValor() < valor){
                        diferenca = valor - gasto.getValor();
                        retornoDao.attGastoCredMais(this, diferenca);
                    }else if(gasto.getValor() > valor){
                        diferenca = gasto.getValor() - valor;
                        retornoDao.attGastoCredMenos(this, diferenca);
                    }
                }else {
                    retornoDao.attGastoCredParaSaldo(this, valor, gasto.getValor(), orcamento);
                }
            }else {
                if(forma.equals(getString(R.string.credito))){

                }else {
                    if(gasto.getValor() < valor){
                        diferenca = valor - gasto.getValor();
                        retornoDao.attGastoOutrosdMais(this, diferenca, orcamento);
                    }else if(gasto.getValor() > valor){
                        diferenca = gasto.getValor() - valor;
                        retornoDao.attGastoOutrosdMenos(this, diferenca, orcamento);
                    }
                }
            }
            gasto.setNome(nome);
            gasto.setFormadePagamento(forma);
            gasto.setLocal(local);
            gasto.setValor(valor);
            gasto.setOrcamento((Orcamento) orcamentoSpinner.getSelectedItem());
            try {
                Dao.CreateOrUpdateStatus resGasto = gastoDao.createOrUpdate(gasto);
                if(resGasto.isCreated()){
                    Toast.makeText(NovoGastoActivity.this, R.string.toast_salvo, Toast.LENGTH_LONG).show();
                }else if(resGasto.isUpdated()){
                    Toast.makeText(NovoGastoActivity.this, R.string.toast_atualizado, Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(NovoGastoActivity.this, R.string.toast_erro, Toast.LENGTH_LONG).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        gasto = new Gasto();
        nomeGasto.setText("");
        localGasto.setText("");
        valorGasto.setText("");
        voltar(view);
    }

    public void voltar(View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}