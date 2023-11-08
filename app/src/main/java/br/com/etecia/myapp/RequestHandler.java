package br.com.etecia.myapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RequestHandler {

    public String sendPostRequest(String requestURL,
                                  HashMap<String,String> postDataParams){

     //Criação da URL
        URL url;

     //StringBuilder objeto que serve para armazenar a mensagem recuperada do servidor
     StringBuilder sb = new StringBuilder();
     try{
         //Iniciando URL
         url = new URL(requestURL);

         HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

         conn.setReadTimeout(15000);
         conn.setConnectTimeout(15000);
         conn.setRequestMethod("POST");
         conn.setDoInput(true);
         conn.setDoOutput(true);

         OutputStream os = conn.getOutputStream();

         BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os,"UTF-8"));
         writer.write(getPostDataString(postDataParams));

         writer.flush();
         writer.close();
         os.close();
         int responseCode = conn.getResponseCode();

         if (responseCode == HttpsURLConnection.HTTP_OK){
             BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
             sb = new StringBuilder();
             String response;

             while ((response = br.readLine())!= null){
                 sb.append(response);
             }
         }
     }catch (Exception e){
         e.printStackTrace();
     }
     return sb.toString();
    }
    public String sendGetRequest(String requestURL){
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null){
                sb.append(s + "\n");
            }
        }catch (Exception e){
        }
        return  sb.toString();
    }
    private String getPostDataString(HashMap<String,String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        return result.toString();
    }
}
