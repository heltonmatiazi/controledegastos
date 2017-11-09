package senac.com.br.controledegastos.activities;

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
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.AdapterLvGastos;
import senac.com.br.controledegastos.model.AdapterSpinnerOrcamento;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;
import senac.com.br.controledegastos.util.RetornoDao;

//Created by Carlos Lohmeyer.

public class ListaDeGastosActivity extends AppCompatActivity {

    private ListView lvGastos;
    private AdapterLvGastos adapterLvGastos;
    private ArrayList<Gasto> gastosOrcamento;
    private ArrayList<Orcamento> orcamentos;
    Mes mes;
    RetornoDao retornoDao;
    private Spinner spinnerListar;
    private AdapterSpinnerOrcamento adapterSpinner;
    Dao<Gasto, Integer> gastoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_gastos);

        iniciar();
        mes = retornoDao.retornaMesAtual(this);
        orcamentos = retornoDao.retornarListaDeOrcamentosComGastos(this, mes);
        adapterSpinner = new AdapterSpinnerOrcamento(this, orcamentos);
        spinnerListar.setAdapter(adapterSpinner);
    }

    public void iniciar(){
        gastosOrcamento = new ArrayList<Gasto>();
        orcamentos = new ArrayList<Orcamento>();
        mes = new Mes();
        spinnerListar = (Spinner) findViewById(R.id.spListar);
        lvGastos = (ListView) findViewById(R.id.lvGastos);
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
        lvGastos.setOnItemLongClickListener(cliqueLongo());
    }

    public void voltar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public AdapterView.OnItemLongClickListener cliqueLongo(){
        return  new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Gasto gastoSelecionado = (Gasto) adapterLvGastos.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaDeGastosActivity.this);
                alerta.setTitle(R.string.del_gasto);
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                alerta.setMessage(R.string.msg_apagar_gasto );
                alerta.setNegativeButton(R.string.nao, null);
                alerta.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                //Resgatar o gasto selecionado pelo usuÃ¡rio
                final Gasto gastoSelecionado = (Gasto) adapterLvGastos.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaDeGastosActivity.this);
                alerta.setTitle(R.string.editar_gasto);
                alerta.setIcon(android.R.drawable.ic_menu_edit);
                alerta.setMessage(R.string.msg_editar_gasto);
                alerta.setNeutralButton(R.string.nao, null);
                alerta.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(ListaDeGastosActivity.this, NovoGastoActivity.class);
                        it.putExtra("idGasto", gastoSelecionado.getId());
                        startActivity(it);
                    }
                });
                alerta.show();
            }
        };
    }

}