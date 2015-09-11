package com.team1.moa_member_upload;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected ArrayList<ParseUser> WebToonDbList;

    final int[] i = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button read = (Button) findViewById(R.id.read);
        Button send = (Button) findViewById(R.id.send);
        final TextView text = (TextView) findViewById(R.id.text);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser i=new ParseUser();
                i.setUsername("dfdfd");
                i.setEmail("fdffff@sdf.com");
                i.setPassword("dff");
                i.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                            Log.d("df","fd");
                    }
                });
                //new ReadDbFileAsyncTask().execute("member.csv");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WebToonDbList != null) {
                    new UploadDbAsyncTask().execute(WebToonDbList);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.error_no_loaded_webtoon_list), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected ArrayList<ParseUser> getWebToonDbList(String filename) {
        ArrayList<ParseUser> readDbList = new ArrayList<>();

        File fileCsv = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + filename);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileCsv), "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] token = line.split(",", -1);

                ParseUser po=new ParseUser();
                po.setUsername(token[0]);
                po.setPassword(token[1]);
                po.setEmail(token[5]);
                po.put("detail", token[2]);
                po.put("info", token[3]);
                po.put("job", token[4]);
                po.put("phone", token[6]);

                po.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                            Log.d("fffffff","FFfffffffffffffffff");
                    }
                });
                readDbList.add(po);
            }
        } catch (FileNotFoundException e) {
            Log.d("filenot",e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("io",e.getMessage());
            e.printStackTrace();
        }

        return readDbList;
    }

    protected class ReadDbFileAsyncTask extends AsyncTask<String, Void, ArrayList<ParseUser>> {
        protected ProgressDialog progressDialog;

        public ReadDbFileAsyncTask() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.load_webtoon_list_from_file));
        }

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ArrayList<ParseUser> doInBackground(String... filenames) {
            return getWebToonDbList(filenames[0]);
        }

        protected void onPostExecute(ArrayList<ParseUser> result) {
            progressDialog.dismiss();
            WebToonDbList = result;
            for(ParseUser p:result)

            ((TextView) findViewById(R.id.text)).setText(String.format(getString(R.string.read_file_result_format), result.size()));
        }
    }

    protected class UploadDbAsyncTask extends AsyncTask<ArrayList<ParseUser>, Void, Void> {
        protected ProgressDialog progressDialog;
        protected int failCount = 0;

        public UploadDbAsyncTask() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.uploading_webtoon_to_server));
        }

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(ArrayList<ParseUser>... lists) {
            if (lists == null || lists.length < 1) return null;

            ArrayList<ParseUser> webToonList = lists[0];
            for (ParseUser po : webToonList) {

                try {
                    po.signUp();
                } catch (ParseException e) {
                    Log.d("dfdff","efrrrrr");
                    e.printStackTrace();
                }
            }
            onPostExecute();

            return null;
        }

        protected void onPostExecute() {
            progressDialog.dismiss();

            ((TextView) findViewById(R.id.text)).setText(String.format(getString(R.string.read_file_result_format), failCount));
        }
    }
}
