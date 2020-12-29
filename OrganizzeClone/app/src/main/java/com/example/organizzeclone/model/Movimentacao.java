package com.example.organizzeclone.model;

import com.example.organizzeclone.config.ConfiguracaoFirebase;
import com.example.organizzeclone.helper.Base64Custon;
import com.example.organizzeclone.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

     private String data, categoria, descricao, tipo;
     private Double valor;

     public Movimentacao() {//Contrutor
     }

     public void salvar(String dataEscolhida){

          FirebaseAuth auth = ConfiguracaoFirebase.getFireBaseAutentificacao();
          String usuarioId = Base64Custon.codificarBase64(auth.getCurrentUser().getEmail());//Criptografando email/Id no firebase

          String mesAno = DateCustom.mesAno(dataEscolhida);//Convertida para mes-ano 122020

          DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();//Pega a instacia do banco
          database.child("movimentacao")//Inserindo nos no banco
                  .child(usuarioId)//Email criptografado Base64Custon
                  .child(mesAno)//Data escolhida
                  .push()//Id do FireBase criptografado
                  .setValue(this);//Passa o obj para ser salvo no banco
     }

     public String getData() {
          return data;
     }

     public void setData(String data) {
          this.data = data;
     }

     public String getCategoria() {
          return categoria;
     }

     public void setCategoria(String categoria) {
          this.categoria = categoria;
     }

     public String getDescricao() {
          return descricao;
     }

     public void setDescricao(String descricao) {
          this.descricao = descricao;
     }

     public String getTipo() {
          return tipo;
     }

     public void setTipo(String tipo) {
          this.tipo = tipo;
     }

     public Double getValor() {
          return valor;
     }

     public void setValor(Double valor) {
          this.valor = valor;
     }
}
