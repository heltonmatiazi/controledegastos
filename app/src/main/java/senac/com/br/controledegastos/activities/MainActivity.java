package senac.com.br.controledegastos.activities;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.SystemClock;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.util.RetornoDao;
import senac.com.br.controledegastos.util.TimeChangedReceiver;

public class MainActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle, data, hora;
    private RetornoDao r;
    private Mes mes;
    private boolean confirmarMesAtual;
    private TimeChangedReceiver timeChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // dar um jeito de invocar o aviso activity aqui daqui

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
        mes = r.mesAtual(this);
        confirmarMesAtual = r.verificarMesAtual(mes, data);
        if(confirmarMesAtual == false){
        // lançar activity de novo mês
            //launchMonthly();
        }
        /* testando o tutorial - esse intent sempre vai lançar on start*/
        Intent j = new Intent(this,TutorialActivity.class);
        startActivity(j);
       // launchTutorial();

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

    // esse método vai lançar o tutorial na primeira vez que o usuário utilizar o aplicativo
    private void launchTutorial(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                //  Create a new boolean and preference and set it to true
                boolean isMonthStart = getPrefs.getBoolean("monthStart", true);
                //  If the activity has never started before...
                if (isMonthStart) {
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
                    e.putBoolean("monthStart", false);
                    //  Apply changes
                    e.apply();
                }
            }
        });
        // Start the thread
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
                        Toast.makeText(MainActivity.this, "Opção 2 selecionada com sucesso", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, "Opção 5 selecionada com sucesso", Toast.LENGTH_SHORT).show();
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
        if (id == R.id.action_settings) {
            return true;
        }
        // Activate the navigation drawer toggle
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
}