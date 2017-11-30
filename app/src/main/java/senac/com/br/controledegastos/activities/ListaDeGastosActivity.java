package senac.com.br.controledegastos.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;

import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.AdapterLvGastos;
import senac.com.br.controledegastos.model.AdapterSpinnerOrcamento;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;
import senac.com.br.controledegastos.util.ActivityHelper;
import senac.com.br.controledegastos.util.RetornoDao;

//Created by Carlos Lohmeyer.

public class ListaDeGastosActivity extends AppCompatActivity {

    private ListView lvGastos;
    private AdapterLvGastos adapterLvGastos;
    private ArrayList<Gasto> gastosOrcamento;
    private ArrayList<Orcamento> orcamentos;
    private Mes mes;
    private Orcamento orcamento;
    private RetornoDao retornoDao;
    private Spinner spinnerListar;
    private AdapterSpinnerOrcamento adapterSpinner;
    private Dao<Mes, Integer> mesDao;
    private Dao<Orcamento, Integer> orcamentoDao;
    private Dao<Gasto, Integer> gastoDao;
    private Float mesCartao, orcamentosaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_gastos);

        iniciar();
        mes = retornoDao.retornaMesAtual(this);
        orcamentos = retornoDao.retornarListaDeOrcamentosComGastos(this, mes);
        adapterSpinner = new AdapterSpinnerOrcamento(this, orcamentos);
        spinnerListar.setAdapter(adapterSpinner);
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
    }

    public void iniciar(){
        lvGastos = (ListView) findViewById(R.id.lvGastos);
        gastosOrcamento = new ArrayList<Gasto>();
        orcamentos = new ArrayList<Orcamento>();
        mes = new Mes();
        orcamento = new Orcamento();
        spinnerListar = (Spinner) findViewById(R.id.spListar);
        retornoDao = new RetornoDao();
    }

    public void listarGastos(View view){
        //Capturar o item de orcamento selecionado
        Orcamento orcamento = (Orcamento) spinnerListar.getSelectedItem();
        //Com o item, puxar os gastos dele
        gastosOrcamento = retornoDao.retornaListaDeGastos(this, orcamento);
        //Listar os gastos no listview
        adapterLvGastos = new AdapterLvGastos(this, gastosOrcamento);
        lvGastos.setAdapter(adapterLvGastos);
        lvGastos.setCacheColorHint(Color.TRANSPARENT);
        lvGastos.setOnItemClickListener(cliqueCurto());
        lvGastos.setOnItemLongClickListener(cliqueLongo(this));
    }

    public void voltar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public AdapterView.OnItemLongClickListener cliqueLongo(final Context context){
        return  new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Gasto gastoSelecionado = (Gasto) adapterLvGastos.getItem(position);
                mesCartao = mes.getCartaoMesAtual();
                orcamentosaldo = retornoDao.retornaSaldoOrcamento(context, gastoSelecionado.getOrcamento().getId());
                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaDeGastosActivity.this);
                alerta.setTitle(getString(R.string.del_gasto));
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                if(gastoSelecionado.getFormadePagamento().equals(getString(R.string.credito))){
                    alerta.setMessage(getString(R.string.msg_apagar_gasto_cartao));
                }else {
                    alerta.setMessage(getString(R.string.msg_apagar_gasto));
                }
                alerta.setNegativeButton(getString(R.string.nao), null);
                alerta.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(gastoSelecionado.getFormadePagamento().equals(getString(R.string.credito))){
                            mesCartao = mesCartao - gastoSelecionado.getValor();
                            mes.setCartaoMesAtual(mesCartao);
                            try {
                                Dao.CreateOrUpdateStatus resMes = mesDao.createOrUpdate(mes);
                                if(resMes.isUpdated()){
                                    System.out.println("MES ATUALIZADO AO EXCLUIR GASTO");
                                }else {
                                    System.out.println("ERRO AO ATUALIZAR O MES AO EXCLUIR GASTO");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }else {
                            orcamentosaldo = orcamentosaldo + gastoSelecionado.getValor();
                            orcamento.setSaldo(orcamentosaldo);
                            try {
                                Dao.CreateOrUpdateStatus resOrc = orcamentoDao.createOrUpdate(orcamento);
                                if(resOrc.isUpdated()){
                                    System.out.println("MES ATUALIZADO AO EXCLUIR GASTO");
                                }else {
                                    System.out.println("ERRO AO ATUALIZAR O MES AO EXCLUIR GASTO");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            gastoDao.delete(gastoSelecionado);
                            gastosOrcamento.remove(gastoSelecionado);
                            adapterLvGastos = new AdapterLvGastos(ListaDeGastosActivity.this, (ArrayList<Gasto>) gastosOrcamento);
                            lvGastos.setAdapter(adapterLvGastos);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alerta.show();
                return true;
            }
        };
    }

    public AdapterView.OnItemClickListener cliqueCurto(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Resgatar o gasto selecionado pelo usuário
                final Gasto gastoSelecionado = (Gasto) adapterLvGastos.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaDeGastosActivity.this);
                alerta.setTitle(getString(R.string.editar_gasto));
                alerta.setIcon(android.R.drawable.ic_menu_edit);
                alerta.setMessage(getString(R.string.msg_editar_gasto));
                alerta.setNeutralButton(getString(R.string.nao), null);
                alerta.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(ListaDeGastosActivity.this, NovoGastoActivity.class);
                        it.putExtra("gasto", gastoSelecionado);
                        startActivity(it);
                    }
                });
                alerta.show();
            }
        };
    }
}