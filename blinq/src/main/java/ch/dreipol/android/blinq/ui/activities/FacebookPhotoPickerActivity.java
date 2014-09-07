package ch.dreipol.android.blinq.ui.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.model.facebook.FacebookAlbum;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhoto;
import ch.dreipol.android.blinq.ui.fragments.facebook.FacebookAlbumFragment;
import ch.dreipol.android.blinq.ui.fragments.facebook.FacebookPhotosFragment;
import ch.dreipol.android.blinq.ui.fragments.facebook.FacebookPhotosOfMeFragment;

public class FacebookPhotoPickerActivity extends BaseBlinqActivity implements FacebookAlbumFragment.OnAlbumInteractionListener, FacebookPhotosFragment.OnPhotoInteractionListener {


    public static final String FACEBOOK_PHOTO_ID = "ch.dreipol.android.facebookphotopicker.object_id";
    private static final String FACEBOOK_PHOTO_URL = "ch.dreipol.android.facebookphotopicker.url";

    @Override
    public void didSelectAlbum(FacebookAlbum album) {
        String albumId = album.getId();
        showPhotos(new FacebookPhotosFragment(), albumId);
    }

    @Override
    public void didSelectPhoto(FacebookPhoto photo) {
        Intent data = new Intent();
        data.putExtra(FACEBOOK_PHOTO_ID, photo.getId());
        data.putExtra(FACEBOOK_PHOTO_URL, photo.getPicture());
        setResult(RESULT_OK, data);
        finish();
    }

    private void showPhotos(FacebookPhotosFragment fragment, String albumId) {
        FragmentTransaction transaction = createFragmentTransaction();

        Bundle args = new Bundle();
        args.putString(FacebookPhotosFragment.ALBUM_ID, albumId);
        fragment.setArguments(args);
        transaction.setCustomAnimations(R.anim.left_to_right, R.anim.left_to_left_out, R.anim.left_out_to_left, R.anim.right_to_left);
        transaction.replace(R.id.picker_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void didSelectPhotosOfMe() {
        showPhotos(new FacebookPhotosOfMeFragment(), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_photo_picker);

        FragmentTransaction transaction = createFragmentTransaction();
        transaction.add(R.id.picker_container, new FacebookAlbumFragment());
        transaction.commit();

    }

    private FragmentTransaction createFragmentTransaction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.beginTransaction();
    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }


}
