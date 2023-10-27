package br.com.etecia.myapp;

import static android.view.View.GONE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//TODO Concertar problema com criação da Card
public class MainActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    ImageView imageViewProduto;
    EditText editTextNome,editTextValor,editId;
    RatingBar ratingBar;
    ProgressBar progressBar;
    ListView listView;
    Button btnAddUpdate;

    List<Wish> wishList;

    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNome = findViewById(R.id.editTextName);
        editTextValor = findViewById(R.id.editTextValor);
        editId = findViewById(R.id.editId);

        ratingBar = findViewById(R.id.ratingBar);

        btnAddUpdate = findViewById(R.id.btnAddUpdate);

        progressBar = findViewById(R.id.progressBar);

        listView = findViewById(R.id.listViewWishes);

        wishList = new ArrayList<>();

        btnAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdating){
                    updateProduto();
                }else {
                    criarProduto();
                }
            }
        });
        getProdutos();
    }
    private void criarProduto(){
        String id = editId.getText().toString();
        String nome = editTextNome.getText().toString().trim();
        float valor = (float) editTextValor.getTextAlignment();

        int rating = (int) ratingBar.getRating();

        if (TextUtils.isEmpty(nome)){
            editTextNome.setError("Por favor entre com o nome");
            editTextNome.requestFocus();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id",id);
        params.put("nome",nome);
        params.put("valor", String.valueOf(valor));
        params.put("rating",String.valueOf(rating));

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_PRODUTO,params,CODE_POST_REQUEST);
        request.execute();

        btnAddUpdate.setText("Adicionar");

        editTextNome.setText("");
        editTextValor.setText("");
        ratingBar.setRating(0);

        isUpdating = false;

    }
    private void deletarProduto(int id){
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_PRODUTO + id,null,CODE_GET_REQUEST);
        request.execute();
    }

    private void getProdutos(){
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_PRODUTO,null, CODE_GET_REQUEST);
        request.execute();
    }

    private void updateProduto(){

        String nome = editTextNome.getText().toString();
        String valor = String.valueOf((float) editTextValor.getTextAlignment());

        int rating = (int) ratingBar.getRating();

        if (TextUtils.isEmpty(nome)){
            editTextNome.setError("Por favor entre com o nome");
            editTextNome.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(valor)){
            editTextValor.setError("Coloque o valor");
            editTextValor.requestFocus();
            return;
        }
        HashMap<String,String> params = new HashMap<>();
        params.put("nome",nome);
        params.put("valor",valor);
        params.put("rating", String.valueOf(rating));

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_PRODUTO,params,CODE_POST_REQUEST);

    }
    private void refreshProdutoList(JSONArray wishlists) throws JSONException{
        wishList.clear();

        for (int i = 0; i <wishlists.length();i++){
            JSONObject obj = wishlists.getJSONObject(i);

            wishList.add(new Wish(
               obj.getInt("id"),
               obj.getString("nome"),
               obj.getInt("valor"),
                    obj.getInt("rating")
            ));
        }
        WishAdapter adapter = new WishAdapter(wishList);
        listView.setAdapter(adapter);

    }
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String>{
        String url;
        HashMap<String,String> params;
        int requestCode;
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshProdutoList(object.getJSONArray("heroes"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url,params);

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);



            return null;
        }
    }
    class WishAdapter extends ArrayAdapter<Wish> {
        List<Wish> wishList;

        public WishAdapter(List<Wish> wishList) {
            super(MainActivity.this, R.layout.wish_list_layout, wishList);
            this.wishList = wishList;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.wish_list_layout, null, true);

            TextView txtViewName = listViewItem.findViewById(R.id.txtTextName);
            TextView txtViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView txtViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Wish wish = wishList.get(position);

            txtViewName.setText(wish.getNome());

            txtViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isUpdating = true;
                    editId.setText(String.valueOf(wish.getId()));
                    editTextNome.setText(wish.getNome());
                    editTextValor.setText(wish.getValor());
                    ratingBar.setRating(wish.getRating());
                    btnAddUpdate.setText("Alterar");
                }
            });
            txtViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Apagar " + wish.getNome())
                        .setMessage("Tem certeza que deseja excluir?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int which) {
                                deletarProduto(wish.getId());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                }
            });
return listViewItem;
}
}
}
