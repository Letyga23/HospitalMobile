package com.example.hospitalmobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class DetailsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Bitmap selectedBitmap = null;
    private Button saveBut = null;
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


        currentPatient = getIntent().getParcelableExtra("patient");
        imageView = findViewById(R.id.photoPacient);
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

        imageView.setOnClickListener(v -> showConfirmationDialog());

        saveBut = findViewById(R.id.saveButton);
        saveBut.setVisibility(View.INVISIBLE);
        saveBut.setOnClickListener(v -> saveData());
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
        saveBut.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(selectedBitmap);
        currentPatient.setPatientPhoto(bitmapToBase64(selectedBitmap));
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private void saveData()
    {
        try {
            ApiManager.sendRequest(HttpRequestType.PUT, "Put/" + currentPatient.getId_Patient(), currentPatient.convertToJSONObject());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(getApplicationContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
        saveBut.setVisibility(View.INVISIBLE);
    }
}