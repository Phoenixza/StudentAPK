package com.asa.welser.student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    /** Called when the activity is first created. */
    JSONArray android = null;
    JSONArray androids = null;
    ListView list;
    Spinner spinner1;
    Spinner spinner2;
    Button getdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //HashMap<String, String> map = new HashMap<String, String>();
    private static String url = "http://storage.mobile2b.de/recruiting/students.json";
    private static final String TAG_LIST = "students";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "id";
    private static final String TAG_GRADE = "grade";
    private static final String TAG_SEMESTER = "semester";
    private static final String TAG_DIFFERENCE = "difference";
    EditText etResponse;
    TextView tvIsConnected;
    int selectionSort = 1;
    int selectionSemester = 1;
    //public JSONObject c = null;
    public Comparator<Map<String, String>> mapComparatorNameUp = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m1.get(TAG_NAME).compareTo(m2.get(TAG_NAME));
        }
    };
    public Comparator<Map<String, String>> mapComparatorNameDown = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m2.get(TAG_NAME).compareTo(m1.get(TAG_NAME));
        }
    };
    public Comparator<Map<String, String>> mapComparatorGradeUp = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m1.get(TAG_GRADE).compareTo(m2.get(TAG_GRADE));
        }
    };
    public Comparator<Map<String, String>> mapComparatorGradeDown = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m2.get(TAG_GRADE).compareTo(m1.get(TAG_GRADE));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        getdata = (Button) findViewById(R.id.button);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sort, R.layout.spinner_layout);
        spinner1.setAdapter(adapter);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.semester, R.layout.spinner_layout);
        spinner2.setAdapter(adapter2);
        // spinner1 = Sort
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectionSort = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // spinner2 = Semester
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectionSemester = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

  }


    public void onClick(View view) {
        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute("http://storage.mobile2b.de/recruiting/students.json");
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Daten geladen!" , Toast.LENGTH_SHORT).show();
            try {

                JSONObject json = new JSONObject(result);
                oslist.clear();
// Getting JSON Array from URL
                android = json.getJSONArray(TAG_LIST);
                for (int i = 0; i < android.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject c = android.getJSONObject(i);
// Storing JSON item in a Variable
                    String name =       "Name:        " + c.getString(TAG_NAME);
                    //String name = c.getString(TAG_NAME);
                    String id =         "ID:               "+ c.getString(TAG_ID);
                    String semester =   "Semester:  " + c.getString(TAG_SEMESTER);
                    String grade =      "Note:          " + c.getString(TAG_GRADE);
                    String difference = "Notendifferenz: " ;
// Adding value HashMap key => value

                    if(selectionSemester+1 == Integer.parseInt(c.getString(TAG_SEMESTER)) | selectionSemester == 3){
                        map.put(TAG_NAME, name);
                        map.put(TAG_ID, id);
                        map.put(TAG_SEMESTER, semester);
                        map.put(TAG_GRADE, grade);
                        int j;
                        if(c.getString(TAG_SEMESTER).contentEquals("1")){
                            difference = "Differenz:    --";
                        } else{
                            androids = json.getJSONArray(TAG_LIST);
                            for ( j = 0; j < androids.length(); j++) {
                                JSONObject d = androids.getJSONObject(j);


                                String x = c.getString(TAG_ID);
                                String y = d.getString(TAG_ID);
                                    if (c.getString(TAG_ID).equals(d.getString(TAG_ID)) | c.getString(TAG_ID).equalsIgnoreCase(d.getString(TAG_ID))){


                                        int a = Integer.parseInt(c.getString(TAG_SEMESTER));
                                        int b = Integer.parseInt(d.getString(TAG_SEMESTER));

                                        if( Integer.parseInt(c.getString(TAG_SEMESTER))-1 == Integer.parseInt(d.getString(TAG_SEMESTER))
                                                ){
                                            double dif = Double.parseDouble(d.getString(TAG_GRADE))- Double.parseDouble(c.getString(TAG_GRADE));
                                            double temp = dif * 100;
                                            temp = Math.round(temp);
                                            temp = temp / 100;
                                            dif = 0;
                                            difference = "Differenz:   " + temp;
                                            break;
                                        } else {
                                            int as = Integer.parseInt(c.getString(TAG_SEMESTER));
                                            int bs = Integer.parseInt(d.getString(TAG_SEMESTER));
                                            //difference += "Differenz: xxx " + as + " " + bs +"\n";
                                            difference = "Differenz:    --";
                                        }
                                    } else {
                                        //difference += "Differenz: " + x+ " " + y + " \n ";
                                        difference = "Differenz:    --";
                                    }
                            }

                        }
                        map.put(TAG_DIFFERENCE,difference);
                        oslist.add(map);
                    }
                }
                    // Sortingfunktion
                    if(selectionSort == 0){
                        Collections.sort(oslist, mapComparatorNameUp);
                    }else if (selectionSort == 1){
                        Collections.sort(oslist, mapComparatorNameDown);
                    }else if(selectionSort == 2){
                        Collections.sort(oslist, mapComparatorGradeUp);
                    }else if(selectionSort == 3){
                        Collections.sort(oslist, mapComparatorGradeDown);
                    }


                list = (ListView) findViewById(R.id.list);
                ListAdapter adapter = new SimpleAdapter(MainActivity.this, oslist,
                        R.layout.list_child,
                        new String[] {TAG_NAME, TAG_ID, TAG_SEMESTER, TAG_GRADE, TAG_DIFFERENCE }, new int[] {
                        R.id.name, R.id.id, R.id.semester, R.id.grade, R.id.difference });
                list.setAdapter(adapter);


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



        }

    }

}