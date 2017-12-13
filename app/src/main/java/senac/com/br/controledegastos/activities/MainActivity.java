package senac.com.br.controledegastos.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import senac.com.br.controledegastos.DAO.MyORMLiteHelper;
import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.util.RetornoDao;
import senac.com.br.controledegastos.util.TimeChangedReceiver;

import static android.R.attr.id;

@SuppressWarnings("MissingPermission")
public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle, data;
    private RetornoDao r;
    private Mes mes;
    private Float tvSaldo, tvGastos, tvcartao, zero, zeroNegativo;
    private boolean confirmarMesAtual;
    private TimeChangedReceiver timeChange;
    private TextView textview1, saldo, nomeSaldo, gastosMes, cartaoAnterior;
    private String tvSaldoFormatado, tvGastosFormatado, tvcartaoFormatado;

    private ArrayList<Mes> meses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = new RetornoDao();
        timeChange = new TimeChangedReceiver();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        data = dataAtual(calendar);

        try {
            r.instanciarMes(this, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        meses = new ArrayList<Mes>();
        meses = r.retornaListaDeMeses(this);
        System.out.println("QUANTIDADE DE MESES  " + meses.size());

        mes = new Mes();
        mes = r.retornaMesAtual(this);
        confirmarMesAtual = r.verificarMesAtual(mes, data, this);
        if(confirmarMesAtual == false){
            try {
                r.iniciarNovoMes(this, data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //lançar activity de novo mês
            //launchMonthly();
        }
        zero = Float.valueOf("0.00");
        zeroNegativo = Float.valueOf("-0.00");
        textview1 = (TextView) findViewById(R.id.textview1);
        nomeSaldo = (TextView) findViewById(R.id.nomoSaldo);
        saldo = (TextView) findViewById(R.id.dinheiroMes);
        gastosMes = (TextView) findViewById(R.id.gastosMes);
        cartaoAnterior = (TextView) findViewById(R.id.cartaoAnterior);
        textview1.setText(mes.getNome() + " / " + mes.getAno());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if(mes.getSaldoMensal() == zeroNegativo){
            mes.setSaldoMensal(zero);
        }
        if(mes.getRenda() == null){
            saldo.setText(getString(R.string.cifrao) + " 0.00");
            gastosMes.setText(getString(R.string.cifrao) + " 0.00");
        }else if(mes.getRenda() != null && mes.getSaldoMensal().equals(mes.getRenda())){
            tvSaldo = mes.getSaldoMensal();
            tvSaldoFormatado = decimalFormat.format(tvSaldo);
            saldo.setText(getString(R.string.cifrao) + " " + tvSaldoFormatado.replace(",", "."));
            gastosMes.setText(getString(R.string.cifrao) + " 0.00");
        }else {
            if(mes.getSaldoMensal() < zero){
                saldo.setTextColor(Color.RED);
                nomeSaldo.setTextColor(Color.RED);
            }else if(mes.getSaldoMensal() == zero){
                saldo.setTextColor(Color.BLACK);
                nomeSaldo.setTextColor(Color.BLACK);
            }else if(mes.getSaldoMensal() > zero){
                saldo.setTextColor(Color.BLUE);
                nomeSaldo.setTextColor(Color.BLUE);
            }
            tvSaldo = mes.getSaldoMensal();
            tvSaldoFormatado = decimalFormat.format(tvSaldo);
            tvGastos = r.retornaEAtualizaTotalDeGastos(this);
            tvGastosFormatado = decimalFormat.format(tvGastos);
            saldo.setText(getString(R.string.cifrao) + " " + tvSaldoFormatado.replace(",", "."));
            gastosMes.setText(getString(R.string.cifrao) + " " + tvGastosFormatado.replace(",", "."));
            ArrayList<Mes> meses = new ArrayList();
            meses = r.retornaListaDeMeses(this);
            for(int bbb = 0; bbb < meses.size(); bbb ++) {
                System.out.println("Nome " + meses.get(bbb).getNome());
            }
        }
        if(mes.getCartaoMesAnterior() == null){
            cartaoAnterior.setText(getString(R.string.cifrao) + " 0.00");
        }else {
            tvcartao = mes.getCartaoMesAnterior();
            tvcartaoFormatado = decimalFormat.format(tvcartao);
            cartaoAnterior.setText(getString(R.string.cifrao) + " " + tvcartaoFormatado.replace(",", "."));
        }

        /* testando o tutorial - esse intent sempre vai lançar on start*/
        //Intent j = new Intent(this,TutorialActivity.class);
        //startActivity(j);
        launchTutorial();

        // para o ambiente de testes é necessário inicializar os slides de controle de mês todas as vezes que o aplicativo for lançado
        //Intent i = new Intent(this,IntroActivity.class);
         //startActivity(i);

        // inicializando o menu lateral
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
       }

/*esse metodo vai controlar o inicio e fim de mês*/
//TODO implementar logica de controle de dadas - validar isso vai ser uma bosta

    private void launchMonthly(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
                //  If the activity has never started before...
                if (isFirstStart) {
                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });
                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();
                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);
                    //  Apply changes
                    e.apply();
                }
            }
        });
        // Start the thread
        t.start();
    }

    private void launchTutorial(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                boolean isMonthStart = getPrefs.getBoolean("monthStart", true);
                if (isMonthStart) {
                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });
                    SharedPreferences.Editor e = getPrefs.edit();
                    e.putBoolean("monthStart", false);
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
                        break;
                    case 3:
                        //Adiciona novo gasto
                        AdicionarGasto();
                        break;
                    case 4:
                        // visualizar gastos
                        VisualizarGastos();
                        break;
                    case 5:
                        // gerar relatório
                        Toast.makeText(MainActivity.this, "Em breve", Toast.LENGTH_SHORT).show();
                        break;
                }


            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely closed state. */
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
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

       if (id == R.id.action_tutorial){
            mostrarTutorial(item);
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

    private Intent chamarAviso(){
        final Intent i = new Intent(this,AvisoActivity.class);
        return i;
    }

    public void adicionarGasto(View view){
        AdicionarGasto();
    }

    public void verCartao(View view){
        if(mes.getCartaoMesAtual() == null){
            tvSaldo = Float.valueOf("0.00");
        }else {
            tvSaldo = mes.getCartaoMesAtual();
        }
        DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
        tvSaldoFormatado = decimalFormat2.format(tvSaldo);
        Toast.makeText(this, getString(R.string.toast_cartao) + " " + tvSaldoFormatado.replace(",", "."), Toast.LENGTH_LONG).show();

    }
    public void mostrarTutorial(MenuItem menuItem){
        Intent i = new Intent(this,TutorialActivity.class);
        startActivity(i);
    };
    @Override
    protected void onResume() {
        super.onResume();
    }
}