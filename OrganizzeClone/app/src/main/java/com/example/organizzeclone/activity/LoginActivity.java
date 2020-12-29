package com.example.organizzeclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizzeclone.R;
import com.example.organizzeclone.config.ConfiguracaoFirebase;
import com.example.organizzeclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button buttonLogar;
    Usuario usuario = new Usuario();//Instancia obj usuario

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        inicializandoComponentesView();

        buttonLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()){
                    if (!senha.isEmpty()){

                        usuario.setEmail(email);
                        usuario.setSenha(senha);
                        cadastroUsuario(usuario);//Autenticando com fireBase

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Informe a Senha!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Informe o Email!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void inicializandoComponentesView(){

        campoEmail = findViewById(R.id.editTextEmail);
        campoSenha = findViewById(R.id.editTextSenha);
        buttonLogar = findViewById(R.id.buttonLogar);

    }

    public void cadastroUsuario(Usuario usuario){

        //Classe interna que recupera as instancias do fireBase
        auth = ConfiguracaoFirebase.getFireBaseAutentificacao();//Pegando a instancia
        auth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()/*Validando*/
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this,
                            "Sucesso ao fazer login!", Toast.LENGTH_SHORT).show();

                } else {

                    String excecao = "";
                    try {//Tratando possiveis excecoes de acordo com a documentacao

                        throw task.getException();//Recupera a excecao de task, e lanca
                    }catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não cadastrado!";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email ou senha não correspondente!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuario: " + e.getMessage();//Pega a msg da excecao
                        e.printStackTrace();//Printar excessao no log
                    }

                    Toast.makeText(LoginActivity.this,
                            excecao, Toast.LENGTH_SHORT).show();//Exibe excecao lancada

                }
            }
        });
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}