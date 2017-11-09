package senac.com.br.controledegastos.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
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
    Mes mes;
    RetornoDao retornoDao;
    Dao<Orcamento, Integer> orcamentoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        setContentView(R.layout.activity_lista_de_itens);
        iniciar();
        mes = retornoDao.retornaMesAtual(this);
        orcamentos = retornoDao.retornaListaDeOrcamentosAtuais(this);
        adapterLvOrcamento = new AdapterLvOrcamento(this, orcamentos);
        lvOrcamentos.setAdapter(adapterLvOrcamento);
        lvOrcamentos.setOnItemClickListener(cliqueCurto());
        lvOrcamentos.setOnItemLongClickListener(cliqueLongo());
    }

    public AdapterView.OnItemLongClickListener cliqueLongo(){
        return  new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Resgatar o orÃ§amento selecionado pelo usuÃ¡rio
                final Orcamento orcamentoSelecionado = (Orcamento) adapterLvOrcamento.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaDeItensDeOrcamentoActivity.this);
                alerta.setTitle(R.string.del_orcamento);
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                alerta.setMessage(R.string.msg_apagar_orcamento + " " + orcamentoSelecionado.getNome() + "?");
                alerta.setNegativeButton(R.string.nao, null);
                alerta.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            orcamentoDao.delete(orcamentoSelecionado);
                            orcamentos.remove(orcamentoSelecionado);
                            adapterLvOrcamento = new AdapterLvOrcamento(ListaDeItensDeOrcamentoActivity.this, (ArrayList<Orcamento>) orcamentos);
                            lvOrcamentos.setAdapter(adapterLvOrcamento);
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
                //Resgatar o orÃ§amento selecionado pelo usuÃ¡rio
                final Orcamento orcamentoSelecionado = (Orcamento) adapterLvOrcamento.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaDeItensDeOrcamentoActivity.this);
                alerta.setTitle(R.string.editar_orcamento);
                alerta.setIcon(android.R.drawable.ic_menu_edit);
                alerta.setMessage(R.string.msg_editar_orcamento + " " + orcamentoSelecionado.getNome() + "?");
                alerta.setNeutralButton(R.string.nao, null);
                alerta.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(ListaDeItensDeOrcamentoActivity.this, NovoGastoActivity.class);
                        it.putExtra("idOrcamento", orcamentoSelecionado.getId());
                        startActivity(it);
                    }
                });
                alerta.show();
            }
        };
    }

    public void iniciar(){
        orcamentos = new ArrayList<Orcamento>();
        mes = new Mes();
        lvOrcamentos = (ListView) findViewById(R.id.lvOrcamentos);
    }

    public void voltar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}