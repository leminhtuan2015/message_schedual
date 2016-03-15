package com.leminhtuan.myapplication.message_schedual.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.leminhtuan.myapplication.R;
import com.leminhtuan.myapplication.message_schedual.adapters.ListMessageAdapter;
import com.leminhtuan.myapplication.message_schedual.daos.MessageSchedualDAO;
import com.leminhtuan.myapplication.message_schedual.events.MessageSentEvent;
import com.leminhtuan.myapplication.message_schedual.models.MessageSchedual;
import com.leminhtuan.myapplication.message_schedual.services.ContactService;
import com.leminhtuan.myapplication.message_schedual.services.MessageService;
import com.leminhtuan.myapplication.message_schedual.utils.HardCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private NavigationView navigationView;
    private ListView listView;
    private ProgressBar progressBar;
    private DrawerLayout drawer;
    private ArrayAdapter adapter;
    private List<MessageSchedual> dataSet;
    private MessageSchedualDAO messageSchedualDAO;
    private MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        messageSchedualDAO = new MessageSchedualDAO(this);
        messageService =  new MessageService(this);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_main);
        getViews();
        new BuildListViewTask().execute();
        getSupportActionBar().setTitle("Message Schedual");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == HardCode.REQUEST_CODE_CREATE) {
            Toast.makeText(this, R.string.successful, Toast.LENGTH_SHORT).show();
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);

            MessageSchedual message = (MessageSchedual) data.getExtras().get("message");

            dataSet.add(message);
            adapter.notifyDataSetChanged();
            messageService.sendDelay(message);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.list_view_messages) {
            menu.setHeaderTitle("Message");
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.options, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final int index = menuinfo.position;
        final MessageSchedual messageSchedual = dataSet.get(index);

        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("Delete alarm ?");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        messageService.cancel(messageSchedual);
                        messageSchedualDAO.delete(messageSchedual);
                        dataSet.remove(index);
                        adapter.notifyDataSetChanged();
                    }
                });
                alertBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                alertBuilder.create();
                alertBuilder.show();
                return true;
            case R.id.cancle:
                messageSchedual.setStatus(HardCode.CANCELED);
                messageSchedualDAO.update(messageSchedual);
                messageService.cancel(messageSchedual);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.resum:
                messageSchedual.setStatus(HardCode.PENDING);
                messageSchedualDAO.update(messageSchedual);
                messageService.sendDelay(messageSchedual);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void getViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        listView = (ListView) findViewById(R.id.list_view_messages);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivityForResult(intent, HardCode.REQUEST_CODE_CREATE);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        dataSet = new ArrayList<MessageSchedual>();
        adapter = new ListMessageAdapter(this, R.layout.message_row, dataSet);

        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

    @Subscribe
    public void onEvent(MessageSentEvent event) {
        for (int i = 0; i < dataSet.size(); i++) {
            MessageSchedual messageSchedual = dataSet.get(i);
            if(messageSchedual.getId() == event.getMessageSchedual().getId()) {
                messageSchedual.setSentNumber(event.getMessageSchedual().getSentNumber());
                messageSchedual.setFailNumber(event.getMessageSchedual().getFailNumber());
                adapter.notifyDataSetChanged();
            }
        }
    }

    class BuildListViewTask extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            ContactService.contactsMapping(MainActivity.this);
            dataSet.clear();
            dataSet.addAll(messageSchedualDAO.all());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}
