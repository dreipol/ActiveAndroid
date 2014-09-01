package ch.dreipol.android.blinq.ui.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Date;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.impl.LocationService;
import rx.functions.Action1;


public class SystemInformationActivity extends BaseBlinqActivity {

    private GridLayout mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_information);
        mGrid = (GridLayout) findViewById(R.id.sysinfo_grid);

        AppService appService = AppService.getInstance();
        appService.getLocationService().subscribeToLocation().subscribe(new Action1<LocationService.LocationInformation>() {
            @Override
            public void call(LocationService.LocationInformation locationInformation) {

                addSeparatorRow();

                addRow("Location:", locationInformation.locationName);
                if (locationInformation.getLocation() != null) {
                    addRow("Coordinates:", locationInformation.getLocation().toString());
                }

            }
        });
        final IFacebookService facebookService = appService.getFacebookService();

        facebookService.subscribeToSessionState().subscribe(new Action1<FacebookService.FacebookServiceInfo>() {
            @Override
            public void call(FacebookService.FacebookServiceInfo info) {
                addSeparatorRow();
                if (info.status.equals(FacebookService.FacebookServiceStatus.LOGGED_IN)) {

                    addRow("Facebook Session:", info.toString());
                    addRow("Facebook Token:", facebookService.getAccessToken());
                    addRow("Facebook Id:", facebookService.getFacebookId());
                } else {
                    addRow("Facebook:", "No FB Session");
                }
            }
        });

        addRow("BOARD", Build.BOARD);
        addRow("BOOTLOADER", Build.BOOTLOADER);
        addRow("BRAND", Build.BRAND);
        addRow("CPU_ABI", Build.CPU_ABI);
        addRow("CPU_ABI2", Build.CPU_ABI2);
        addRow("DEVICE", Build.DEVICE);
        addRow("DISPLAY", Build.DISPLAY);
        addRow("FINGERPRINT", Build.FINGERPRINT);
        addRow("HARDWARE", Build.HARDWARE);
        addRow("HOST", Build.HOST);
        addRow("ID", Build.ID);
        addRow("MANUFACTURER", Build.MANUFACTURER);
        addRow("MODEL", Build.MODEL);
        addRow("PRODUCT", Build.PRODUCT);
        addRow("RADIO", Build.getRadioVersion());
        addRow("SERIAL", Build.SERIAL);
        addRow("TAGS", Build.TAGS);
        addRow("TIME", new Date(Build.TIME).toString());
        addRow("TYPE", Build.TYPE);
        addRow("USER", Build.USER);
        addSeparatorRow();
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            addRow("Build number", Integer.toString(info.versionCode));
            addRow("Build version", info.versionName);
            addSeparatorRow();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        addRow("ANDROID_ID", Settings.Secure.getString(appService.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));

    }

    private void addSeparatorRow() {
        addRow("", "");
    }

    private void addRow(String name, String value) {
        TextView nameTv = new TextView(this);
        nameTv.setText(name);
        TextView valueTv = new TextView(this);
        valueTv.setText(value);
//        GridLayout.Spec  titleTxtSpecC1 = GridLayout.spec(0);
//        GridLayout.Spec  titleTxtSpecC2 = GridLayout.spec(1);
//        int rowCount = mGrid.getRowCount() + 1;
//        GridLayout.Spec  titleTxtSpecR = GridLayout.spec(rowCount);
////        mGrid.setRowCount(rowCount);
////        mGrid.addView(nameTv, new GridLayout.LayoutParams(titleTxtSpecR, titleTxtSpecC1));
////        mGrid.addView(valueTv, new GridLayout.LayoutParams(titleTxtSpecR, titleTxtSpecC2));
        mGrid.addView(nameTv);
        mGrid.addView(valueTv);
    }


    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }

}
