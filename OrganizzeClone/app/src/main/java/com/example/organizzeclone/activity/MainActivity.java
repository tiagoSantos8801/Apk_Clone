package com.example.organizzeclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.organizzeclone.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()//Criando Slidde
                .background(android.R.color.white)//Cor de fundo
                .fragment(R.layout.intro_1)//Layout criado
                .canGoBackward(false)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)//Pode ir em frente
                .build());

    }

    public void btEntrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public  void btCadasrtar(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

}