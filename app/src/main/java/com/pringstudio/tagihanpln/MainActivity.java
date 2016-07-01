package com.pringstudio.tagihanpln;

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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    // UI Component
    Button btnCheck;
    EditText inputIDPel;

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

        // Init HTTP Client
        client = new AsyncHttpClient();

        // Add Listener
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Content Value
                String idpel = inputIDPel.getText().toString();

                // Make Api Request
                // GET http://ibacor.com/api/tagihan-pln?idp=...&thn=...&bln=..
                String apiUrl = "http://ibacor.com/api/tagihan-pln?idp=515040969459&thn=2016&bln=06";
                client.get(apiUrl, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Will Return Message on Debug Logcat
                        // D/onSuccess: Resp: {"status":"success","query":{"id_pelanggan":"515040969459","tahun":"2016","bulan":"06"},"data":{"diskon":"0","angsuran":"ppn","thblrek":"201606","lwbp":"119825","beban":"18000","ketlunas":"08 Juni 2016","fakmkvam":"fakm","bpju":"13783","ptl":"137825","idpel":"515040969459","sahlwbp":"80051000","tglbacalalu":"22-04-2016","slawbp":"sewa","nama":"TOMO    ","namaupi":"JAWA TIMUR","daya":"900","fjn":"N","jamnyala":"mat","slalwbp":"77498000","pemkwh":"255","tagihan":"151608","namathblrek":"Juni 2016","terbilang":"Seratus Lima Puluh Satu Ribu Enam Ratus Delapan Rupiah ","wbp":"kvarh","alamat":"DS SIRIGAN No.0 RT.006 RW.01 SIRIGAN ","tglbacaakhir":"23-05-2016","tarif":"R1"}}
                        Log.d("onSuccess","Resp: "+response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("onFailure","Request Gagal "+throwable.getMessage());
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
