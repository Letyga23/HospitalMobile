package com.example.hospitalmobile;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HospitalAdapter extends BaseAdapter
{
    private Context mContext;
    private List<Patient> mPatientList;

    public HospitalAdapter(Context mContext, List<Patient> mPatientList) {
        this.mContext = mContext;
        this.mPatientList = mPatientList;
    }

    @Override
    public int getCount() {
        return mPatientList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPatientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPatientList.get(position).getId_Patient();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_pacient, null);

        ImageView imageView = view.findViewById(R.id.imageViewItemPacientPhoto);
        TextView lastName = view.findViewById(R.id.LastName);
        TextView firstName = view.findViewById(R.id.FirstName);
        TextView patronymic = view.findViewById(R.id.Patronymic);
        TextView dateOfBirth = view.findViewById(R.id.DateOfBirth);

        Patient currentPatient = mPatientList.get(position);

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        imageView.setImageBitmap(currentPatient.getBitmapSource());
        lastName.setText(currentPatient.getLastName());
        firstName.setText(currentPatient.getFirstName());
        patronymic.setText(currentPatient.getPatronymic());
        dateOfBirth.setText(currentPatient.getDateOfBirth());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetails = new Intent(mContext, DetailsActivity.class);

                intentDetails.putExtra("patient", currentPatient);

                mContext.startActivity(intentDetails);
            }
        });


        return view;
    }
}
