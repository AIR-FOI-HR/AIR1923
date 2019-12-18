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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

public class Registration_user extends AppCompatActivity {

    ConnectionClass connectionClass;
    Button registracija, natragNaPrijavu, uploadSlike;
    EditText txtIme, txtPrezime, txtMail, txtTelefon, txtLozinka, txtPlozinka;
    ImageView imageView;
    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_user);

        connectionClass = new ConnectionClass();

        imageView = findViewById(R.id.profilnaSlika);
        txtIme = findViewById(R.id.txtIme);
        txtPrezime = findViewById(R.id.txtPrezime);
        txtMail = findViewById(R.id.txtMail);
        txtTelefon = findViewById(R.id.txtTelefon);
        txtLozinka = findViewById(R.id.txtLozinka);
        txtPlozinka = findViewById(R.id.txtPlozinka);
        uploadSlike = findViewById(R.id.btnUploadSlike);
        registracija = findViewById(R.id.btnRegistracija);
        natragNaPrijavu = findViewById(R.id.btnNatragNaPrijavu);

        uploadSlike.setOnClickListener(v -> ActivityCompat.requestPermissions(
                Registration_user.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        ));

        registracija.setOnClickListener(v -> doRegistration());
        natragNaPrijavu.setOnClickListener(v -> backToLogin());

    }

    public void doRegistration(){
        String ime = txtIme.getText().toString();
        String prezime = txtPrezime.getText().toString();
        String mail = txtMail.getText().toString();
        String telefon = txtTelefon.getText().toString();
        String lozinka = txtLozinka.getText().toString();
        String plozinka = txtPlozinka.getText().toString();
        byte[] slika = imageViewToByte(imageView);

        if (ime.equals("") || prezime.equals("") || mail.equals("") || telefon.equals("") || lozinka.equals("") || plozinka.equals("")){
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

                        String queryReg = "INSERT INTO Korisnik(Ime, Prezime, Mail, Lozinka, Telefon, Slika) " +
                                "VALUES(('"+ ime +"'), ('"+ prezime +"'), ('"+ mail +"'), ('"+ lozinka +"'), ('"+ telefon +"'), CAST(('"+ slika +"') as varBinary(Max)))";

                        if (st.executeUpdate(queryReg) == 1){
                            Toast.makeText(getApplicationContext(), "Registracija uspješna, možete se prijaviti!", Toast.LENGTH_LONG).show();
                            txtIme.setText("");
                            txtPrezime.setText("");
                            txtMail.setText("");
                            txtTelefon.setText("");
                            txtLozinka.setText("");
                            txtPlozinka.setText("");
                            imageView.setImageResource(R.drawable.registracija);
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
                imageView.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
