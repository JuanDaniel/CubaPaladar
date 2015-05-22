package org.cuba.paladar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileExplorerActivity extends ListActivity {

    private int field;
    private List<String> item = null;
    private List<String> path = null;
    private String root = "/";
    private TextView myPath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_explorer);
        // getActionBar().setDisplayHomeAsUpEnabled(true);

        field = getIntent().getExtras().getInt("field");

        myPath = (TextView) findViewById(R.id.path);

        getDir(root);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getDir(String dirPath) {
        myPath.setText(getString(R.string.location) + ": " + dirPath);

        item = new ArrayList<String>();

        path = new ArrayList<String>();

        File f = new File(dirPath);

        File[] files = f.listFiles();

        if (!dirPath.equals(root)) {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(f.getParent());
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            path.add(file.getPath());

            if (file.isDirectory()) {
                item.add(file.getName() + "/");
            } else {
                item.add(file.getName());
            }
        }

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
                R.layout.row_file_explorer, item);

        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position,
                                   long id) {
        File file = new File(path.get(position));

        if (file.isDirectory()) {
            if (file.canRead()) {
                getDir(path.get(position));
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(
                                "[" + file.getName()
                                        + "] folder can't be read!")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).show();

            }

        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(getString(R.string.select_file))
                    .setPositiveButton(getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent returnIntent = new Intent();

                                    returnIntent.putExtra("field", field);
                                    returnIntent.putExtra("path",
                                            path.get(position));

                                    setResult(RESULT_OK, returnIntent);

                                    finish();
                                }

                            })
                    .setNegativeButton(R.string.no,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    arg0.cancel();
                                }
                            }).show();
        }
    }

    public void onBackPressed() {
        if (path.get(0).equals(root)) {
            getDir(path.get(1));
        } else {
            finish();
        }
    }

}
