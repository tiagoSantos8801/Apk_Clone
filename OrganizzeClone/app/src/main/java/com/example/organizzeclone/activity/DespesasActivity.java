package com.example.organizzeclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.organizzeclone.R;
import com.example.organizzeclone.helper.DateCustom;
import com.example.organizzeclone.model.Movimentacao;
import com.google.android.material.textfield.TextInputEditText;

public class DespesasActivity extends AppCompatActivity {

     private TextInputEditText campoDataD, campoDescricaoD, campoCategoriaD;
     private EditText campoValorD;
     private Movimentacao movimentacao;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_despesas);

          //Inicializando campos
          inicializandoCampos();

          campoDataD.setText(DateCustom.dataAtual());//Exibindo data formatada - do sistema

     }

     public void salvarDespesa(View view){

          //Carregando valores no obj
          movimentacao = new Movimentacao();
          String dataEscolhida = campoDataD.getText().toString();

          movimentacao.setValor(Double.parseDouble(campoValorD.getText().toString()));
          movimentacao.setCategoria(campoCategoriaD.getText().toString());
          movimentacao.setDescricao(campoDescricaoD.getText().toString());
          movimentacao.setData(dataEscolhida);
          movimentacao.setTipo("D");

          //Salvando dados do obj no banco
          movimentacao.salvar(dataEscolhida);
          finish();
     }

     private void inicializandoCampos(){
          campoValorD = findViewById(R.id.editValorD);
          campoDataD = findViewById(R.id.editDataD);
          campoCategoriaD = findViewById(R.id.editCategoriaD);
          campoDescricaoD = findViewById(R.id.editDescricaoD);
     }

}