package uk.co.danhawkes.machopper.ui;

import uk.co.danhawkes.machopper.R;
import uk.co.danhawkes.machopper.ui.preferences.PreferenceActivity;
import uk.co.danhawkes.machopper.ui.preferences.UiUtils;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private String[] drawerTitles;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;

	private final CountdownFragment countdownFragment = new CountdownFragment();
	private final LogFragment logFragment = new LogFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UiUtils.setThemeFromPreferences(this);
		setContentView(R.layout.activity_main);

		setVisibleFragment(countdownFragment);

		drawerTitles = new String[] { getString(R.string.fragment_countdown_title),
				getString(R.string.fragment_logs_title) };
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
				drawerTitles));
		drawerList.setOnItemClickListener(new DrawerItemClickListener());
		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle = new DrawerToggler(this, drawerLayout);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.common, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, PreferenceActivity.class));
			return true;
		} else if (id == R.id.action_help) {
			new HelpDialogFragment().show(getFragmentManager(), null);
			return true;
		} else {
			return false;
		}
	}

	private class DrawerToggler extends ActionBarDrawerToggle {

		public DrawerToggler(Activity activity, DrawerLayout drawerLayout) {
			super(activity, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
					R.string.drawer_close);
		}
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Fragment fragment;
			switch (position) {
			case 0:
				fragment = countdownFragment;
				break;
			case 1:
			default:
				fragment = logFragment;
				break;
			}

			setVisibleFragment(fragment);

			drawerList.setItemChecked(position, true);
			setTitle(drawerTitles[position]);
			drawerLayout.closeDrawer(drawerList);
		}
	}

	private void setVisibleFragment(Fragment fragment) {
		getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
	}

	@Override
	public void setTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}
}
