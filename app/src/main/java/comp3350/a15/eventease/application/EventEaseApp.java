package comp3350.a15.eventease.application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.AssetManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;
import comp3350.a15.eventease.presentation.NewServiceRequestBroadcastReceiver;
import comp3350.a15.eventease.presentation.RespondedServiceRequestBroadcastReceiver;
import dagger.Lazy;
import dagger.hilt.android.HiltAndroidApp;


//Android application class required for use with hilt dependency injection library
@HiltAndroidApp
public class EventEaseApp extends Application {
    private static String dbName = "EventEaseDB";

    public static final String ACCEPTED_SERVICE_REQUEST_INTENT = "acceptedServiceRequest";
    public static final String REJECTED_SERVICE_REQUEST_INTENT = "rejectedServiceRequest";
    public static final String NEW_SERVICE_REQUEST_INTENT = "newServiceRequest";

    @Inject
    RespondedServiceRequestBroadcastReceiver requestResponseReceiver;
    @Inject
    NewServiceRequestBroadcastReceiver newRequestReceiver;

    @Inject
    Lazy<IUserManager> userManager;

    public static String getDBPathName() {
        return dbName;
    }

    public static void setDBPathName(String name) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(e);
        }
        dbName = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        copyDatabaseToDevice(this);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter acceptedRequestFilter = new IntentFilter(ACCEPTED_SERVICE_REQUEST_INTENT);
        IntentFilter rejectedRequestFilter = new IntentFilter(REJECTED_SERVICE_REQUEST_INTENT);
        IntentFilter newRequestFilter = new IntentFilter(NEW_SERVICE_REQUEST_INTENT);
        localBroadcastManager.registerReceiver(requestResponseReceiver, acceptedRequestFilter);
        localBroadcastManager.registerReceiver(requestResponseReceiver, rejectedRequestFilter);
        localBroadcastManager.registerReceiver(newRequestReceiver, newRequestFilter);

    }

    public static void copyDatabaseToDevice(Context context) {
        final String DB_PATH = "db";

        String[] assetNames;
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = context.getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            if (assetNames != null) {
                for (int i = 0; i < assetNames.length; i++) {
                    assetNames[i] = DB_PATH + "/" + assetNames[i];
                }

                copyAssetsToDirectory(context, assetNames, dataDirectory);

                setDBPathName(dataDirectory.toString() + "/" + dbName);
            }
        } catch (IOException ioe) {
            System.out.println("Unable to access application data: " + ioe.getMessage());
        }

    }

    private static void copyAssetsToDirectory(Context context, String[] assets, File directory) throws IOException {
        AssetManager assetManager = context.getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }
}
