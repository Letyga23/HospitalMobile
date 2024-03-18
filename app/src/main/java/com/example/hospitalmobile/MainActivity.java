package com.example.hospitalmobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HospitalAdapter mAdapter;
    private List<Patient> mPatientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listViewHospitals = findViewById(R.id.listViewPacients);
        mAdapter = new HospitalAdapter(MainActivity.this, mPatientList);
        listViewHospitals.setAdapter(mAdapter);

        findViewById(R.id.LoadButton).setOnClickListener(v -> loadData());
    }

    private void loadData()
    {
        Toast.makeText(getApplicationContext(), "Данные загружаются", Toast.LENGTH_SHORT).show();
        mPatientList.clear();
        mAdapter.notifyDataSetChanged();
        new GetPatients().execute();
    }

    private class GetPatients  extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            try
            {
                URL url = new URL("http://192.168.0.103:5181/Patient");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }

                return result.toString();
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try
            {
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject hospitalJson = jsonArray.getJSONObject(i);
                    Patient tempPatient;
                    tempPatient = new Patient(
                            hospitalJson.getInt("id_Patient"),
                            hospitalJson.getString("lastName"),
                            hospitalJson.getString("firstName"),
                            hospitalJson.getString("patronymic"),
                            hospitalJson.getString("passportData"),
                            hospitalJson.getString("dateOfBirth"),
                            hospitalJson.getString("gender"),
                            hospitalJson.getString("address"),
                            hospitalJson.getString("phoneNumber"),
                            hospitalJson.getString("email"),
                            hospitalJson.getString("photo"));

                    mPatientList.add(tempPatient);
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), "Данные получены", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(), "Ошибка получения данных: " + ex.getMessage() , Toast.LENGTH_LONG).show();
            }
        }
    }
}