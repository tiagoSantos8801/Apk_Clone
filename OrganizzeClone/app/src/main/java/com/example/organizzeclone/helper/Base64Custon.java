package com.example.organizzeclone.helper;

import android.util.Base64;

public class Base64Custon {//Criptografando Email e Senha

     public static String codificarBase64(String texto){//Codificando
          // - \\r -> Equiparado ao enter  - \\n -> Quebra de linha  --> Substitui pelo vazio
          return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("\\n|\\r","");
     }

     public static String decodificarBase64(String textoCodificado){//Decodificando
          return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
     }

}
