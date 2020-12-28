package com.example.organizzeclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.organizzeclone.R;
import com.example.organizzeclone.helper.DateCustom;
import com.google.android.material.textfield.TextInputEditText;

public class ReceitasActivity extends AppCompatActivity {

     private TextInputEditText campoDataR, campoDescricaoR, campoCategoriaR;
     private EditText campoValorR;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_receitas);

          //Inicializando campos
          inicializandoCampos();

          campoDataR.setText(DateCustom.dataAtual());//Exibindo data formatada

     }

     public void salvarReceitas(View view){

     }

     private void inicializandoCampos(){
          campoValorR = findViewById(R.id.editValorR);
          campoDataR = findViewById(R.id.editDataR);
          campoCategoriaR = findViewById(R.id.editCategoriaR);
          campoDescricaoR = findViewById(R.id.editDescricaoR);
     }
}