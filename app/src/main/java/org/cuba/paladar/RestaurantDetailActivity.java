package org.cuba.paladar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.cuba.paladar.Adapters.ImagePagerAdapter;
import org.cuba.paladar.Model.DataContexts.ApplicationDataContext;
import org.cuba.paladar.Model.Entities.ContentCritic;
import org.cuba.paladar.Model.Entities.Language;
import org.cuba.paladar.Model.Entities.Restaurant;

import java.util.ArrayList;
import java.util.Locale;

public class RestaurantDetailActivity extends BaseActivity {

    private ActionBar actionBar;
    private ApplicationDataContext dataContext;
    private Language language;
    private Restaurant restaurant;
    private org.cuba.paladar.Model.Entities.RestaurantCritic restaurantCritic;
    private ContentCritic contentCritic;
    private long restaurant_id;
    private ProgressDialog progressBar;
    private ViewPager imageViewPager;

    private static View createTabView(Context context, Drawable icon,
                                      String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab, null);

        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(text);

        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.restaurant_detail);

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getString(R.string.contact));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator(getString(R.string.critic));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator(getString(R.string.rank));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        restaurant_id = getIntent().getLongExtra("restaurant_id", -1);

        try {
            dataContext = new ApplicationDataContext(RestaurantDetailActivity.this);

            progressBar = new ProgressDialog(this);
            progressBar.setIndeterminate(true);
            progressBar.setMessage(getString(R.string.msg_loading));
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.show();

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
        finish();

        return true;
    }

    public void showData() {
        // Hours
        ArrayList<String> hours = restaurant
                .getHoursTranslate(RestaurantDetailActivity.this);
        if (hours.size() > 0) {
            TableLayout hoursTable = (TableLayout) findViewById(R.id.hours);
            TableRow row;
            for (String hour : hours) {
                row = new TableRow(RestaurantDetailActivity.this);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView textHour = new TextView(RestaurantDetailActivity.this);

                textHour.setText(hour);

                row.addView(textHour);

                hoursTable.addView(row, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            }

            ((TextView) findViewById(R.id.hours_label))
                    .setVisibility(View.VISIBLE);
            ((TableLayout) findViewById(R.id.hours))
                    .setVisibility(View.VISIBLE);
        }

        // Address
        if (restaurant.getAddress() != null) {
            TextView address = (TextView) findViewById(R.id.address);
            address.setText(restaurant.getAddress().__toString());

            ((TextView) findViewById(R.id.address_label))
                    .setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.address)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.goMap)).setVisibility(View.VISIBLE);

            if (restaurant.getAddress().getLatitude() == 0
                    && restaurant.getAddress().getLongitude() == 0) {
                ((Button) findViewById(R.id.goMap)).setEnabled(false);
                ((TextView) findViewById(R.id.address)).setEnabled(false);
            }
        }

        // Phone
        if (restaurant.getPhones() != null) {
            TextView phone = (TextView) findViewById(R.id.phone);
            phone.setText(restaurant.getPhones());

            ((TextView) findViewById(R.id.phone_label))
                    .setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.phone)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.callPhone)).setVisibility(View.VISIBLE);
        }

        // Cell
        if (restaurant.getCell() != null) {
            TextView cell = (TextView) findViewById(R.id.cell);
            cell.setText(restaurant.getCell());

            ((TextView) findViewById(R.id.cell_label))
                    .setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.cell)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.callCell)).setVisibility(View.VISIBLE);
        }

        // Web
        if (restaurant.getWeb() != null) {
            TextView web = (TextView) findViewById(R.id.web);
            web.setText(restaurant.getWeb());

            ((TextView) findViewById(R.id.web_label))
                    .setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.web)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.goWeb)).setVisibility(View.VISIBLE);
        }

        // Email
        if (restaurant.getEmail() != null) {
            TextView email = (TextView) findViewById(R.id.email);
            email.setText(restaurant.getEmail());

            ((TextView) findViewById(R.id.email_label))
                    .setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.email)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.sendEmail)).setVisibility(View.VISIBLE);
        }
    }

    public void goMap(View view) {
        Intent intent = new Intent().setClass(RestaurantDetailActivity.this,
                MapActivity.class);

        intent.putExtra("restaurant_id", restaurant_id);
        intent.putExtra("latitude", restaurant.getAddress().getLatitude());
        intent.putExtra("longitude", restaurant.getAddress().getLongitude());

        startActivity(intent);
    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        if (view.getId() == R.id.callPhone || view.getId() == R.id.phone) {
            intent.setData(Uri.parse("tel:" + restaurant.getPhones()));
        } else {
            intent.setData(Uri.parse("tel:" + restaurant.getCell()));
        }

        startActivity(intent);
    }

    public void goWeb(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        String url = restaurant.getWeb();

        if (!url.contains("http://")) {
            url = "http://" + url;
        }

        intent.setData(Uri.parse(url));

        startActivity(intent);
    }

    public void sendEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        intent.setData(Uri.parse("mailto:" + restaurant.getEmail()));

        startActivity(intent);
    }

    private double round(double num, int places) {
        return Math.round(num * Math.pow(10, places)) / Math.pow(10, places);
    }

    public class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                if (dataContext != null) {
                    dataContext.restaurantsSet.fill("ID = ?", new String[]{String.valueOf(restaurant_id)}, null, 1);
                    restaurant = dataContext.restaurantsSet.get(0);

                    dataContext.restaurantCriticSet.fill("restaurant_id = ?", new String[]{String.valueOf(restaurant_id)}, null, 1);
                    restaurantCritic = dataContext.restaurantCriticSet.get(0);

                    if (restaurantCritic != null) {
                        dataContext.contentCriticSet.fill("restaurant_critic_id = ?", new String[]{restaurantCritic.getID().toString()}, null, null);
                        contentCritic = dataContext.getContentCritic(Locale.getDefault().getLanguage());
                    }
                }
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(
                                RestaurantDetailActivity.this,
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
                actionBar.setTitle(restaurant.getName());

                // TAB 1
                imageViewPager = (ViewPager) findViewById(R.id.imageViewPager);
                if (restaurant.getImages() != null && restaurant.getImages().size() > 0) {
                    imageViewPager.setAdapter(new ImagePagerAdapter(
                            RestaurantDetailActivity.this, restaurant.getImages()));
                } else {
                    ((RelativeLayout) imageViewPager.getParent()).setVisibility(View.GONE);
                }

                // Show the present data
                showData();

                // TAB 2
                WebView webViewCritic = (WebView) findViewById(R.id.critic);
                webViewCritic.setBackgroundColor(Color.WHITE);
                webViewCritic.setVerticalScrollBarEnabled(true);
                webViewCritic.setVerticalFadingEdgeEnabled(true);

                webViewCritic.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view,
                                                            String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onLoadResource(WebView view, String url) {
                        if (!progressBar.isShowing()) {
                            progressBar.show();
                        }
                        super.onLoadResource(view, url);
                    }

                    public void onPageFinished(WebView view, String url) {
                        if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                    }
                });

                String critic = null;
                String author = null;
                if (restaurantCritic != null) {
                    if (contentCritic != null) {
                        critic = contentCritic.getContent();
                    } else {
                        critic = getString(R.string.no_critic);
                    }
                    author = restaurantCritic.getAuthor();
                } else {
                    critic = getString(R.string.no_critic);
                }

                String html = "<!DOCTYPE html><html lang=\"es-ES\"><head><meta charset=\"utf-8\"><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"></head><body style=\"text-align:justify; font-size:18px; margin:10px 15px 0 15px;\">"
                        + "<center><div><h1 style=\"background:#D1996A; border-color:#663a10; border-radius:4px; border-style:solid; border-width:2px; display:inline; font-size:19px; font-weight:bolder; margin-left:20px; padding:1px 9px 2px;\">"
                        + restaurant.getName() + "</h1></div>";

                if (restaurant.getSlogan() != null) {
                    html += "<div style=\"margin-top:5px;\"><strong style=\"font-style: italic;color:#4C2600; font-size:12px;\">\""
                            + restaurant.getSlogan() + "\"</strong></div>";
                }

                html += "<div style=\"margin-top:10px;\"><label><span style=\"background-color:#036109; border-color:#4f4f4f; border-radius:1px 9px; border-style:solid; border-width:1px; color:white; font-size:12px; padding:1px 9px 2px; font-weight:bold; line-height:14px; text-shadow:0 -1px 0 rgba(0, 0, 0, 0.25); vertical-align:baseline; white-space:nowrap;\">"
                        + "#"
                        + String.valueOf(restaurant.getRankingCpl())
                        + " del ranking de Cuba Paladar</span></label></div>"
                        + "<div style=\"text-align:justify; margin-top:15px;\">"
                        + critic;

                if (author != null) {
                    html += "<p><b>Por: </b>"
                            + author + "</p>";
                }

                html += "</div></center></body></html>";

                webViewCritic.loadData(html, "text/html; charset=utf-8",
                        "UTF-8");

                // TAB 3
                WebView webViewRank = (WebView) findViewById(R.id.rank);
                webViewRank.setBackgroundColor(Color.WHITE);
                webViewRank.setVerticalScrollBarEnabled(true);
                webViewRank.setVerticalFadingEdgeEnabled(true);

                webViewRank.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view,
                                                            String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onLoadResource(WebView view, String url) {
                        if (!progressBar.isShowing()) {
                            progressBar.show();
                        }
                        super.onLoadResource(view, url);
                    }

                    public void onPageFinished(WebView view, String url) {
                        if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                    }
                });

                if (restaurantCritic != null) {
                    html = "<!DOCTYPE html><html lang=\"es-ES\"><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"><meta charset=\"UTF-8\"><style type=\"text/css\">"
                            + "body{text-align:justify; font-size:14px; margin:10px 15px 0 15px;} tr th, tr td{padding-right: 20px;} .progress {border: 1px solid #51a529; background-color: #f7f7f7;background-image: -moz-linear-gradient(center top , #f5f5f5, #f9f9f9);background-repeat: repeat-x;border-radius: 4px;box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) inset;height: 18px; overflow: hidden;} .progress .bar {padding-left: 10px;	text-align: left;text-shadow: none !important;background-color: #91e170;background-image: none;font-weight: bold;height: 20px;box-shadow: 0 -1px 0 rgba(0, 0, 0, 0.15) inset;box-sizing: border-box;color: #fff;font-size: 12px;text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);transition: width 0.6s ease 0s;} .bar small {color: #000;	padding-left: 3px;}small {font-size: 100%;}"
                            + "</style><head></head><body><center><div><label><span style=\"background-color:#036109; border-color:#4f4f4f; border-radius:1px 9px; border-style:solid; border-width:1px; color:white; font-size:14px; padding:1px 9px 2px; font-weight:bold; line-height:16px; text-shadow:0 -1px 0 rgba(0, 0, 0, 0.25); vertical-align:baseline; white-space:nowrap;\">"
                            + "# "
                            + String.valueOf(restaurant.getRankingCpl())
                            + " del ranking de Cuba Paladar</span></label></div>";

                    double points = round(
                            restaurantCritic.getFood()
                                    + restaurantCritic.getDrinking()
                                    + restaurantCritic.getService()
                                    + restaurantCritic.getEnviroment()
                                    + restaurantCritic.getPriceQuality()
                                    + restaurantCritic.getPrice()
                                    + restaurantCritic.getCount(), 2);

                    html += "<div style=\"text-align:center;font-size:1.5em;font-weight:bold;color:#468847;margin-top:15px\">"
                            + String.valueOf(points)
                            + "</div><div style=\"font-size:1em;color:#999;text-align:center;margin-top:0px\">puntos</div>"
                            + "<table style=\"margin-top:15px\"><thead><tr><th>Variable</th><th>%</th><th>Puntos</th></tr></thead><tbody>";

                    double percent;

                    if (restaurantCritic.getFood() > 0) {
                        percent = round(restaurantCritic.getFood() / 40 * 100,
                                2);
                        html += "<tr><td style=\"color: black; font-weight: bolder;\">Comida</td><td style=\"width: 80px;\"><div class=\"progress progress-success\" style=\"margin-bottom:0\"><div class=\"bar\" style=\"width: "
                                + String.valueOf(percent)
                                + "%\">"
                                + "<small>"
                                + String.valueOf(percent)
                                + "</small></div></div></td><td style=\"font-size: 0.9em; text-align:center;\">"
                                + restaurantCritic.getFood() + "</td></tr>";
                    }

                    if (restaurantCritic.getDrinking() > 0) {
                        percent = round(
                                restaurantCritic.getDrinking() / 12 * 100, 2);
                        html += "<tr><td style=\"color: black; font-weight: bolder;\">Bebida</td><td style=\"width: 80px;\"><div class=\"progress progress-success\" style=\"margin-bottom:0\"><div class=\"bar\" style=\"width: "
                                + String.valueOf(percent)
                                + "%\">"
                                + "<small>"
                                + String.valueOf(percent)
                                + "</small></div></div></td><td style=\"font-size: 0.9em; text-align:center;\">"
                                + restaurantCritic.getDrinking() + "</td></tr>";
                    }

                    if (restaurantCritic.getService() > 0) {
                        percent = round(
                                restaurantCritic.getService() / 12 * 100, 2);
                        html += "<tr><td style=\"color: black; font-weight: bolder;\">Servicio</td><td style=\"width: 80px;\"><div class=\"progress progress-success\" style=\"margin-bottom:0\"><div class=\"bar\" style=\"width: "
                                + String.valueOf(percent)
                                + "%\">"
                                + "<small>"
                                + String.valueOf(percent)
                                + "</small></div></div></td><td style=\"font-size: 0.9em; text-align:center;\">"
                                + restaurantCritic.getService() + "</td></tr>";
                    }

                    if (restaurantCritic.getEnviroment() > 0) {
                        percent = round(
                                restaurantCritic.getEnviroment() / 12 * 100, 2);
                        html += "<tr><td style=\"color: black; font-weight: bolder;\">Ambientación</td><td style=\"width: 80px;\"><div class=\"progress progress-success\" style=\"margin-bottom:0\"><div class=\"bar\" style=\"width: "
                                + String.valueOf(percent)
                                + "%\">"
                                + "<small>"
                                + String.valueOf(percent)
                                + "</small></div></div></td><td style=\"font-size: 0.9em; text-align:center;\">"
                                + restaurantCritic.getEnviroment()
                                + "</td></tr>";
                    }

                    if (restaurantCritic.getPriceQuality() > 0) {
                        percent = round(
                                restaurantCritic.getPriceQuality() / 10 * 100,
                                2);
                        html += "<tr><td style=\"color: black; font-weight: bolder;\">Precio/Calidad</td><td style=\"width: 80px;\"><div class=\"progress progress-success\" style=\"margin-bottom:0\"><div class=\"bar\" style=\"width: "
                                + String.valueOf(percent)
                                + "%\">"
                                + "<small>"
                                + String.valueOf(percent)
                                + "</small></div></div></td><td style=\"font-size: 0.9em; text-align:center;\">"
                                + restaurantCritic.getPriceQuality()
                                + "</td></tr>";
                    }

                    if (restaurantCritic.getPrice() > 0) {
                        percent = round(restaurantCritic.getPrice() / 8 * 100,
                                2);
                        html += "<tr><td style=\"color: black; font-weight: bolder;\">Precio</td><td style=\"width: 80px;\"><div class=\"progress progress-success\" style=\"margin-bottom:0\"><div class=\"bar\" style=\"width: "
                                + String.valueOf(percent)
                                + "%\">"
                                + "<small>"
                                + String.valueOf(percent)
                                + "</small></div></div></td><td style=\"font-size: 0.9em; text-align:center;\">"
                                + restaurantCritic.getPrice() + "</td></tr>";
                    }

                    if (restaurantCritic.getCount() > 0) {
                        percent = round(restaurantCritic.getCount() / 6 * 100,
                                2);
                        html += "<tr><td style=\"color: black; font-weight: bolder;\">Cantidad</td><td style=\"width: 80px;\"><div class=\"progress progress-success\" style=\"margin-bottom:0\"><div class=\"bar\" style=\"width: "
                                + String.valueOf(percent)
                                + "%\">"
                                + "<small>"
                                + String.valueOf(percent)
                                + "</small></div></div></td><td style=\"font-size: 0.9em; text-align:center;\">"
                                + restaurantCritic.getCount() + "</td></tr>";
                    }

                    html += "</tbody></table><hr/><div style=\"margin: 25px 0 20px 0;\"><label><span style=\"background-color:#ffce61; border-color:#b07c0a; border-radius:1px 9px; border-style:solid; border-width:1px; color:black; font-size:16px; padding:1px 9px 2px; font-weight:bold; line-height:14px; text-shadow:0 -1px 0 rgba(0, 0, 0, 0.25); vertical-align:baseline; white-space:nowrap;\">";
                    html += "# " + String.valueOf(restaurant.getRankingP())
                            + " del ranking de Popularidad</span>";
                    html += "</label></div></center></body></html>";

                    webViewRank.loadData(html, "text/html; charset=utf-8",
                            "UTF-8");
                }
            }

            // progressBar.dismiss();
        }

        @Override
        protected void onCancelled() {
            progressBar.dismiss();
        }
    }
}
