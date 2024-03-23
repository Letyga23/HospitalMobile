package com.example.hospitalmobile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
    private TextView searchText = null;
    private Spinner spinner = null;
    private ListView listViewHospitals = null;

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

        findViewById(R.id.LoadButton).setOnClickListener(v -> loadData());

        findViewById(R.id.addBut).setVisibility(View.INVISIBLE);

        setSearchCategory();

        searchText = findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {search(charSequence.toString());}

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setSearchCategory()
    {
        spinner = findViewById(R.id.SearchCategory);
        List<String> items = new ArrayList<>();
        items.add("Фамилия");
        items.add("Имя");
        items.add("Отчество");

        // Создаем адаптер для Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        // Присоединяем адаптер к Spinner
        spinner.setAdapter(adapter);
    }

    private void addData()
    {
        Intent intentDetails = new Intent(this, DetailsActivity.class);
        startActivity(intentDetails);
    }

    private void loadData()
    {
        listViewHospitals = findViewById(R.id.listViewPacients);
        mAdapter = new HospitalAdapter(MainActivity.this, mPatientList);
        listViewHospitals.setAdapter(mAdapter);

        searchText.setText("");

        Button add = findViewById(R.id.addBut);
        add.setVisibility(View.VISIBLE);
        add.setOnClickListener(v -> addData());

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

    private void search(String searchText) {
        if (mPatientList != null) {
            List<Patient> filteredPatientList = new ArrayList<>();
            String textForComparison = "";

            switch (spinner.getSelectedItemPosition()) {
                case 0:
                    textForComparison = "lastName";
                    break;
                case 1:
                    textForComparison = "firstName";
                    break;
                case 2:
                    textForComparison = "patronymic";
                    break;
            }

            for (Patient patient : mPatientList) {
                String patientField = "";
                switch (textForComparison) {
                    case "lastName":
                        patientField = patient.getLastName();
                        break;
                    case "firstName":
                        patientField = patient.getFirstName();
                        break;
                    case "patronymic":
                        patientField = patient.getPatronymic();
                        break;
                }

                if (patientField.toLowerCase().startsWith(searchText.toLowerCase())) {
                    filteredPatientList.add(patient);
                }
            }

            mAdapter = new HospitalAdapter(MainActivity.this, filteredPatientList);
            listViewHospitals.setAdapter(mAdapter);
        }
    }

}