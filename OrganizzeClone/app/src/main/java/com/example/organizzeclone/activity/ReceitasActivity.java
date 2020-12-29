package com.example.organizzeclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizzeclone.R;
import com.example.organizzeclone.config.ConfiguracaoFirebase;
import com.example.organizzeclone.helper.Base64Custon;
import com.example.organizzeclone.helper.DateCustom;
import com.example.organizzeclone.model.Movimentacao;
import com.example.organizzeclone.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {

     private TextInputEditText campoDataR, campoDescricaoR, campoCategoriaR;
     private EditText campoValorR;
     private DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
     private FirebaseAuth auth = ConfiguracaoFirebase.getFireBaseAutentificacao();
     private Double receitaTotal;
     private Movimentacao movimentacao;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_receitas);

          //Inicializando campos
          inicializandoCampos();

          campoDataR.setText(DateCustom.dataAtual());//Exibindo data formatada - do sistema
          recuperaReceitaTotal();//Pegando dado de despesas do banco

     }

     public void salvarReceitas(View view){

          if (validarCamposReceita()){
               //Carregando valores no obj
               movimentacao = new Movimentacao();
               String dataEscolhida = campoDataR.getText().toString();//Data informada
               Double valorRecuperado = Double.parseDouble(campoValorR.getText().toString());//Valor informado

               //Setando obj configuracoes
               movimentacao.setValor(valorRecuperado);
               movimentacao.setCategoria(campoCategoriaR.getText().toString());
               movimentacao.setDescricao(campoDescricaoR.getText().toString());
               movimentacao.setData(dataEscolhida);
               movimentacao.setTipo("R");

               Double receitaAtualizada = valorRecuperado + receitaTotal;//Total despesas do usuario
               atualizarReceita(receitaAtualizada);//Atualiza a despeza total no banco

               //Salvando dados do obj no banco
               movimentacao.salvar(dataEscolhida);
               finish();
          }

     }

     private void inicializandoCampos(){
          campoValorR = findViewById(R.id.editValorR);
          campoDataR = findViewById(R.id.editDataR);
          campoCategoriaR = findViewById(R.id.editCategoriaR);
          campoDescricaoR = findViewById(R.id.editDescricaoR);
     }

     public boolean validarCamposReceita(){
          String textoValor = campoValorR.getText().toString();
          String textoData = campoDataR.getText().toString();
          String textoCategoria = campoCategoriaR.getText().toString();
          String textoDescricao = campoDescricaoR.getText().toString();

          if (!textoValor.isEmpty()){
               if (!textoData.isEmpty()){
                    if (!textoCategoria.isEmpty()){
                         if (!textoDescricao.isEmpty()){
                              return true;
                         } else {
                              Toast.makeText(this, "Preencha a descrição!", Toast.LENGTH_SHORT).show();
                              return false;
                         }
                    } else {
                         Toast.makeText(this, "Preencha a categoria!", Toast.LENGTH_SHORT).show();
                         return false;
                    }
               } else {
                    Toast.makeText(this, "Preencha a data!", Toast.LENGTH_SHORT).show();
                    return false;
               }
          } else {
               Toast.makeText(this, "Preencha o valor!", Toast.LENGTH_SHORT).show();
               return false;
          }
     }

     public void recuperaReceitaTotal(){

          String emailusuario = auth.getCurrentUser().getEmail();//Pega o email do usuario atual
          String idUsuario = Base64Custon.codificarBase64(emailusuario);//Criptografa pegando assim o id do usuario

          DatabaseReference referenceIdUsuario = database.child("Usuários").child(idUsuario);
          referenceIdUsuario.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //Pegando informacoes do dataBase - pega o retorno e converte em Usuario.class
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    receitaTotal = usuario.getReceitaTotal();
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
          });
     }
     public void atualizarReceita(Double receitaAtualizada){

          String emailusuario = auth.getCurrentUser().getEmail();//Pega o email do usuario atual
          String idUsuario = Base64Custon.codificarBase64(emailusuario);//Criptografa pegando assim o id do usuario
          DatabaseReference referenceUsuario = database.child("Usuários").child(idUsuario);

          referenceUsuario.child("receitaTotal").setValue(receitaAtualizada);

     }
}