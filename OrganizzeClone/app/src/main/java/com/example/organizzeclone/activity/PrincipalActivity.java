package com.example.organizzeclone.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.organizzeclone.adapter.AdapterMovimentacao;
import com.example.organizzeclone.config.ConfiguracaoFirebase;
import com.example.organizzeclone.helper.Base64Custon;
import com.example.organizzeclone.model.Movimentacao;
import com.example.organizzeclone.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.organizzeclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

     private MaterialCalendarView calendarView;
     private TextView textSaldacao, textSaldo;
     private Double despesaTotal = 0.0,
                    receitaTotal = 0.0,
                    resumoUsuario  = 0.0;
     private RecyclerView recyclerView;
     private AdapterMovimentacao adapterMovimentacao;
     List<Movimentacao> movimentacoes = new ArrayList<>();

     private FirebaseAuth auth = ConfiguracaoFirebase.getFireBaseAutentificacao();
     private DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
     private DatabaseReference referenceUsuario;//Para fechar evento de Listner
     private ValueEventListener valueEventListenerUsuario;//Para fechar evento de Listner

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_principal);

          Toolbar toolbar = findViewById(R.id.toolbar);
          toolbar.setTitle("");
          setSupportActionBar(toolbar);

          //View
          calendarView = findViewById(R.id.calendarView);
          textSaldacao = findViewById(R.id.textSaudacao);
          textSaldo = findViewById(R.id.textSaldo);
          recyclerView = findViewById(R.id.recyclerMovimentos);

          configuraCalendario();//Configurando calendario

          //Configurando adapter
          adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

          //Configurando RecyclerView
          RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
          recyclerView.setLayoutManager(layoutManager);
          recyclerView.setHasFixedSize(true);
          recyclerView.setAdapter(adapterMovimentacao);

/*
          FloatingActionButton fab = findViewById(R.id.fab);
          fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
               }
          });*/
     }

     @Override//Cria o menu
     public boolean onCreateOptionsMenu(Menu menu) {
          getMenuInflater().inflate(R.menu.menu_principal, menu);
          return super.onCreateOptionsMenu(menu);
     }

     @Override//Item de menu clicado
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
          switch (item.getItemId()){
               case R.id.menuSair://Pega um int === id do item clicado
                    auth.signOut();//Desloga
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    break;
          }
          return super.onOptionsItemSelected(item);
     }

     public void adcionarDespesas(View view){
          startActivity(new Intent(this, DespesasActivity.class));
     }

     public void adcionarReceitas(View view){
          startActivity(new Intent(this, ReceitasActivity.class));
     }

     public void configuraCalendario(){

          CharSequence meses[] = {"Janeiro","Fevereiro", "Março","Abril","Maio",
                  "Junho","Julho","Agosto","Setembro","Outubro",
                  "Novembro","Dezembro"};
          calendarView.setTitleMonths( meses );

          //Altera titulo dos dias da semana
          CharSequence semanas[] = {"Segunda", "Terça","Quarta",
                  "Quinta","Sexta","Sábado","Domingo"};
          calendarView.setWeekDayLabels(semanas);
     }

     public void recuperaResumo(){
          String emailusuario = auth.getCurrentUser().getEmail();//Pega o email do usuario atual
          String idUsuario = Base64Custon.codificarBase64(emailusuario);//Criptografa pegando assim o id do usuario
          referenceUsuario = database.child("Usuários").child(idUsuario);

          //Cancelar evento listner
          Log.i("onStop", "Evento adcionado!");
          valueEventListenerUsuario = referenceUsuario.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario usuario = snapshot.getValue(Usuario.class);//Pega os dados na forma de obj usuario

                    //Pegando do bando
                    despesaTotal = usuario.getDespesaTotal();
                    receitaTotal = usuario.getReceitaTotal();
                    resumoUsuario = receitaTotal - despesaTotal;

                    DecimalFormat decimalFormat = new DecimalFormat("#.##");//duas casas decimais, sem o zero
                    String resultadoFormatado = decimalFormat.format(resumoUsuario);

                    //Setando para View
                    textSaldacao.setText("Olá, " + usuario.getNome());
                    textSaldo.setText("R$: " + resultadoFormatado);

               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
          });
     }

     @Override
     protected void onStart() {
          super.onStart();
          recuperaResumo();//Acessa o banco e paga dados
     }

     @Override
     protected void onStop() {
          super.onStop();
          Log.i("onStop", "Evento removido!");
          referenceUsuario.removeEventListener( valueEventListenerUsuario );//Removendo Listner
     }
}