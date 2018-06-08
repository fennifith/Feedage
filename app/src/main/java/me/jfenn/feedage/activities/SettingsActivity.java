package me.jfenn.feedage.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.items.FeedPreferenceItemData;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.lib.data.AtomFeedData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.utils.DimenUtils;

public class SettingsActivity extends FeedageActivity {

    public static final String EXTRA_SHOULD_RESTART = "me.jfenn.feedage.EXTRA_SHOULD_RESTART";
    private static final String FORMAT_12H = "h:mm a";
    private static final String FORMAT_24H = "HH:mm";

    private SharedPreferences prefs;

    private RecyclerView feedRecycler;
    private List<ItemData> feedList;

    private TextView syncTimeValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        feedRecycler = findViewById(R.id.feeds);
        AppCompatSpinner theme = findViewById(R.id.theme);
        syncTimeValue = findViewById(R.id.syncTimeValue);

        updateSyncTime();
        findViewById(R.id.syncTime).setOnClickListener(v -> new TimePickerDialog(v.getContext(), (view, hourOfDay, minute) -> {
            getFeedage().setSyncTime(hourOfDay);
            updateSyncTime();
        }, getFeedage().getSyncTime(), 0, DateFormat.is24HourFormat(v.getContext()))
                .show());

        feedList = new ArrayList<>();
        for (FeedData feed : getFeedage().getFeeds())
            feedList.add(new FeedPreferenceItemData(feed));

        feedRecycler.setLayoutManager(new LinearLayoutManager(this));
        feedRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        feedRecycler.setAdapter(new ItemAdapter(feedList));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                String name = "han solo dies";
                if (feedList.get(position) instanceof FeedPreferenceItemData)
                    name = ((FeedPreferenceItemData) feedList.get(position)).getFeed().getBasicHomepage();

                new AlertDialog.Builder(SettingsActivity.this).setTitle(R.string.title_remove_feed)
                        .setMessage(String.format(getString(R.string.msg_remove_feed), name))
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                            feedList.remove(position);
                            feedRecycler.getAdapter().notifyItemRemoved(position);

                            List<FeedData> feeds = new ArrayList<>();
                            for (ItemData item : feedList) {
                                if (item instanceof FeedPreferenceItemData)
                                    feeds.add(((FeedPreferenceItemData) item).getFeed());
                            }
                            getFeedage().setFeeds(feeds);
                        })
                        .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
                            feedRecycler.getAdapter().notifyItemChanged(position);
                            dialogInterface.dismiss();
                        })
                        .show();
            }
        }).attachToRecyclerView(feedRecycler);

        findViewById(R.id.newFeed).setOnClickListener(v -> {
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
            input.setHint(R.string.title_feed_url);

            int padding = (int) DimenUtils.getPixelsFromDp(18);
            FrameLayout layout = new FrameLayout(this);
            layout.setPadding(padding, (int) DimenUtils.getPixelsFromDp(8), padding, 0);
            layout.addView(input);

            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_add_feed)
                    .setView(layout)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        String url = input.getText().toString();
                        if (URLUtil.isValidUrl(url)) {
                            FeedData feed = new AtomFeedData(url, Color.WHITE, Color.BLACK);
                            feedList.add(new FeedPreferenceItemData(feed));
                            feedRecycler.getAdapter().notifyItemInserted(feedList.size() - 1);

                            List<FeedData> feeds = new ArrayList<>();
                            for (ItemData item : feedList) {
                                if (item instanceof FeedPreferenceItemData)
                                    feeds.add(((FeedPreferenceItemData) item).getFeed());
                            }
                            getFeedage().setFeeds(feeds);

                            Toast.makeText(SettingsActivity.this, R.string.msg_feed_added, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(SettingsActivity.this, R.string.msg_invalid_url, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel())
                    .show();
        });

        theme.setAdapter(ArrayAdapter.createFromResource(this, R.array.themes, R.layout.support_simple_spinner_dropdown_item));
        theme.setSelection(prefs.getInt(Feedage.PREF_THEME, Feedage.THEME_LIGHT));
        theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != getFeedage().getThemePreference()) {
                    getFeedage().setTheme(position);

                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_SHOULD_RESTART, true);
                    setResult(RESULT_OK, intent);

                    finish();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Drawable icon = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back, getTheme());
        if (icon != null) {
            DrawableCompat.setTint(icon, getFeedage().getTextColorSecondary());
            toolbar.setNavigationIcon(icon);
            setSupportActionBar(toolbar);
        }
    }

    private void updateSyncTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, getFeedage().getSyncTime());
        calendar.set(Calendar.MINUTE, 0);
        syncTimeValue.setText(new SimpleDateFormat(DateFormat.is24HourFormat(this) ? FORMAT_24H : FORMAT_12H, Locale.getDefault())
                .format(calendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
