package com.example.hospitalmobile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Patient currentPatient = getIntent().getParcelableExtra("patient");
        ImageView imageView = findViewById(R.id.photoPacient);
        TextView lastName = findViewById(R.id.LastNameDate);
        TextView firstName = findViewById(R.id.FirstNameDate);
        TextView patronymic = findViewById(R.id.PatronymicDate);
        TextView passportData = findViewById(R.id.PassportDataDate);
        TextView dateOfBirth = findViewById(R.id.DateOfBirthDate);
        TextView gender = findViewById(R.id.GenderDate);
        TextView address = findViewById(R.id.AddressDate);
        TextView phoneNumber = findViewById(R.id.PhoneNumberDate);
        TextView email = findViewById(R.id.EmailDate);

        imageView.setImageBitmap(currentPatient.getBitmapSource());
        lastName.setText(currentPatient.getLastName());
        firstName.setText(currentPatient.getFirstName());
        patronymic.setText(currentPatient.getPatronymic());
        passportData.setText(currentPatient.getPassportData());
        dateOfBirth.setText(currentPatient.getDateOfBirth());
        gender.setText(currentPatient.getGender());
        address.setText(currentPatient.getAddress());
        phoneNumber.setText(currentPatient.getPhoneNumber());
        email.setText(currentPatient.getEmail());
    }
}