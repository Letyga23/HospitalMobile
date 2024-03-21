package com.example.hospitalmobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

public class DetailsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private EditText lastName = null;
    private EditText firstName = null;
    private EditText patronymic = null;
    private EditText passportData = null;
    private EditText dateOfBirth = null;
    private TextView gender = null;
    private TextView address = null;
    private TextView phoneNumber = null;
    private TextView email = null;
    private Bitmap selectedBitmap = null;
    private Button changeBut = null;
    private Patient currentPatient = null;

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

        imageView = findViewById(R.id.photoPacient);
        lastName = findViewById(R.id.LastNameDate);
        firstName = findViewById(R.id.FirstNameDate);
        patronymic = findViewById(R.id.PatronymicDate);
        passportData = findViewById(R.id.PassportDataDate);
        dateOfBirth = findViewById(R.id.DateOfBirthDate);
        gender = findViewById(R.id.GenderDate);
        address = findViewById(R.id.AddressDate);
        phoneNumber = findViewById(R.id.PhoneNumberDate);
        email = findViewById(R.id.EmailDate);
        changeBut = findViewById(R.id.Сhange);


        currentPatient = getIntent().getParcelableExtra("patient");
        if(currentPatient != null)
        {
            blockingEdin(false);
            installingData();
            changeBut.setOnClickListener(v -> changeOn());
        }
        else
            addingModeActive();

    }


    private void installingData()
    {
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

    private void addingModeActive()
    {
        currentPatient = new Patient();
        changeBut.setText("Добавить");
        imageView.setOnClickListener(v -> showConfirmationDialog());
        changeBut.setOnClickListener(v -> addData());
    }

    private void blockingEdin(boolean flag)
    {
        imageView.setEnabled(flag);
        lastName.setEnabled(flag);
        firstName.setEnabled(flag);
        patronymic.setEnabled(flag);
        passportData.setEnabled(flag);
        dateOfBirth.setEnabled(flag);
        gender.setEnabled(flag);
        address.setEnabled(flag);
        phoneNumber.setEnabled(flag);
        email.setEnabled(flag);
    }

    private void clear()
    {
        imageView.setImageResource(0);
        lastName.setText("");
        firstName.setText("");
        patronymic.setText("");
        passportData.setText("");
        dateOfBirth.setText("");
        gender.setText("");
        address.setText("");
        phoneNumber.setText("");
        email.setText("");
    }

    private void changeOn()
    {
        changeBut.setText("Сохранить");
        blockingEdin(true);
        imageView.setOnClickListener(v -> showConfirmationDialog());
        changeBut.setOnClickListener(v -> updateData());
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                showConfirmationDialogAfterSelection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Хотите изменить фото?");

        builder.setPositiveButton("Да", (dialog, id) -> openGallery());

        builder.setNegativeButton("Нет", (dialog, id) -> {
                    // Ничего не делать
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmationDialogAfterSelection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы уверены в выборе?");

        builder.setPositiveButton("Да", (dialog, id) -> updateImage());

        builder.setNegativeButton("Нет", (dialog, id) -> {
                    // Ничего не делать
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateImage() {
        String image = WorkingWithImage.bitmapToBase64(selectedBitmap);
        String updateBase64 = WorkingWithImage.resizeBase64Image(image);
        imageView.setImageBitmap(WorkingWithImage.base64ToBitmap(updateBase64));
        currentPatient.setPatientPhoto(WorkingWithImage.resizeBase64Image(updateBase64));
    }

    private void changeObject()
    {
        currentPatient.setLastName(lastName.getText().toString());
        currentPatient.setFirstName(firstName.getText().toString());
        currentPatient.setPatronymic(patronymic.getText().toString());
        currentPatient.setPassportData(passportData.getText().toString());
        currentPatient.setDateOfBirth(dateOfBirth.getText().toString());
        currentPatient.setGender(gender.getText().toString());
        currentPatient.setAddress(address.getText().toString());
        currentPatient.setPhoneNumber(phoneNumber.getText().toString());
        currentPatient.setEmail(email.getText().toString());
    }

    private void updateData()
    {
        try {
            changeObject();
            ApiManager.sendRequest(HttpRequestType.PUT, "Put/" + currentPatient.getId_Patient(), currentPatient.convertToJSONObject());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(getApplicationContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
        changeBut.setText("Изменить");
        changeBut.setOnClickListener(v -> changeOn());
        blockingEdin(false);
        imageView.setOnClickListener(null);
    }

    private void addData()
    {
        try {
            changeObject();
            ApiManager.sendRequest(HttpRequestType.POST, "Post", currentPatient.convertToJSONObject());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(getApplicationContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
        changeBut.setText("Добавить");
        changeBut.setOnClickListener(v -> addingModeActive());
        clear();
    }
}