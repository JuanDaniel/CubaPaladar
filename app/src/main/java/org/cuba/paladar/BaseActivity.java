package org.cuba.paladar;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

/**
 * Created by jdsantana on 31/03/15.
 */
public class BaseActivity extends ActionBarActivity {

    protected void configDataBasePath() {
        Toast.makeText(BaseActivity.this,
                getString(R.string.msg_database_exception_data),
                Toast.LENGTH_LONG).show();

        Intent update = new Intent(BaseActivity.this, UpdateActivity.class);

        startActivity(update);
        finish();
    }
}
