package senac.com.br.controledegastos.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;

import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.AdapterLvOrcamento;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;
import senac.com.br.controledegastos.util.ActivityHelper;
import senac.com.br.controledegastos.util.RetornoDao;

//Created by Carlos Lohmeyer.

public class ListaDeItensDeOrcamentoActivity extends AppCompatActivity {

    private ListView lvOrcamentos;
    private AdapterLvOrcamento adapterLvOrcamento;
    private ArrayList<Orcamento> orcamentos;
    private Mes mes;
    private Orcamento orcamentoSelecionado;
    private RetornoDao retornoDao;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Dao<Mes, Integer> mesDao;
    private Float saldoMes, saldoOrcamento, cartaoOrcamento, cartaoOrcamentoFechado, cartaoMes, valorInicial, valor;
    private TextView tvcabecalho;
    private boolean condicional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_itens);
        iniciar();
        mes = retornoDao.retornaMesAtual(this);
        orcamentos = retornoDao.retornaListaDeOrcamentosAtuais(this);
        adapterLvOrcamento = new AdapterLvOrcamento(this, orcamentos);
        lvOrcamentos.setAdapter(adapterLvOrcamento);
        lvOrcamentos.setOnItemClickListener(cliqueCurto());
        lvOrcamentos.setOnItemLongClickListener(cliqueLongo(this));
        try {
            orcamentoDao = MyORMLiteHelper.getInstance(this).getOrcamentoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            mesDao = MyORMLiteHelper.getInstance(this).getMesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvcabecalho.setText(getString(R.string.tvCabecalho) + " " + mes.getNome() + " / " + mes.getAno());
    }

    public AdapterView.OnItemLongClickListener cliqueLongo(final Context context){
        return  new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Resgatar o orçamento selecionado pelo usuário
                orcamentoSelecionado = (Orcamento) adapterLvOrcamento.getItem(position);
                condicional = retornoDao.retornarIsCredito(context, orcamentoSelecionado);
                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaDeItensDeOrcamentoActivity.this);
                alerta.setTitle(getString(R.string.del_orcamento));
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                if(orcamentoSelecionado.isGastoMultiplo() == false){
                    if(!orcamentoSelecionado.getFormadePagamento().equals(getString(R.string.credito))){
                        alerta.setMessage(getString(R.string.msg_apagar_orcamento) + " " + orcamentoSelecionado.getNome() + "?");
                    }else {
                        alerta.setMessage(getString(R.string.msg_apagar_orcamentoFixo_cartao) + " " + orcamentoSelecionado.getNome() + "?");
                    }
                }
                if(condicional == true){
                    alerta.setMessage(getString(R.string.msg_apagar_orcamento_cartao) + " " + orcamentoSelecionado.getNome() + "?");
                }else {
                    alerta.setMessage(getString(R.string.msg_apagar_orcamento) + " " + orcamentoSelecionado.getNome() + "?");
                }
                alerta.setNegativeButton(getString(R.string.nao), null);
                alerta.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mes = retornoDao.retornaMesAtual(context);
                        saldoMes = mes.getSaldoMensal();
                        if(mes.getCartaoMesAtual() != null){
                            cartaoMes = mes.getCartaoMesAtual();
                        }else {
                            cartaoMes = Float.valueOf("0");
                        }
                        if(orcamentoSelecionado.isGastoMultiplo() == false){
                            if(!orcamentoSelecionado.getFormadePagamento().equals(getString(R.string.credito))){
                                saldoMes = saldoMes + orcamentoSelecionado.getValorInicial();
                                mes.setSaldoMensal(saldoMes);
                                try {
                                    Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
                                    if(res.isUpdated()){
                                        Toast.makeText(ListaDeItensDeOrcamentoActivity.this, getString(R.string.toast_att_mes), Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(ListaDeItensDeOrcamentoActivity.this, getString(R.string.toast_erro), Toast.LENGTH_LONG).show();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                cartaoOrcamentoFechado = orcamentoSelecionado.getValorInicial();
                                cartaoMes = cartaoMes - cartaoOrcamentoFechado;
                                mes.setCartaoMesAtual(cartaoMes);
                                try {
                                    Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
                                    if(res.isUpdated()){
                                        Toast.makeText(ListaDeItensDeOrcamentoActivity.this, getString(R.string.toast_att_mes), Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(ListaDeItensDeOrcamentoActivity.this, getString(R.string.toast_erro), Toast.LENGTH_LONG).show();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else {
                            saldoOrcamento = orcamentoSelecionado.getSaldo();
                            cartaoOrcamento = retornoDao.retornarOrcamentoCredito(context, orcamentoSelecionado);
                            valorInicial = orcamentoSelecionado.getValorInicial();
                            if(saldoOrcamento < 0){
                                valor = valorInicial + (saldoOrcamento * (-1));
                            }else if(saldoOrcamento > 0 || saldoOrcamento == 0){
                                valor = valorInicial;
                            }
                            retornoDao.apagarGastos(context, orcamentoSelecionado);
                            saldoMes = saldoMes + valor;
                            cartaoMes = cartaoMes - cartaoOrcamento;
                            mes.setSaldoMensal(saldoMes);
                            mes.setCartaoMesAtual(cartaoMes);
                            try {
                                Dao.CreateOrUpdateStatus res = mesDao.createOrUpdate(mes);
                                if(res.isUpdated()){
                                    Toast.makeText(ListaDeItensDeOrcamentoActivity.this, getString(R.string.toast_att_mes), Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(ListaDeItensDeOrcamentoActivity.this, getString(R.string.toast_erro), Toast.LENGTH_LONG).show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            orcamentoDao.delete(orcamentoSelecionado);
                            orcamentos.remove(orcamentoSelecionado);
                            adapterLvOrcamento = new AdapterLvOrcamento(ListaDeItensDeOrcamentoActivity.this, (ArrayList<Orcamento>) orcamentos);
                            lvOrcamentos.setAdapter(adapterLvOrcamento);
                            Toast.makeText(ListaDeItensDeOrcamentoActivity.this, getString(R.string.toast_del_orcamento), Toast.LENGTH_LONG).show();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //orcamentoSelecionado = new Orcamento();
                alerta.show();
                return true;
            }
        };
    }

    public AdapterView.OnItemClickListener cliqueCurto(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Resgatar o orçamento selecionado pelo usuário
                final Orcamento orcamentoSelecionado = (Orcamento) adapterLvOrcamento.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaDeItensDeOrcamentoActivity.this);
                alerta.setTitle(getString(R.string.editar_orcamento));
                alerta.setIcon(android.R.drawable.ic_menu_edit);
                alerta.setMessage(getString(R.string.msg_editar_orcamento) + " " + orcamentoSelecionado.getNome() + "?");
                alerta.setNeutralButton(getString(R.string.nao), null);
                alerta.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(ListaDeItensDeOrcamentoActivity.this, NovoItemActivity.class);
                        it.putExtra("orcamento", orcamentoSelecionado);
                        startActivity(it);
                    }
                });
                alerta.show();
            }
        };
    }

    public void iniciar(){
        lvOrcamentos = (ListView) findViewById(R.id.lvOrcamentos);
        orcamentos = new ArrayList<Orcamento>();
        mes = new Mes();
        orcamentoSelecionado = new Orcamento();
        retornoDao = new RetornoDao();
        tvcabecalho = (TextView) findViewById(R.id.tvCabecalho);
    }

    public void voltar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}