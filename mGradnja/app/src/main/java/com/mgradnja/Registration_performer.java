package com.mgradnja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

public class Registration_performer extends AppCompatActivity {

    ConnectionClass connectionClass;
    Button registracija, natragNaPrijavu, uploadSlike;
    EditText txtOIB, txtNaziv, txtAdresa, txtGrad, txtZupanija, txtTelefon, txtMail, txtBrRacuna, txtLozinka, txtPlozinka;
    Spinner idPravniStatus;
    ImageView imageView;
    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_performer);

        connectionClass = new ConnectionClass();

        Spinner pravniStatus = findViewById(R.id.pravniStatus);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(Registration_performer.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.pravniStatusi));
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pravniStatus.setAdapter(statusAdapter);

        imageView = findViewById(R.id.profilnaSlika);
        idPravniStatus = findViewById(R.id.pravniStatus);
        txtOIB = findViewById(R.id.txtOib);
        txtNaziv = findViewById(R.id.txtNaziv);
        txtAdresa = findViewById(R.id.txtAdresa);
        txtGrad = findViewById(R.id.txtCijenaPonude);
        txtZupanija = findViewById(R.id.txtZupanija);
        txtTelefon = findViewById(R.id.txtTelefon);
        txtMail = findViewById(R.id.txtMail);
        txtBrRacuna = findViewById(R.id.txtBrojRacuna);
        txtLozinka = findViewById(R.id.txtLozinka);
        txtPlozinka = findViewById(R.id.txtPlozinka);
        uploadSlike = findViewById(R.id.btnUploadSlike);
        registracija = findViewById(R.id.btnRegistracija);
        natragNaPrijavu = findViewById(R.id.btnNatragNaPrijavu);

        uploadSlike.setOnClickListener(v -> ActivityCompat.requestPermissions(
                Registration_performer.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        ));

        registracija.setOnClickListener(v -> doRegistration());
        natragNaPrijavu.setOnClickListener(v -> backToLogin());

    }

    public void doRegistration(){
        Long idPravnogStatusa = idPravniStatus.getSelectedItemId();
        String oib = txtOIB.getText().toString();
        String naziv = txtNaziv.getText().toString();
        String adresa = txtAdresa.getText().toString();
        String grad = txtGrad.getText().toString();
        String zupanija = txtZupanija.getText().toString();
        String telefon = txtTelefon.getText().toString();
        String mail = txtMail.getText().toString();
        String brRacuna = txtBrRacuna.getText().toString();
        String lozinka = txtLozinka.getText().toString();
        String plozinka = txtPlozinka.getText().toString();
        //byte[] slika = imageViewToByte(imageView);
        String profilna = Base64.encodeToString(imageViewToByte(imageView), Base64.DEFAULT);


        if (idPravnogStatusa == 0 || oib.equals("") || naziv.equals("") || adresa.equals("") || grad.equals("") || zupanija.equals("") || telefon.equals("") || mail.equals("") || brRacuna.equals("") || lozinka.equals("") || plozinka.equals("")){
            Toast.makeText(getApplicationContext(), "Niste ispunili sve potrebne podatke!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (lozinka.equals(plozinka)){
                try {
                    Connection con = connectionClass.CONN();

                    if (con == null){
                        Toast.makeText(getApplicationContext(), "Greška sa serverom, pokušajte kasnije!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Statement st = con.createStatement();

                        //CAST(('"+ slika +"') as varBinary(Max))
                        String queryReg = "INSERT INTO Izvodjac(OIB, Naziv, Adresa, Grad, Zupanija, Telefon, Mail, Lozinka, Broj_racuna, Slika, ID_pravnog_statusa) " +
                                "VALUES(('"+ oib +"'), ('"+ naziv +"'), ('"+ adresa +"'), ('"+ grad +"'), ('"+ zupanija +"'), ('"+ telefon +"'), ('"+ mail +"'), ('"+ lozinka +"'), ('"+ brRacuna +"'), ('"+ profilna +"'), ('"+ idPravnogStatusa +"'))";

                        if (st.executeUpdate(queryReg) == 1){
                            Toast.makeText(getApplicationContext(), "Registracija uspješna, možete se prijaviti!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Greška prilikom registracije, pokušajte ponovno!", Toast.LENGTH_LONG).show();
                        }

                        con.close();

                    }
                }
                catch (Exception ex){
                    String porukaIznimke;
                    porukaIznimke = ex.getMessage();
                    Toast.makeText(getApplicationContext(), porukaIznimke, Toast.LENGTH_LONG).show();
                }

            }
            else {
                Toast.makeText(getApplicationContext(), "Lozinka i potvrda lozinke se ne poklapaju!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void backToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "Nemate prava pristupa", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GALLERY){
            Uri uri = data.getData();

            try{
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
