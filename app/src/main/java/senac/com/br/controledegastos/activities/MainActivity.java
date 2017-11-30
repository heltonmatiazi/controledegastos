package senac.com.br.controledegastos.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.util.ActivityHelper;
import senac.com.br.controledegastos.util.RetornoDao;
import senac.com.br.controledegastos.util.TimeChangedReceiver;

import static java.lang.Integer.parseInt;
import static senac.com.br.controledegastos.R.id.gastosMes;
import static senac.com.br.controledegastos.R.id.textview1;
import static senac.com.br.controledegastos.R.id.tvGastos;


@SuppressWarnings("MissingPermission")
public class MainActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle, data, hora;
    private RetornoDao r;
    private Mes mes;
    private Float tvSaldo, tvGastos;
    private boolean confirmarMesAtual;
    private TimeChangedReceiver timeChange;
    private TextView textview1,saldo, gastosMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        setContentView(R.layout.activity_main);
        r = new RetornoDao();
        // testando banco de dados
        try {
            r.inserirMesParaTeste(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // inicializando o broadcast receiver para detectar mudanças de hora no dispositivo
        Intent it = new Intent(this,TimeChangedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,10,it,PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime();
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME,futureInMillis,5000,pendingIntent);

        //TODO Pega o dia e a hora atual do dispositivo
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        data = dataAtual(calendar);
        hora = horaAtual(calendar);
        mes = new Mes();
        mes = r.retornaMesAtual(this);
        confirmarMesAtual = r.verificarMesAtual(mes, data, this);

        textview1 = (TextView) findViewById(R.id.textview1);
        saldo = (TextView) findViewById(R.id.dinheiroMes);
        gastosMes = (TextView) findViewById(gastosMes);
        textview1.setText(mes.getNome() + " / " + mes.getAno());
        if(mes.getRenda() == null){
            saldo.setText(getString(R.string.cifrao) + " 0.0");
            gastosMes.setText(getString(R.string.cifrao) + " 0.0");
        }else {
            tvSaldo = mes.getSaldoMensal();
            tvGastos = r.retornaEAtualizaTotalDeGastos(this);
            saldo.setText(getString(R.string.cifrao) + " " + tvSaldo);
            gastosMes.setText(getString(R.string.cifrao) + " " + tvGastos);
            /*if(mes.getRenda() == mes.getSaldoMensal()){
                gastosMes.setText(getString(R.string.cifrao) + " 0,00");
            }else {
                gastosMes.setText(getString(R.string.cifrao) + " " + tvGastos);
            }*/
        }

        // lança o tutorial no primeiro acesso
        launchTutorial();
        // o metodo 'primeiroDia' retorna true caso o numero do dia seja igual a 1.
        if(primeiroDia(calendar) == true){
            launchMonthly();
        };
        // inicializando o menu lateral
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
       }
    private void launchMonthly(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                boolean isFirstDayOfMonth = getPrefs.getBoolean("primeiroDia", true);
                if (isFirstDayOfMonth) {
                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });
                    SharedPreferences.Editor editor = getPrefs.edit();
                    editor.putBoolean("primeiroDia", false);
                    editor.apply();
                }
            }
        });
        t.start();
    }
    private void launchTutorial(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                boolean isMonthStart = getPrefs.getBoolean("primeiroAcesso", true);
                if (isMonthStart) {
                    final Intent i = new Intent(MainActivity.this, TutorialActivity.class);
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });
                    SharedPreferences.Editor e = getPrefs.edit();
                    e.putBoolean("primeiroAcesso", false);
                    e.apply();
                }
            }
        });
        t.start();
    }
    private void addDrawerItems() {
        String[] opcoesArray = { getResources().getString(R.string.drawer_novoItem),
                getResources().getString(R.string.drawer_verItem), getResources().getString(R.string.drawer_editar),
                getResources().getString(R.string.drawer_novoGasto), getResources().getString(R.string.drawer_verGastos),
                getResources().getString(R.string.drawer_gerar)};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opcoesArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   switch (position){
                        case 0:
                            //Adicionar um item de orçamento
                            AdicionarItem();
                            break;
                        case 1:
                            // visualizar itens do orçamento
                            VisualizarItens();
                            break;
                        case 2:
                            // editar o valor da renda mensal
                            EditarRenda();
                        case 3:
                            //Adiciona novo gasto
                            ;AdicionarGasto();
                            break;
                        case 4:
                            // visualizar gastos
                            VisualizarGastos();
                            break;
                        case 5:
                            // gerar relatório
                            Toast.makeText(MainActivity.this, "Opção 6 selecionada com sucesso", Toast.LENGTH_SHORT).show();
                            break;
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AdicionarGasto(){
        Intent i = new Intent(this, NovoGastoActivity.class);
        startActivity(i);
    }

    public void AdicionarItem(){
        Intent i = new Intent(this, NovoItemActivity.class);
        startActivity(i);
    }

    public void VisualizarGastos(){
        Intent i = new Intent(this,ListaDeGastosActivity.class);
        startActivity(i);
    }

    public void VisualizarItens(){
        Intent i = new Intent(this,ListaDeItensDeOrcamentoActivity.class);
        startActivity(i);
    }

    public void EditarRenda(){
        Intent i = new Intent(this, EditarRendaActivity.class);
        startActivity(i);
    }

    private String dataAtual(Calendar calendar){
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        String data_atual = dataFormatada.format(calendar.getTime());
        return data_atual;
    }

    private String horaAtual(Calendar calendar){
        SimpleDateFormat horaFormatada = new SimpleDateFormat("HH:mm:ss");
        String hora_atual = horaFormatada.format(calendar.getTime());
        return hora_atual;
    }

    private Boolean primeiroDia(Calendar calendar){
        SimpleDateFormat diaFormatado = new SimpleDateFormat("d");
        String diaAtual = diaFormatado.format(calendar.getTime());
        Integer dia = parseInt(diaAtual);
        if(dia == 1){
            return true;
        }else{
            return false;
        }
    };

    private Intent chamarAviso(){
        final Intent i = new Intent(this,AvisoActivity.class);
        return i;
    }

    public void verCartao(View view){
        Toast.makeText(this, "Total do cartão de crédito para o mês seguinte: R$ " + mes.getCartaoMesAtual(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("TESTANDO O CARTÃO MES ATUAL R$ " + mes.getCartaoMesAtual());
    }
}