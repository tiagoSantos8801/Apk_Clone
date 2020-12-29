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

public class DespesasActivity extends AppCompatActivity {

     private TextInputEditText campoDataD, campoDescricaoD, campoCategoriaD;
     private EditText campoValorD;
     private Movimentacao movimentacao;
     private Double despezaTotal;

     //Pegando instancias - FireBase
     private DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
     private FirebaseAuth auth = ConfiguracaoFirebase.getFireBaseAutentificacao();

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_despesas);

          //Inicializando campos
          inicializandoCampos();

          campoDataD.setText(DateCustom.dataAtual());//Exibindo data formatada - do sistema
          recuperaDespesaTotal();//Pegando dado de despesas do banco

     }

     public void salvarDespesa(View view){

          if (validarCamposDespesa()){
               //Carregando valores no obj
               movimentacao = new Movimentacao();
               String dataEscolhida = campoDataD.getText().toString();//Data informada
               Double valorRecuperado = Double.parseDouble(campoValorD.getText().toString());//Valor informado

               //Setando obj configuracoes
               movimentacao.setValor(valorRecuperado);
               movimentacao.setCategoria(campoCategoriaD.getText().toString());
               movimentacao.setDescricao(campoDescricaoD.getText().toString());
               movimentacao.setData(dataEscolhida);
               movimentacao.setTipo("D");

               Double despezaAtualizada = valorRecuperado + despezaTotal;//Total despesas do usuario
               atualizarDespesa(despezaAtualizada);//Atualiza a despeza total no banco

               //Salvando dados do obj no banco
               movimentacao.salvar(dataEscolhida);
               finish();
          }

     }

     private void inicializandoCampos(){
          campoValorD = findViewById(R.id.editValorD);
          campoDataD = findViewById(R.id.editDataD);
          campoCategoriaD = findViewById(R.id.editCategoriaD);
          campoDescricaoD = findViewById(R.id.editDescricaoD);
     }

     public boolean validarCamposDespesa(){

          String textoValor = campoValorD.getText().toString();
          String textoData = campoDataD.getText().toString();
          String textoCategoria = campoCategoriaD.getText().toString();
          String textoDescricao = campoDescricaoD.getText().toString();

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

     public void recuperaDespesaTotal(){

          String emailusuario = auth.getCurrentUser().getEmail();//Pega o email do usuario atual
          String idUsuario = Base64Custon.codificarBase64(emailusuario);//Criptografa pegando assim o id do usuario

          DatabaseReference referenceIdUsuario = database.child("Usuários").child(idUsuario);
          referenceIdUsuario.addValueEventListener(new ValueEventListener() {//Pegar informacoes do banco
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //Pegando informacoes do dataBase - pega o retorno e converte em Usuario.class
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    despezaTotal = usuario.getDespesaTotal();
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
          });
     }

     public void atualizarDespesa(Double despesaAtualizada){

          String emailusuario = auth.getCurrentUser().getEmail();//Pega o email do usuario atual
          String idUsuario = Base64Custon.codificarBase64(emailusuario);//Criptografa pegando assim o id do usuario
          DatabaseReference referenceUsuario = database.child("Usuários").child(idUsuario);

          referenceUsuario.child("despesaTotal").setValue(despesaAtualizada);

     }
}