package org.cuba.paladar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.cuba.paladar.Adapters.CityAdpater;
import org.cuba.paladar.Adapters.ProvinceAdpater;
import org.cuba.paladar.Model.DataContexts.ApplicationDataContext;
import org.cuba.paladar.Model.Entities.City;
import org.cuba.paladar.Model.Entities.Province;

import java.util.List;

public class NoticeMeActivity extends BaseActivity {

    private ActionBar actionBar;
    private ApplicationDataContext dataContext;
    private ProgressDialog progressBar;
    private Spinner selectProvince;
    private Spinner selectCity;
    private EditText textName;
    private EditText textPerson;
    private EditText textPhone;
    private List<Province> list_province;
    private List<City> list_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.notice_me);

        selectProvince = (Spinner) findViewById(R.id.select_province);
        selectCity = (Spinner) findViewById(R.id.select_city);
        textName = (EditText) findViewById(R.id.name_restaurant);
        textPerson = (EditText) findViewById(R.id.person);
        textPhone = (EditText) findViewById(R.id.phone);

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setMessage(getString(R.string.msg_loading));
        progressBar.show();

        try {
            dataContext = new ApplicationDataContext(NoticeMeActivity.this);

            // Execute LoadDataTask AsyncTask
            new LoadDataTask().execute((Void) null);
        } catch (AdaFrameworkException e) {
            configDataBasePath();
        } catch (org.cuba.paladar.Utils.ApplicationHelper.DataBaseException e) {
            configDataBasePath();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendNotification(View view) {
        if (TextUtils.isEmpty(textName.getText().toString())) {
            textName.setError(getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(textPerson.getText().toString())) {
            textPerson.setError(getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(textPhone.getText().toString())) {
            textPhone.setError(getString(R.string.error_field_required));
        } else {
            String msg = "Datos solicitados para formar parte del catálogo de Cuba Paladar.\n\n";
            msg += "\tProvincia: "
                    + ((Province) selectProvince.getSelectedItem()).getName()
                    + "\n";
            msg += "\tCiudad: "
                    + ((City) selectCity.getSelectedItem()).getName() + "\n";
            msg += "\tNombre del restaurant: " + textName.getText().toString()
                    + "\n";
            msg += "\tPersona a contactar: " + textPerson.getText().toString()
                    + "\n";
            msg += "\tTeléfono de contacto: " + textPhone.getText().toString()
                    + "\n\n";
            msg += "Mensaje enviado desde App Cuba Paladar.";

            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{"cuba@cubapaladar.org"});
            intent.putExtra(Intent.EXTRA_SUBJECT,
                    "Sugerir restaurante a Cuba Paladar");
            intent.putExtra(Intent.EXTRA_TEXT, msg);

            startActivity(Intent.createChooser(intent,
                    getString(R.string.choose_email_client)));
        }
    }

    public class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                if (dataContext != null) {
                    list_province = dataContext.provinceSet.search(true, null, null, null, null, null, null, null);
                }
            } catch (AdaFrameworkException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(
                                NoticeMeActivity.this,
                                getString(R.string.msg_database_exception_data),
                                Toast.LENGTH_LONG).show();
                    }
                });

                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                ArrayAdapter<Province> adapterProvince = new ProvinceAdpater(
                        NoticeMeActivity.this, list_province);
                adapterProvince
                        .setDropDownViewResource(R.layout.spinner_dropdown_item);
                selectProvince.setAdapter(adapterProvince);

                selectProvince
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int arg2, long arg3) {
                                try {
                                    list_city = dataContext.citySet.search(
                                            true,
                                            "province_id = ?",
                                            new String[]{list_province.get(arg2).getID().toString()},
                                            "ID ASC", null, null, null, null);

                                    ArrayAdapter<City> adapterCity = new CityAdpater(
                                            getApplicationContext(), list_city);
                                    adapterCity
                                            .setDropDownViewResource(R.layout.spinner_dropdown_item);
                                    selectCity.setAdapter(adapterCity);

                                } catch (AdaFrameworkException e) {
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(
                                                    NoticeMeActivity.this,
                                                    getString(R.string.msg_database_exception_data),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }

                        });
            }
            progressBar.dismiss();
        }

        @Override
        protected void onCancelled() {
            progressBar.dismiss();
        }
    }
}
