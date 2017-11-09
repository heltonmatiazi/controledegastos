package senac.com.br.controledegastos.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

import senac.com.br.controledegastos.R;
import senac.com.br.controledegastos.components.SampleSlide;
import senac.com.br.controledegastos.util.ActivityHelper;

//Created by helton on 14/09/2017.

public class TutorialActivity extends AppIntro {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        addSlide(SampleSlide.newInstance(R.layout.tutorial_slide_1));
        addSlide(SampleSlide.newInstance(R.layout.tutorial_slide_2));
        addSlide(SampleSlide.newInstance(R.layout.tutorial_slide_3));
        addSlide(SampleSlide.newInstance(R.layout.tutorial_slide_4));
        addSlide(SampleSlide.newInstance(R.layout.tutorial_slide_5));

        showSkipButton(true);

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
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}