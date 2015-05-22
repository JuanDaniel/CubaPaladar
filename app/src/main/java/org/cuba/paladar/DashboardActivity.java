package org.cuba.paladar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.cuba.paladar.Adapters.ItemMenuAdapter;
import org.cuba.paladar.Model.ItemMenu;
import org.cuba.paladar.Utils.ApplicationHelper;

import java.util.ArrayList;

public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        ApplicationHelper.getInstance(DashboardActivity.this).checkConfig();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void menuSystem(View view) {
        showMenu(view, 1);
    }

    public void menuRestaurant(View view) {
        showMenu(view, 2);
    }

    public void rankCpl(View view) {
        Intent intent = new Intent(DashboardActivity.this, RankActivity.class);
        intent.putExtra("rank_type", 1);

        startActivity(intent);
    }

    public void rankP(View view) {
        Intent intent = new Intent(DashboardActivity.this, RankActivity.class);
        intent.putExtra("rank_type", 2);

        startActivity(intent);
    }

    public void info(View view) {
        Intent intent = new Intent(DashboardActivity.this, InfoActivity.class);

        startActivity(intent);
    }

    public void goWeb(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse("http://" + getString(R.string.url)));

        startActivity(intent);
    }

    public void showMenu(View view, int menu) {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.select_option);
        dialog.setContentView(R.layout.menu_options);

        ListView options = (ListView) dialog.findViewById(R.id.menu_options);
        ArrayList<ItemMenu> items = null;

        switch (menu) {
            // System options
            case 1:
                items = new ArrayList<ItemMenu>();
                items.add(new ItemMenu(android.R.drawable.ic_popup_sync,
                        getString(R.string.update), menu));
                items.add(new ItemMenu(android.R.drawable.ic_menu_help,
                        getString(R.string.help), menu));

                break;

            // Restaurant options
            case 2:
                items = new ArrayList<ItemMenu>();
                items.add(new ItemMenu(android.R.drawable.ic_menu_search,
                        getString(R.string.search), menu));
                items.add(new ItemMenu(android.R.drawable.ic_menu_mylocation,
                        getString(R.string.map), menu));
                items.add(new ItemMenu(android.R.drawable.ic_menu_myplaces,
                        getString(R.string.nearest), menu));
                items.add(new ItemMenu(android.R.drawable.ic_menu_send,
                        getString(R.string.notice_me), menu));

                break;

            default:
                break;
        }

        options.setAdapter(new ItemMenuAdapter(this, items));

        options.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                dialog.dismiss();

                ItemMenu item = (ItemMenu) arg0.getAdapter().getItem(arg2);

                // Menu System
                if (item.getMenu() == 1) {
                    switch (arg2) {
                        // Config
                        case 0:
                            Intent intentUpdate = new Intent(
                                    DashboardActivity.this, UpdateActivity.class);

                            startActivity(intentUpdate);

                            break;

                        // Help
                        case 1:
                            Intent intentHelp = new Intent(DashboardActivity.this,
                                    HelpActivity.class);

                            startActivity(intentHelp);

                            break;

                        default:
                            break;
                    }
                }
                // Menu restaurant
                else {
                    switch (arg2) {
                        // Search
                        case 0:
                            Intent intentSearch = new Intent(
                                    DashboardActivity.this, SearchActivity.class);

                            startActivity(intentSearch);

                            break;

                        // Map
                        case 1:
                            Intent intentMap = new Intent(DashboardActivity.this,
                                    MapActivity.class);

                            startActivity(intentMap);

                            break;

                        // Nears me
                        case 2:
                            Intent intentNear = new Intent(DashboardActivity.this,
                                    MapActivity.class);

                            intentNear.putExtra("localization", true);

                            startActivity(intentNear);

                            break;

                        // Notice me
                        case 3:
                            Intent intentNotice = new Intent(
                                    DashboardActivity.this, NoticeMeActivity.class);

                            startActivity(intentNotice);

                            break;

                        default:
                            break;
                    }
                }
            }
        });

        dialog.show();
    }

    public void onBackPressed() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);

        localBuilder
                .setTitle(R.string.msg_warning)
                .setCancelable(false)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        })
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.cancel();
                            }
                        });

        localBuilder.setMessage(R.string.dialog_close);
        localBuilder.create().show();
    }
}
