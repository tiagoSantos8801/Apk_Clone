package com.example.organizzeclone.model;

import com.example.organizzeclone.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

     //Caracteristicas do obj usuario - model
     private String nome, senha,
                    email, idUsuario;
     private Double despesaTotal = 0.00,
                    receitaTotal = 0.00;

     public Usuario() {

     }

     public void salvarIdUsuarioDB(){
          DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
          firebase.child("Usuários")//Criando nos no banco em tempo real
                  .child(this.idUsuario)
                  .setValue(this);
     }

     public Double getDespesaTotal() {
          return despesaTotal;
     }

     public void setDespesaTotal(Double despesaTotal) {
          this.despesaTotal = despesaTotal;
     }

     public Double getReceitaTotal() {
          return receitaTotal;
     }

     public void setReceitaTotal(Double receitaTotal) {
          this.receitaTotal = receitaTotal;
     }

     @Exclude//Informa que quando for pegar o obj para salva, exclua esse atributo do salvamento
     public String getIdUsuario() {
          return idUsuario;
     }

     public void setIdUsuario(String idUsuario) {
          this.idUsuario = idUsuario;
     }

     public String getNome() {
          return nome;
     }

     public void setNome(String nome) {
          this.nome = nome;
     }

     @Exclude//Fora da apreciacao de salvamento
     public String getSenha() {
          return senha;
     }

     public void setSenha(String senha) {
          this.senha = senha;
     }

     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }
}
