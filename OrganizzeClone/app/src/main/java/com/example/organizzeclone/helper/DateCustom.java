package com.example.organizzeclone.helper;

import android.util.Log;

import java.text.SimpleDateFormat;

public class DateCustom {

     public static String dataAtual(){
          Long data = System.currentTimeMillis();//Data em segundos     //hh:mm:ss tambem
          SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");//Definindo padrao para a formatacao da data
          String dataAtual = dataFormatada.format(data);//Data no formato Long para data formatada

          return dataAtual;
     }

     public static String mesAno(String dataEscolhida){

          String retorno[] = dataEscolhida.split("/");//Onde tem (/) ele quebra
          String dia = retorno[0];
          String mes = retorno[1];
          String ano = retorno[2];

          return mes + ano;
     }
}