package com.example.organizzeclone.config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoFirebase {

     private static FirebaseAuth autentificacao;

     //Pegando instacia de FirebaseAuth
     public static FirebaseAuth getFireBaseAutentificacao(){

          if (autentificacao == null)
               autentificacao = FirebaseAuth.getInstance();
          return autentificacao;
     }

}
