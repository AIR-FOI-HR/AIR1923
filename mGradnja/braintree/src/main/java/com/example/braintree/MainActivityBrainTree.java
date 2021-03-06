package com.example.braintree;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.posadskiy.currencyconverter.Currency;
import com.posadskiy.currencyconverter.CurrencyConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivityBrainTree extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    //IP na XAMPP Apache-u je 127.0.0.1 ali je u AS njegov emulator na tom IP-ju pa treba koristit 10.0.2.2
    //sandbox username: mvecenaj, pass: mvecenaj1

    final String API_GET_TOKEN = "http://10.0.2.2/braintree/main.php";
    final String API_CHECK_OUT = "http://10.0.2.2/braintree/checkout.php";

    ConnectionClass connectionClass= new ConnectionClass();
    Connection con = connectionClass.CONN();

    //currency converter jer Braintree radi s Eurima tj nema kune...
    CurrencyConverter converter = new CurrencyConverter("f85d4af962abb27ccc47");
    Double eurToHrk = converter.rateFromEuro(Currency.HRK);

    String token, amount;
    Double iznos, cijena;
    Integer ID_upita, ID_korisnika;
    HashMap<String, String> paramsHash;

    Button btn_pay;
    TextView euri, naslov;
    EditText edt_amount;
    LinearLayout group_waiting, group_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_braintree);
        Intent intent = getIntent();
        ID_upita = intent.getIntExtra("BrojUpita", 0);
        ID_korisnika = intent.getIntExtra("ID_korisnika", 0);
        setTitle("Plaćanje posla");


        String query = "Select cijena From ponuda Where ponuda.Status = 1 and ponuda.ID_upita='"+ ID_upita+"'";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                cijena = rs.getDouble("cijena");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query2 = "Select naziv From upit Where ID_upita='"+ID_upita+"'";
        try {
            Statement statement2 = con.createStatement();
            ResultSet rs2 = statement2.executeQuery(query2);

            while(rs2.next()){
                naslov = findViewById(R.id.naslov);
                naslov.setText(rs2.getString("naziv"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        group_payment = findViewById(R.id.payment_group);
        group_waiting = findViewById(R.id.waiting_group);
        btn_pay = findViewById(R.id.btn_pay);
        euri = findViewById(R.id.euri);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c);

        DecimalFormat precision = new DecimalFormat("0.00");
        edt_amount = findViewById(R.id.amount);
        edt_amount.setEnabled(false);
        edt_amount.setText(precision.format(cijena/eurToHrk));
        euri.setText("Tečajna rata na dan " +formattedDate +" iznosi " + precision.format(eurToHrk) );
        btn_pay.setText("Plati "+ edt_amount.getText().toString() + " €");

        new getToken().execute();

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submintPayment();
            }
        });
    }

    private void submintPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if (!edt_amount.getText().toString().isEmpty()) {
                    amount = edt_amount.getText().toString();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount", amount);
                    paramsHash.put("nonce", strNonce);

                    sendPayments();
                } else {
                    Toast.makeText(this, "Unesite odgovarajući iznos", Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Otkazano", Toast.LENGTH_SHORT).show();
            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("MOJ_ERROR", error.toString());

            }
        }
    }

    private void sendPayments() {
        RequestQueue queue = Volley.newRequestQueue(MainActivityBrainTree.this);
        con = connectionClass.CONN();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().contains("Successful")){

                            String query = "Update posao Set Placeno=1 Where ID_upita='" + ID_upita + "'";
                            try {
                               Statement statement = con.createStatement();
                               ResultSet rs = statement.executeQuery(query);

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MainActivityBrainTree.this,"Transakcija uspješna!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.putExtra("ID_korisnika", ID_korisnika);
                            intent.setComponent(new ComponentName("com.mgradnja","com.mgradnja.GlavniIzbornikKorisnik"));
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivityBrainTree.this, "Transakcija neuspješna", Toast.LENGTH_LONG).show();
                            Log.d("MOJ_ERROR", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MOJ_ERROR", error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(paramsHash == null){
                    return null;
                }
                Map<String, String> params = new HashMap<>();
                for (String key:paramsHash.keySet()){
                    params.put(key,paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        } ;

        queue.add(stringRequest);
    }

    private class getToken extends AsyncTask {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(MainActivityBrainTree.this, android.R.style.Theme_DeviceDefault_Dialog);
            mDialog.setCancelable(false);
            mDialog.setMessage("Molimo pričekajte");
            mDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new  HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //sakriti grupu za cekanje
                            group_waiting.setVisibility(View.GONE);
                            group_payment.setVisibility(View.VISIBLE);

                            //postavi token
                            token = responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Log.d("MOJ_ERROR", exception.toString());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mDialog.dismiss();
        }


    }
}
