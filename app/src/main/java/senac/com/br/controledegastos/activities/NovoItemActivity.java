package senac.com.br.controledegastos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;
import senac.com.br.controledegastos.util.RetornoDao;

//Created by Carlos Lohmeyer.

public class NovoItemActivity extends AppCompatActivity {

    private RetornoDao retornoDao;
    private Orcamento orcamento;
    private EditText editNomeItem, editValorItem;
    private LinearLayout linear_ajuda, linearPagamento;
    private boolean visao = true;
    private Mes mes;
    private Float valor, valorAntigo, diferencaSaldoOrc, saldoOrcamento;
    private CheckBox cbMultiplas;
    private String value, forma, formaAntiga;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Spinner pagamentoItem;
    private Bundle params;
    private int indiceForma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item);
        resgatarViews();
        //Resgatar dados da activity chamadora
        params = getIntent().getExtras();
        try{
            //Extrair dados enviados do parametro
            orcamento = (Orcamento) params.getSerializable("orcamento");
            if(orcamento.getId() != null){
                editNomeItem.setText(orcamento.getNome());
                editValorItem.setText(String.valueOf(orcamento.getValorInicial()));
                if(orcamento.isGastoMultiplo() == true){
                    cbMultiplas.setChecked(true);
                    cbMultiplasHideIntent();
                    cbMultiplas.setClickable(false);
                }
                if(orcamento.isGastoMultiplo() == false){
                    if(orcamento.getFormadePagamento().equals(getString(R.string.dinheiro))){
                        indiceForma = 1;
                    }else if(orcamento.getFormadePagamento().equals(getString(R.string.credito))){
                        indiceForma = 2;
                    }else if(orcamento.getFormadePagamento().equals(getString(R.string.debito))){
                        indiceForma = 3;
                    }
                }
                pagamentoItem.setSelection(indiceForma);
                cbMultiplas.setClickable(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mes = retornoDao.retornaMesAtual(this);
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(this).getOrcamentoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(mes.getRenda() == null){
            Toast.makeText(this, R.string.msg_add_renda, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, EditarRendaActivity.class);
            startActivity(i);
        }
    }

    public void resgatarViews(){
        retornoDao = new RetornoDao();
        editNomeItem = (EditText)findViewById(R.id.nomeItem);
        editValorItem = (EditText)findViewById(R.id.valorItem);
        linear_ajuda = (LinearLayout)findViewById(R.id.linear_ajuda);
        linearPagamento = (LinearLayout)findViewById(R.id.linearPagamento);
        mes = new Mes();
        cbMultiplas = (CheckBox)findViewById(R.id.cbMultiplas);
        pagamentoItem = (Spinner) findViewById(R.id.spinnerPagamentoItem);
        params = new Bundle();
    }

    public void mostrar_esconder_ajuda(View view){
        if (visao == true){
            mostrar(view);
        } else {
            esconder(view);
        }
    }

    public void esconder(View view){
        linear_ajuda.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        visao = true;
    }

    public void mostrar(View view){
        linear_ajuda.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        visao = false;
    }

    public void cbMultiplasHide(View view){
        if(cbMultiplas.isChecked()){
            linearPagamento.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else {
            linearPagamento.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public void cbMultiplasHideIntent(){
        if(cbMultiplas.isChecked()){
            linearPagamento.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else {
            linearPagamento.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public void voltar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void salvarItem(View view) {

        if(mes.getRenda() == null){
            Toast.makeText(this, R.string.msg_add_renda, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, EditarRendaActivity.class);
            startActivity(i);
        }
        if(editNomeItem.getText().toString().isEmpty()){
            Toast.makeText(NovoItemActivity.this, R.string.toast_nomeItem, Toast.LENGTH_LONG).show();
            editNomeItem.requestFocus();
            return;
        }
        if(orcamento == null){
            orcamento = new Orcamento();

            value = editValorItem.getText().toString();
            forma = pagamentoItem.getSelectedItem().toString();
            System.out.println("FORMA DE PAGAMENTO DESSA MERDA " + forma);
            if(cbMultiplas.isChecked()){
                if(value.isEmpty()){
                    value = "0.00";
                }
            }else if(!cbMultiplas.isChecked()){
                if(forma.equals(getString(R.string.forma))){
                    Toast.makeText(NovoItemActivity.this, R.string.toast_pagamento, Toast.LENGTH_LONG).show();
                    pagamentoItem.requestFocus();
                    return;
                }
                if(value.isEmpty()){
                    Toast.makeText(NovoItemActivity.this, R.string.toast_valorItem, Toast.LENGTH_LONG).show();
                    editValorItem.requestFocus();
                    return;
                }
            }
            valor = Float.valueOf(value);
            orcamento.setNome(editNomeItem.getText().toString().toUpperCase());
            orcamento.setSaldo(valor);
            orcamento.setFormadePagamento(forma);
            orcamento.setValorInicial(valor);
            if(cbMultiplas.isChecked()){
                orcamento.setGastoMultiplo(true);
            }else {
                orcamento.setGastoMultiplo(false);
            }
            if(cbMultiplas.isChecked()){
                try {
                    retornoDao.atualizaSaldoMensal(this, valor);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else if(!cbMultiplas.isChecked()){
                if(forma.equals(getString(R.string.credito))){
                    try {
                        retornoDao.adicionaCartaoAtual(this, valor);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        retornoDao.atualizaSaldoMensal(this, valor);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            orcamento.setMes(mes);
            try {
                Dao.CreateOrUpdateStatus  res = orcamentoDao.createOrUpdate(orcamento);
                if(res.isCreated()){
                    Toast.makeText(NovoItemActivity.this, R.string.toast_salvo, Toast.LENGTH_LONG).show();
                }else if(res.isUpdated()){
                    Toast.makeText(NovoItemActivity.this, R.string.toast_atualizado, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(NovoItemActivity.this, R.string.toast_erro, Toast.LENGTH_LONG).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(orcamento != null){
            value = editValorItem.getText().toString();
            forma = pagamentoItem.getSelectedItem().toString();
            if(orcamento.isGastoMultiplo() == true){
                if(value.isEmpty()){
                    value = "0.00";
                }
            }else {
                if(forma.equals(getString(R.string.forma))){
                    Toast.makeText(NovoItemActivity.this, R.string.toast_pagamento, Toast.LENGTH_LONG).show();
                    pagamentoItem.requestFocus();
                    return;
                }
                if(value.isEmpty()){
                    Toast.makeText(NovoItemActivity.this, R.string.toast_valorItem, Toast.LENGTH_LONG).show();
                    editValorItem.requestFocus();
                    return;
                }
            }
            valor = Float.valueOf(value);
            saldoOrcamento = orcamento.getSaldo();
            valorAntigo = orcamento.getValorInicial();
            formaAntiga = orcamento.getFormadePagamento();
            orcamento.setNome(editNomeItem.getText().toString().toUpperCase());

            if(orcamento.isGastoMultiplo() == false){
                if(formaAntiga.equals(getString(R.string.credito))){
                    if(forma.equals(getString(R.string.credito))){
                        try {
                            retornoDao.editaCartaoAtualOrc(this, valorAntigo, valor);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else if(!forma.equals(getString(R.string.credito))){
                        try {
                            retornoDao.atualizaSaldoMensalSemCartaoOrc(this, valor, valorAntigo);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }else {
                    if(forma.equals(getString(R.string.credito))){
                        try {
                            retornoDao.atualizaCartaoComSaldoMensalOrc(this, valor, valorAntigo);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else if(!forma.equals(getString(R.string.credito))){
                        try {
                            retornoDao.atualizaSaldoMensalOrc(this, valor, valorAntigo);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else {
                try {
                    retornoDao.atualizaSaldoMensalOrc(this, valor, valorAntigo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(valor < valorAntigo){
                diferencaSaldoOrc = valorAntigo - valor;
                saldoOrcamento = saldoOrcamento - diferencaSaldoOrc;
                if(saldoOrcamento < 0){
                    retornoDao.atualizaSaldoMesOrcamentoNegativo(this, orcamento.getSaldo(), saldoOrcamento);
                }
                orcamento.setSaldo(saldoOrcamento);
            }else if(valor > valorAntigo){
                diferencaSaldoOrc = valor - valorAntigo;
                saldoOrcamento = saldoOrcamento + diferencaSaldoOrc;
                if(orcamento.getSaldo() < 0 && saldoOrcamento >= 0){
                    retornoDao.atualizaSaldoMesOrcamentoPositivo(this, diferencaSaldoOrc);
                }
                orcamento.setSaldo(saldoOrcamento);
            }
            orcamento.setFormadePagamento(forma);
            orcamento.setValorInicial(valor);
            orcamento.setMes(mes);
            try {
                Dao.CreateOrUpdateStatus res = orcamentoDao.createOrUpdate(orcamento);
                if(res.isCreated()){
                    Toast.makeText(NovoItemActivity.this, R.string.toast_salvo, Toast.LENGTH_LONG).show();
                }else if(res.isUpdated()){
                    Toast.makeText(NovoItemActivity.this, R.string.toast_atualizado, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(NovoItemActivity.this, R.string.toast_erro, Toast.LENGTH_LONG).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        orcamento = new Orcamento();
        editNomeItem.setText("");
        editValorItem.setText("");
        voltar(view);
    }
}