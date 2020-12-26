package com.example.organizzeclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button buttonCadastrar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializandoComponentesView();

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = campoNome.getText().toString();
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!nome.isEmpty()){
                    if (!email.isEmpty()){
                        if (!senha.isEmpty()){

                            Usuario usuario = new Usuario();//Instancia obj usuario
                            usuario.setNome(nome);
                            usuario.setEmail(email);
                            usuario.setSenha(senha);
                            cadastroUsuario(usuario);//Autenticando com fireBase

                        } else {
                            Toast.makeText(CadastroActivity.this,
                                    "Informe a Senha!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CadastroActivity.this,
                                "Informe o Email!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroActivity.this,
                            "Informe o Nome!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void inicializandoComponentesView(){
        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
    }

    public void cadastroUsuario(Usuario usuario){

        //Classe interna que recupera as instancias do fireBase
        auth = ConfiguracaoFirebase.getFireBaseAutentificacao();//Pegando a instancia
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()/*Validando*/
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {//A variavel task traz o resultado da validacao (isSuccessful())

                if (task.isSuccessful()){
                    finish();//Volta para os slides, que já direciona para a PrincipalActivity.class
                    Toast.makeText(CadastroActivity.this,
                            "Sucesso ao cadastrar usuario!", Toast.LENGTH_SHORT).show();
                }else {

                    String excecao = "";
                    try {//Tratando possiveis excecoes de acordo com a documentacao
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Informe uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Informe um Email válido!";
                    } catch (FirebaseAuthUserCollisionException e){
                        excecao = "Email já cadastrado, informe outro!";
                    } catch (Exception e){
                        excecao = "Erro ao cadastrar usuario: " + e.getMessage();//Pega a msg da excecao
                        e.printStackTrace();//Printar excessao no log
                    }

                    //Exibindo excecao
                    Toast.makeText(CadastroActivity.this,
                            excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}