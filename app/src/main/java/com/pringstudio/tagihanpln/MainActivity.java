package com.pringstudio.tagihanpln;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    // UI Component
    Button btnCheck;
    EditText inputIDPel;
    EditText inputBulan;
    EditText inputTahun;
    TextView judulHasil;
    TextView contentHasil;

    // Progress Dialog
    ProgressDialog pgDialog;

    // HTTP Client
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get From XML
        btnCheck = (Button) findViewById(R.id.tombol_check);
        inputIDPel = (EditText) findViewById(R.id.input_idpel);
        inputBulan = (EditText) findViewById(R.id.input_bulan);
        inputTahun = (EditText) findViewById(R.id.input_tahun);
        judulHasil = (TextView) findViewById(R.id.judul_hasil);
        contentHasil = (TextView) findViewById(R.id.content_hasil);

        // Init HTTP Client
        client = new AsyncHttpClient();

        // Init Progress Dialog
        pgDialog = new ProgressDialog(this);
        pgDialog.setIndeterminate(true);
        pgDialog.setCancelable(false);

        // Add Listener
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Content Value
                String idpel = inputIDPel.getText().toString();
                String bulan = inputBulan.getText().toString();
                String tahun = inputTahun.getText().toString();

                // Validation
                if(idpel.length() != 12){
                    Toast.makeText(getApplicationContext(),"Masukkan data ID Pelanggan dengan benar\n12 Digit",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(bulan.length() != 2){
                    Toast.makeText(getApplicationContext(),"Masukkan bulan dengan benar\n 2 Digit",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(tahun.length() != 4){
                    Toast.makeText(getApplicationContext(),"Masukkan tahun dengan benar\n4 Digit",Toast.LENGTH_SHORT).show();
                    return;
                }



                // Make Api Request
                // GET http://ibacor.com/api/tagihan-pln?idp=...&thn=...&bln=..
                String apiUrl = "http://ibacor.com/api/tagihan-pln?idp="+idpel+"&thn="+tahun+"&bln="+bulan;
                client.get(apiUrl, new JsonHttpResponseHandler(){

                    @Override
                    public void onStart() {
                        // Change Content Title to Loading... when process start
                        judulHasil.setText(R.string.lading);
                        pgDialog.setMessage("Mohon tunggu...");
                        pgDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("onSuccess", "onSuccess: "+response.toString());

                        try {
                            String status = response.getString("status");
                            if (status.equals("success")){
                                Gson gson = new GsonBuilder().create();
                                Data data = gson.fromJson(response.getJSONObject("data").toString(), Data.class);


                                judulHasil.setText("Tagihan untuk bulan "+data.getNamathblrek());
                                contentHasil.setText("Nominal : Rp."+data.getTagihan()+"\nNama: "+data.getNama());


                                Log.d("DataDebug","Data : "+data.getTerbilang());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }



//                        try{
//                            String textBulan = response.getJSONObject("data").getString("namathblrek");
//                            String nominal = response.getJSONObject("data").getString("tagihan");
//                            judulHasil.setText("Tagihan untuk bulan "+textBulan);
//                            contentHasil.setText("Nominal yang harus dibayar : Rp."+nominal);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
                        pgDialog.hide();
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("onFailure","Request Gagal "+throwable.getMessage());
                        pgDialog.hide();
                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
