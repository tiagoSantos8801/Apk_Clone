package com.example.organizzeclone.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

     private static FirebaseAuth autentificacao;
     private static DatabaseReference firebase;

     //Retorna a instancia do firebaseDataBase
     public static DatabaseReference getFirebaseDatabase(){
          if (firebase == null){
               firebase = FirebaseDatabase.getInstance("https://organizzeclone-5ac40-default-rtdb.firebaseio.com/")
                                                       .getReference();
          }
          return firebase;
     }

     //Retorna a instacia de FirebaseAuth
     public static FirebaseAuth getFireBaseAutentificacao(){

          if (autentificacao == null)
               autentificacao = FirebaseAuth.getInstance();
          return autentificacao;
     }

}
