package com.example.hospitalmobile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
//        new GetPatients().execute();

        ApiManager.getData("Get").thenAccept(jsonArray -> {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject hospitalJson = jsonArray.getJSONObject(i);
                    Patient tempPatient = new Patient(
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
                            hospitalJson.getString("patientPhoto"));

                    mPatientList.add(tempPatient);
                }

                runOnUiThread(() -> {
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Данные получены", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception ex) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Ошибка получения данных: " + ex.getMessage(), Toast.LENGTH_LONG).show());
                runOnUiThread(() -> Log.d("TAG", "Ошибка получения данных: " + ex.getMessage()));
            }
        });
    }
}