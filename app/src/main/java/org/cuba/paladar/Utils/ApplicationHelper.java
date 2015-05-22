package org.cuba.paladar.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ApplicationHelper {

    private static ApplicationHelper instance = null;
    public Context context;
    private HashMap<String, String> config;

    public synchronized static ApplicationHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ApplicationHelper();
        }

        instance.context = context;

        return instance;
    }

    public void checkConfig() {
        try {
            FileInputStream fil = context.openFileInput("config.xml");
        } catch (FileNotFoundException e) {
            try {
                writeConfig(null, null);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private HashMap loadConfig() {
        try {
            FileInputStream fil = context.openFileInput("config.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document dom = builder.parse(fil);

            Element root = dom.getDocumentElement();
            NodeList nodes = root.getChildNodes();

            config = new HashMap<>();
            config.put("pathData", nodes.item(0).getFirstChild().getNodeValue());
            config.put("pathMap", nodes.item(1).getFirstChild().getNodeValue());
            config.put("dateUpdate", nodes.item(2).getFirstChild().getNodeValue());
        } catch (Exception e) {
            writeConfig(null, null);
        }

        return config;
    }

    public void writeConfig(String pathData, String pathMap) {
        try {
            OutputStreamWriter fout = new OutputStreamWriter(context.openFileOutput(
                    "config.xml", Context.MODE_PRIVATE));

            XmlSerializer ser = Xml.newSerializer();
            ser.setOutput(fout);

            String defaultPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + File.separator
                    + "CubaPaladar"
                    + File.separator;

            ser.startTag("", "config");

            ser.startTag("", "path_data");
            if (pathData == null) {
                ser.text(defaultPath + "data.db");
            } else {
                ser.text(pathData);
            }
            ser.endTag("", "path_data");

            ser.startTag("", "path_map");
            if (pathMap == null) {
                ser.text(defaultPath + "cuba.db");
            } else {
                ser.text(pathMap);
            }
            ser.endTag("", "path_map");

            ser.startTag("", "dateupdate");
            if (pathData == null) {
                ser.text("22/12/2014");
            } else {
                File file = new File(pathData);
                if (file.exists()) {
                    long ms = file.lastModified();
                    Date date = new Date(ms);
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    ser.text(String.valueOf(calendar.get(Calendar.DATE)) + "/"
                            + String.valueOf(calendar.get(Calendar.MONTH) + "/"
                            + String.valueOf(calendar.get(Calendar.YEAR))));
                }
            }
            ser.endTag("", "dateupdate");

            ser.endTag("", "config");
            ser.endDocument();

            fout.close();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        config = loadConfig();
    }

    public HashMap getConfig() {
        if (config == null) {
            config = loadConfig();
        }

        return config;
    }

    public String getDataBasePath() throws DataBaseException {
        if (config == null) {
            config = loadConfig();
        }

        String dataBasePath = config.get("pathData");

        if (!(new File(dataBasePath).exists())) {
            throw new DataBaseException();
        }

        return dataBasePath;
    }

    public String getMapPath() throws DataBaseException {
        if (config == null) {
            config = loadConfig();
        }

        String mapPath = config.get("pathMap");

        if (!(new File(mapPath).exists())) {
            throw new DataBaseException();
        }

        return mapPath;
    }

    public class SearchException extends Exception {
    }

    public class DataBaseException extends Exception {
    }
}
