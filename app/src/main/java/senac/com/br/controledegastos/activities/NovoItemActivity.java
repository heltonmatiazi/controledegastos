package senac.com.br.controledegastos.activities;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
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
import senac.com.br.controledegastos.util.Constantes;
import senac.com.br.controledegastos.util.Notificador;
import senac.com.br.controledegastos.util.RetornoDao;

//Created by Carlos Lohmeyer.

public class NovoItemActivity extends AppCompatActivity {

    private RetornoDao retornoDao;
    private Orcamento orcamento;
    private EditText editNomeItem, editValorItem;
    private LinearLayout linear_ajuda, linearPagamento;
    private boolean visao = true;
    private Mes mes, mes2;
    private Float valor, valorAntigo, diferencaSaldoOrc, saldoOrcamento;
    private CheckBox cbMultiplas;
    private String value, forma, formaAntiga;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Spinner pagamentoItem;
    private Bundle params;
    private int indiceForma;
    private Intent notificationIntent;

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
                if(cbMultiplas.callOnClick()) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(NovoItemActivity.this);
                    alerta.setTitle(R.string.notification_atencao);
                    alerta.setIcon(android.R.drawable.ic_dialog_alert);
                    alerta.setMessage(getString(R.string.aviso));
                    alerta.setNeutralButton(R.string.notification_ok, null);
                    alerta.show();
                }
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
        mes2 = new Mes();
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

        mes2 = retornoDao.retornaMesAtual(this);
        if(mes2.getSaldoMensal() <= mes2.getNotificar()){
            notificationIntent = new Intent(this, Notificador.class);
            notificar(getNotification(getString(R.string.notification_msg)));
        }

        orcamento = new Orcamento();
        editNomeItem.setText("");
        editValorItem.setText("");
        voltar(view);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Notification getNotification(String texto){
        Notification.Builder notificacao = new Notification.Builder(this);
        notificacao.setContentTitle(getString(R.string.notification_atencao));
        notificacao.setContentText(texto);
        notificacao.setSmallIcon(R.drawable.ic_money);
        //SETVIBRAÇÃO
        notificacao.setVibrate(new long[]{150, 300, 150, 600});
        //SETSOM;
        notificacao.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        //ADICIONAR OPÇOES
       notificationIntent.putExtra("acao", Constantes.BROADCAST_EXECUTAR_ACAO);
        notificationIntent.putExtra("idNotificacao", mes2.getId());
        PendingIntent pendent1 = PendingIntent.getBroadcast(this, Constantes.BROADCAST_EXECUTAR_ACAO, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificacao.addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.notification_ok), pendent1);

        notificacao.setAutoCancel(true);
        return notificacao.build();
    }

    private void notificar(Notification notification){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mes2.getId(), notification);
    }

}