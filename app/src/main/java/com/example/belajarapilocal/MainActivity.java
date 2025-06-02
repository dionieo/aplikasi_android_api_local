package com.example.belajarapilocal;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editName, editEmail;
    private String alamat = "http://192.168.20.168/aslab_belajar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();

                new SubmitDataTask().execute(name, email);
            }
        });
    }

    private class SubmitDataTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String email = params[1];
            try {
                URL url = new
                        URL(alamat + "/insert_data.php");
                HttpURLConnection connection = (HttpURLConnection)
                        url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String data = "name=" + name + "&email=" + email;

                OutputStream os = connection.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Data berhasil disimpan!";
                } else {
                    return "Gagal menyimpan data!";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(MainActivity.this, result,
                    Toast.LENGTH_SHORT).show();
        }
    }
}