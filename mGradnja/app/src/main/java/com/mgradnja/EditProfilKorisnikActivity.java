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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditProfilKorisnikActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    EditText txtImeEditKorisnika, txtPrezimeEditKorisnika, txtTelefonEditKorisnik, txtMailEditKorisnik, txtLozinkaEditKorisnik, txtPlozinkaEditKorisnik;
    ImageView imgProfilnaEditKorisnik;
    Button btnSpremiEditKorisnika, btnEdtiProfilneSlikeKorisnik;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_korisnik);

        connectionClass = new ConnectionClass();

        txtImeEditKorisnika = (EditText) findViewById(R.id.txtEditKorisnikIme);
        txtPrezimeEditKorisnika = (EditText) findViewById(R.id.txtEditKorisnikPrezime);
        txtMailEditKorisnik = (EditText) findViewById(R.id.txtEditKorisnikMail);
        txtTelefonEditKorisnik = (EditText) findViewById(R.id.txtEditKorisnikTelefon);
        txtLozinkaEditKorisnik = (EditText) findViewById(R.id.txtEditKorisnikLozinka);
        txtPlozinkaEditKorisnik = (EditText) findViewById(R.id.txtEditKorisnikPlozinka);
        imgProfilnaEditKorisnik = (ImageView) findViewById(R.id.imgEditKorisnik);
        btnSpremiEditKorisnika = (Button) findViewById(R.id.btnEditKorisnikSpremi);
        btnEdtiProfilneSlikeKorisnik = (Button) findViewById(R.id.btnEditKorisnikUploadSlike);


        //DOHVAT ID-A KORISNIKA IZ PRIJAŠNJE AKTIVNOSTI
        Intent intent = getIntent();
        Integer ID = intent.getIntExtra("ID_korisnika", 0);

        dohvatPodatakaEditKorisnik(ID);

        btnEdtiProfilneSlikeKorisnik.setOnClickListener(v -> ActivityCompat.requestPermissions(
                EditProfilKorisnikActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        ));

        btnSpremiEditKorisnika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spremiEditKorisnik(ID);
            }
        });

    }

    public void dohvatPodatakaEditKorisnik(Integer ID){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();


        String sql = "SELECT * from Korisnik WHERE ID_korisnika=('" + ID + "')";

        try{
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                txtImeEditKorisnika.setText(rs.getString("Ime"));
                txtPrezimeEditKorisnika.setText(rs.getString("Prezime"));
                txtMailEditKorisnik.setText(rs.getString("Mail"));
                txtTelefonEditKorisnik.setText(rs.getString("Telefon"));
                txtLozinkaEditKorisnik.setText(rs.getString("Lozinka"));
                txtPlozinkaEditKorisnik.setText(rs.getString("Lozinka"));
                imgProfilnaEditKorisnik.setImageResource(rs.getByte("Slika"));

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void spremiEditKorisnik(Integer ID){

        String ime = txtImeEditKorisnika.getText().toString();
        String prezime = txtPrezimeEditKorisnika.getText().toString();
        String mail = txtMailEditKorisnik.getText().toString();
        String telefon = txtTelefonEditKorisnik.getText().toString();
        String lozinka = txtLozinkaEditKorisnik.getText().toString();
        String pLozinka = txtPlozinkaEditKorisnik.getText().toString();
        byte[] slika = imageViewToByteKorisnik(imgProfilnaEditKorisnik);

        if (ime.equals("") || prezime.equals("") || mail.equals("") || telefon.equals("") || lozinka.equals("") || pLozinka.equals("")){
            Toast.makeText(getApplicationContext(), "Niste ispunili sve potrebne podatke!", Toast.LENGTH_SHORT).show();
        }
        else{
            if (lozinka.equals(pLozinka)){
                try {
                    Connection con = connectionClass.CONN();

                    if (con == null){
                        Toast.makeText(getApplicationContext(), "Greška sa serverom, pokušajte kasnije!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Statement st = con.createStatement();

                        String queriEditKorisnik = "UPDATE Korisnik SET Ime = ('"+ ime +"'), Prezime  = ('"+ prezime +"'), Telefon = ('"+ telefon +"'), Mail = ('"+ mail +"'), Lozinka = ('"+ lozinka +"'), Slika = CAST(('"+ slika +"') as varBinary(Max)) WHERE ID_korisnika=('" + ID + "')";

                        if (st.executeUpdate(queriEditKorisnik) == 1){
                            Toast.makeText(getApplicationContext(), "Podaci su uspješno promijenjeni!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Greška prilikom pohrane, pokušajte ponovno!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Lozinka i ponovljena lozinka se ne poklapaju!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static byte[] imageViewToByteKorisnik(ImageView image){
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
            }else{
                Toast.makeText(getApplicationContext(), "Nemate prava", Toast.LENGTH_SHORT).show();
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
                imgProfilnaEditKorisnik.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
