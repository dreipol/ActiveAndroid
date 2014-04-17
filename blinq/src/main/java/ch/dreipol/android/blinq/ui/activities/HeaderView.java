package ch.dreipol.android.blinq.ui.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;

/**
 * Created by phil on 17.04.14.
 */
public class HeaderView extends RelativeLayout {


    public HeaderView(Context context) {
        super(context);
        LayoutInflater inflater = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        inflater.inflate(R.layout.header_layout, this, true);

        findViewById(R.id.settings_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.match_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }
}
