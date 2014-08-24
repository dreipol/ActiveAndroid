package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentTransaction;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.model.facebook.FacebookAlbum;
import ch.dreipol.android.blinq.ui.fragments.FacebookAlbumFragment;

public class FacebookPhotoPickerActivity extends BaseBlinqActivity implements FacebookAlbumFragment.OnAlbumInteractionListener {



    @Override
    public void didSelectAlbum(FacebookAlbum album) {

    }

    @Override
    public void didSelectPhotosOfMe() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_photo_picker);



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.picker_container, new FacebookAlbumFragment());
        transaction.commit();


    }@Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }


}
