package com.example.organizzeclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.organizzeclone.adapter.AdapterMovimentacao;
import com.example.organizzeclone.config.ConfiguracaoFirebase;
import com.example.organizzeclone.helper.Base64Custon;
import com.example.organizzeclone.model.Movimentacao;
import com.example.organizzeclone.model.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizzeclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

     private MaterialCalendarView calendarView;
     private TextView textSaldacao, textSaldo;
     private Double despesaTotal   = 0.0,
                    receitaTotal   = 0.0,
                    resumoUsuario  = 0.0;

     private RecyclerView recyclerView;
     private AdapterMovimentacao adapterMovimentacao;
     private List<Movimentacao> movimentacoes = new ArrayList<>();
     private Movimentacao movimentacao;
     private String mesAnoSelecionado;

     private FirebaseAuth auth = ConfiguracaoFirebase.getFireBaseAutentificacao();
     private DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
     private DatabaseReference usuarioRef;//Para fechar evento de Listner
     private DatabaseReference movimentacaoRef;
     private ValueEventListener valueEventListenerUsuario;//Para fechar evento de Listner
     private ValueEventListener valueEventListenerMovimentacoes;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_principal);

          Toolbar toolbar = findViewById(R.id.toolbar);
          toolbar.setTitle("");
          setSupportActionBar(toolbar);

          //View
          calendarView   = findViewById(R.id.calendarView);
          textSaldacao   = findViewById(R.id.textSaudacao);
          textSaldo      = findViewById(R.id.textSaldo);
          recyclerView   = findViewById(R.id.recyclerMovimentos);

          configuraCalendario();//Configurando calendario
          swipe();//Desliza para apagar/cancelar

          //Configurando adapter
          adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);//Passa o arrayList/context

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

     public void swipe(){//Deslizar para apagar Receitas/Despesas

               ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
               @Override//Configura o modo da movimentacao
               public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                    //Cinfigurando movimentacao
                    int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;//Movimento para todos os lados inativo
                    int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;//Arrasta no comeco e no final
                    return makeMovementFlags(dragFlags, swipeFlags);
               }

               @Override
               public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
               }

               @Override//Informa se ouve movimentacao
               public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    Log.i("swipe","item arrastado");
                    excluirItemMovido(viewHolder);//Pega o item que foi movido na view -> index do array movimentacoes
               }
          };
          new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);//Adcionando movimentacao no recyclerView
     }

     public void excluirItemMovido(RecyclerView.ViewHolder viewHolder){

          AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

          alertDialog.setTitle("Excluir movimentação da conta...");
          alertDialog.setMessage("Você deseja excluir esta movimentação de conta ?");
          alertDialog.setCancelable(false);//Somente sai se escolher uma opcao

          alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {//Listnner para click
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {

                    int position = viewHolder.getAdapterPosition();//Index de cards(array) na view do adapter
                    movimentacao = movimentacoes.get(position);

                    String emailusuario = auth.getCurrentUser().getEmail();//Pega o email do usuario atual
                    String idUsuario = Base64Custon.codificarBase64(emailusuario);//Criptografa pegando assim o id do usuario

                    movimentacaoRef = database.child("movimentacao")//Acessando banco de dados
                            .child(idUsuario)
                            .child(mesAnoSelecionado);

                    movimentacaoRef.child(movimentacao.getKey()).removeValue();
                    adapterMovimentacao.notifyItemRemoved(position);//Chama o onBind novamente para tirar item removido
                    atualizarSaldo();
               }
          });

          alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(PrincipalActivity.this, "Cancelado ", Toast.LENGTH_SHORT).show();
                    adapterMovimentacao.notifyDataSetChanged();//Chamasse o onbind novamente
               }
          });

          alertDialog.create();
          alertDialog.show();
     }

     public void atualizarSaldo(){

          String emailusuario = auth.getCurrentUser().getEmail();//Pega o email do usuario atual
          String idUsuario = Base64Custon.codificarBase64(emailusuario);//Criptografa pegando assim o id do usuario

          if (movimentacao.getTipo().equals("R")){
               receitaTotal = receitaTotal - movimentacao.getValor();
               usuarioRef.child("receitaTotal").setValue(receitaTotal);
          }

          if (movimentacao.getTipo().equals("D")){
               despesaTotal = despesaTotal - movimentacao.getValor();
               usuarioRef.child("despesaTotal").setValue(despesaTotal);
          }

     }

     public void recuperarMovimentacoes(){
          String emailusuario = auth.getCurrentUser().getEmail();//Pega o email do usuario atual
          String idUsuario = Base64Custon.codificarBase64(emailusuario);//Criptografa pegando assim o id do usuario

          movimentacaoRef = database.child("movimentacao")//Acessando banco de dados
                                   .child(idUsuario)
                                   .child(mesAnoSelecionado);

          valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {

                    movimentacoes.clear();//Limpa movimentacoes para novas consultas
                    for (DataSnapshot dados : snapshot.getChildren()){

                         Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                         movimentacao.setKey(dados.getKey());//Pega a chave das movimentacoes -> Push();
                         movimentacoes.add(movimentacao);
                    }
                    adapterMovimentacao.notifyDataSetChanged();//Informa ao adapter que os dados mudaram - como se chamasse o onbind
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
          });
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

          //Meses em Portugues
          CharSequence meses[] = {"Janeiro","Fevereiro", "Março","Abril","Maio",
                                  "Junho","Julho","Agosto","Setembro","Outubro",
                                  "Novembro","Dezembro"};
          calendarView.setTitleMonths( meses );

          CalendarDay dataAtual = calendarView.getCurrentDate();//Data atual
          String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1));//Dois digitos, completarcom zero
          mesAnoSelecionado = ( mesSelecionado + "" + dataAtual.getYear());//Data atual corrigida

          //So entra quando tiver uma mudanca no calendario
          calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
               @Override
               public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                    String mesSelecionado = String.format("%02d", (date.getMonth() + 1));//Dois digitos, completarcom zero
                    mesAnoSelecionado = mesSelecionado + "" + date.getYear();//Data movimentada no calendario

                    movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                    recuperarMovimentacoes();
               }
          });
     }

     public void recuperaResumo(){

          String emailusuario = auth.getCurrentUser().getEmail();//Pega o email do usuario atual
          String idUsuario = Base64Custon.codificarBase64(emailusuario);//Criptografa pegando assim o id do usuario
          usuarioRef = database.child("Usuários").child(idUsuario);

          //Cancelar evento listner
          valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
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
          recuperarMovimentacoes();//Movimentacao do calendario -> movimentacoes no banco
     }

     @Override
     protected void onStop() {
          super.onStop();
          usuarioRef.removeEventListener( valueEventListenerUsuario );//Removendo Listner
          movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);//Removendo
     }
}