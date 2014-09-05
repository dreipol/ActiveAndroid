package ch.dreipol.android.blinq.ui.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Select;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.model.ChatMessage;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.ui.adapters.ChatCursorAdapter;
import ch.dreipol.android.dreiworks.ui.activities.ActivityTransitionType;

public class ChatActivity extends BaseBlinqActivity {

    public static final String MATCH_ID = "MATCH_ID";
    private ListView mListView;
    private ChatCursorAdapter mAdapter;
    private Loader<Cursor> mCursorLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle params = getIntent().getExtras();

        Match match = Match.load(Match.class, params.getLong(MATCH_ID));

        TextView nameView = (TextView) findViewById(R.id.profile_name);
        nameView.setText(match.getProfile().getFirstName());


        Button button =  findButtonWithId(R.id.send_button);

        final EditText inputView = (EditText) findViewById(R.id.chat_input);


        inputView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }});

            setupList();

            button.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                String chatMessage = inputView.getText().toString();
                inputView.clearFocus();
                ChatActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//              TODO: sam, go 4 it!
            }
            }

            );
        }

    private void setupList() {

        mListView = (ListView) findViewById(R.id.chat_list);

        Cursor cursor = ActiveAndroid.getDatabase().rawQuery(new Select().from(ChatMessage.class).toSql(), null);


        mAdapter = new ChatCursorAdapter(this, cursor, 0);


        mCursorLoader = getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri = ContentProvider.createUri(ChatMessage.class, null);
                return new CursorLoader(ChatActivity.this,
                        uri,
                        null, null, null, null
                );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                mAdapter.swapCursor(cursor);

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdapter.swapCursor(null);

            }
        });


        mListView.setAdapter(mAdapter);


        mCursorLoader.startLoading();
    }


    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overrideTransitionForAnimationDirection(ActivityTransitionType.TO_RIGHT);
    }
}
