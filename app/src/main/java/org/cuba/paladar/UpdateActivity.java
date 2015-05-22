package org.cuba.paladar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.cuba.paladar.Utils.ApplicationHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

public class UpdateActivity extends ActionBarActivity {

    private EditText pathData;
    private EditText pathMap;
    private TextView dateUpdate;
    private LinkedList<String[]> downloadings;
    // private SharedPreferences preferenceManager;
    // private DownloadManager downloadManager;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.update);

        pathData = (EditText) findViewById(R.id.path_data);
        pathMap = (EditText) findViewById(R.id.path_map);
        dateUpdate = (TextView) findViewById(R.id.date_update);

        loadConfig();

        // preferenceManager =
        // PreferenceManager.getDefaultSharedPreferences(this);
        // downloadManager = (DownloadManager)
        // getSystemService(DOWNLOAD_SERVICE);
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

    public void changePath(View view) {
        Intent fileExplorer = new Intent(UpdateActivity.this,
                FileExplorerActivity.class);
        fileExplorer.putExtra("field", view.getId());
        startActivityForResult(fileExplorer, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras().getInt("field") == R.id.btn_change_data) {
                    pathData.setText(data.getExtras().getString("path"));
                } else {
                    pathMap.setText(data.getExtras().getString("path"));
                }
                writeConfig(pathData.getText().toString(), pathMap.getText()
                        .toString());
                HashMap<String, String> config = ApplicationHelper.getInstance(UpdateActivity.this).getConfig();
                dateUpdate.setText(config.get("dateUpdate"));
            }
        }
    }

    public void loadConfig() {
        HashMap<String, String> config = ApplicationHelper.getInstance(UpdateActivity.this).getConfig();

        pathData.setText(config.get("pathData"));
        pathMap.setText(config.get("pathMap"));
        dateUpdate.setText(config.get("dateUpdate"));

    }

    public void writeConfig(String pathData, String pathMap) {
        ApplicationHelper.getInstance(UpdateActivity.this).writeConfig(pathData, pathMap);
    }

    public void update(View view) {
        // First check if the device are connect to internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            File file = new File(pathData.getText().toString());

            // url = getString(R.string.url) + "/apk/get/dataupdate";

            Toast.makeText(UpdateActivity.this,
                    getString(R.string.msg_downloading), Toast.LENGTH_LONG)
                    .show();

            downloadings = new LinkedList<String[]>(); // Url, Id, Path, Name,
            // PathOld
            String[] values = {"http://cubapaladar.org/app_update",
                    "cubapaladar-dataupdate", file.getParent(),
                    "dataupdate.db", pathData.getText().toString()};
            downloadings.add(values);

            downloadUpdate(values);

            file = new File(pathMap.getText().toString());

            if (!file.exists()) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(getString(R.string.select_file))
                        .setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        File file = new File(pathMap.getText()
                                                .toString());

                                        String[] values = {
                                                "http://cubapaladar.org/download_map",
                                                "cubapaladar-mapupdate",
                                                file.getParent(),
                                                "mapupdate.db",
                                                pathMap.getText().toString()};
                                        downloadings.add(values);

                                        downloadUpdate(values);
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
        } else {
            Toast.makeText(UpdateActivity.this,
                    getString(R.string.msg_no_connection), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void downloadUpdate(String[] values) {
        /*
		 * // Check the version, if is longer that API 8 if
		 * (Build.VERSION.SDK_INT >= 9 false) { Uri downloadUri =
		 * Uri.parse(values[0]);
		 * 
		 * DownloadManager.Request request = new DownloadManager.Request(
		 * downloadUri); request.setTitle(getString(R.string.msg_updating));
		 * request.setDescription(getString(R.string.msg_downloading));
		 * request.setDestinationInExternalFilesDir(getApplicationContext(),
		 * null, values[2] + File.separator + values[3]);
		 * request.setVisibleInDownloadsUi(true);
		 * 
		 * long downloadId = downloadManager.enqueue(request);
		 * 
		 * // Save the request id Editor PrefEdit = preferenceManager.edit();
		 * PrefEdit.putLong(values[1], downloadId); PrefEdit.commit(); } else {
		 * progressBar = new ProgressDialog(this);
		 * progressBar.setCancelable(false);
		 * progressBar.setCanceledOnTouchOutside(false);
		 * progressBar.setMessage(getString(R.string.msg_downloading));
		 * progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		 * progressBar.setProgress(0); progressBar.setMax(100);
		 * progressBar.show();
		 * 
		 * updateTask = new DownloadUpdateTask(values); updateTask.execute(); }
		 */

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage(getString(R.string.msg_downloading));
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        new DownloadUpdateTask(values).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

		/*
		 * IntentFilter intentFilter = new IntentFilter(
		 * DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		 * registerReceiver(downloadReceiver, intentFilter);
		 */
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregisterReceiver(downloadReceiver);
    }

	/*
	 * @SuppressLint("NewApi") private BroadcastReceiver downloadReceiver = new
	 * BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context arg0, Intent arg1) {
	 * DownloadManager.Query query = new DownloadManager.Query(); for (String[]
	 * values : downloadings) {
	 * query.setFilterById(preferenceManager.getLong(values[1], 0)); Cursor
	 * cursor = downloadManager.query(query); if (cursor.moveToFirst()) { int
	 * columnIndex = cursor .getColumnIndex(DownloadManager.COLUMN_STATUS); int
	 * status = cursor.getInt(columnIndex);
	 * 
	 * if (status == DownloadManager.STATUS_SUCCESSFUL) { long downloadId =
	 * preferenceManager.getLong(values[1], 0);
	 * 
	 * // Change the file name and throw the notification File old = new
	 * File(values[4]);
	 * 
	 * ParcelFileDescriptor file; try { file = downloadManager
	 * .openDownloadedFile(downloadId); FileInputStream inputStream = new
	 * ParcelFileDescriptor.AutoCloseInputStream( file); FileOutputStream
	 * outputStream = new FileOutputStream( values[2] + File.separator +
	 * values[3]);
	 * 
	 * byte[] buffer = new byte[1024]; int bufferLength = 0;
	 * 
	 * while ((bufferLength = inputStream.read(buffer)) > 0) {
	 * outputStream.write(buffer, 0, bufferLength); } outputStream.close();
	 * 
	 * File update = new File(values[2] + File.separator + values[3]);
	 * 
	 * if (old.exists() && update.exists()) { old.delete(); }
	 * 
	 * update.renameTo(new File(values[4]));
	 * 
	 * } catch (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * writeUpdateDate();
	 * 
	 * Toast.makeText(UpdateActivity.this,
	 * getString(R.string.msg_update_successful), Toast.LENGTH_LONG).show(); }
	 * else if (status == DownloadManager.STATUS_FAILED) {
	 * Toast.makeText(UpdateActivity.this,
	 * getString(R.string.msg_error_downloading), Toast.LENGTH_LONG).show(); } }
	 * } } };
	 */

    public void writeUpdateDate() {

    }

    public class DownloadUpdateTask extends AsyncTask<Void, Void, Boolean> {

        private String[] values;
        private int totalSize;
        private int downloadedSize;

        public DownloadUpdateTask(String[] values) {
            super();
            this.values = values;
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                URL uri = new URL(values[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) uri
                        .openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                urlConnection.connect();

                File file = new File(values[2] + File.separator + values[3]);

                FileOutputStream fileOutput = new FileOutputStream(file);

                InputStream inputStream = urlConnection.getInputStream();

                totalSize = urlConnection.getContentLength();
                downloadedSize = 0;

                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.setProgress(downloadedSize * 100
                                    / totalSize);
                        }
                    });
                }
                fileOutput.close();

                return true;

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            progressBar.dismiss();
            if (success) {
                File old = new File(values[4]);
                File update = new File(values[2] + File.separator + values[3]);

                if (old.exists() && update.exists()) {
                    old.delete();
                }

                update.renameTo(new File(values[4]));

                writeUpdateDate();

                Toast.makeText(UpdateActivity.this,
                        getString(R.string.msg_update_successful),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(UpdateActivity.this,
                        getString(R.string.msg_error_downloading),
                        Toast.LENGTH_LONG).show();
            }
        }

    }
}
