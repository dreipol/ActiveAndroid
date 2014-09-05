package ch.dreipol.android.blinq.ui.headers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;

/**
 * Created by phil on 24.04.14.
 */
public class HeaderView extends RelativeLayout {

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        inflater.inflate(R.layout.ui_header, this, true);

    }

    public void updateConfiguration(IHeaderViewConfiguration config) {
        View headerImage = findViewById(R.id.blinq_header_image);
        TextView headerTitle = (TextView) findViewById(R.id.blinq_header_title);
        ImageView headerIcon = (ImageView)findViewById(R.id.blinq_header_right_button);

        if (config.showTitle()) {
            headerImage.setVisibility(GONE);
            headerTitle.setVisibility(VISIBLE);

            headerTitle.setText(config.getTitle());
        } else {
            headerImage.setVisibility(VISIBLE);
            headerTitle.setVisibility(GONE);
        }
        if(config.hasIcon()){
            headerIcon.setVisibility(VISIBLE);
            headerIcon.setImageDrawable(getResources().getDrawable(config.getIconDrawable()));
        }else{
            headerIcon.setVisibility(INVISIBLE);
        }

    }
}
