package senac.com.br.controledegastos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;

import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.components.SampleSlide;
import senac.com.br.controledegastos.util.ActivityHelper;

public class AvisoActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        addSlide(SampleSlide.newInstance(R.layout.aviso_slide_1));
        showSkipButton(false);
        setSlideOverAnimation();
        }
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent orc = new Intent(this,ListaDeItensDeOrcamentoActivity.class);
        startActivity(orc);
    }
    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
};


