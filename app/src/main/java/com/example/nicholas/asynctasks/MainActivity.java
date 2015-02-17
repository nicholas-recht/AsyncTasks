package com.example.nicholas.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private List<String> list;
    private ArrayAdapter<String> adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_item_content, list);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void writeFile(View view) {
        WriteFileTask task = new WriteFileTask();
        task.execute();

        Toast toast = Toast.makeText(getApplicationContext(), "Begin writing file...", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void readFile(View view) {
        ReadFileTask task = new ReadFileTask();
        task.execute();

        Toast toast = Toast.makeText(getApplicationContext(), "Begin reading file...", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void clearList(View view) {
        list.clear();
        adapter.notifyDataSetChanged();
        progressBar.setProgress(0);

        Toast toast = Toast.makeText(getApplicationContext(), "List cleared", Toast.LENGTH_SHORT);
        toast.show();
    }

    private class WriteFileTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String filename = "writeFile.txt";
            try {
                OutputStreamWriter outputStream = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));

                for (int i = 0; i < 101; ++i) {
                    String out = String.valueOf(i) + "\n";
                    outputStream.write(out);
                    Thread.sleep(50);
                    publishProgress(i);
                }

                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setProgress(0);
        }
    }

    private class ReadFileTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String filename = "writeFile.txt";
            try {
                InputStream inputStream = openFileInput(filename);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);

                String curLine = br.readLine();

                //loop through each line and display the numWords and contents
                int i = 1;
                while (curLine != null) {
                    list.add(curLine);
                    curLine = br.readLine();

                    publishProgress(i++);

                    Thread.sleep(50);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
            progressBar.setProgress(0);
        }
    }
}
