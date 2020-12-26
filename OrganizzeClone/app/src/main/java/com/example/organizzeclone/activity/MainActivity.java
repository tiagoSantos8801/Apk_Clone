package com.example.organizzeclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.organizzeclone.R;
import com.example.organizzeclone.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth auth;

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

    @Override
    protected void onStart() {
        verificarUsuarioLogado();//Redirecionamento para PrincipalActivity.class, quando usu logado, quando inicia a activity
        super.onStart();
    }

    public void btEntrar(View view){
        startActivity(new Intent(this,
                LoginActivity.class)
        );
    }

    public  void btCadasrtar(View view){
        startActivity(new Intent(this,
                CadastroActivity.class)
        );
    }

    public void verificarUsuarioLogado(){
        auth = ConfiguracaoFirebase.getFireBaseAutentificacao();
        if (auth.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

}