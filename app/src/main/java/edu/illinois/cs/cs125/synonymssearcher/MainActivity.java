package edu.illinois.cs.cs125.synonymssearcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SynonymsSearcher:Main";
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);

        final Button searchSynonym = findViewById(R.id.searchSynonym);
        searchSynonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Starting to search synonyms");
                searchSynonym();
            }
        });
    }
    void searchSynonym() {
        final EditText typeWord = findViewById(R.id.typeWord );
        final TextView returnSynonym = findViewById(R.id. returnSynonym);
        String content = typeWord.getText().toString();
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://words.bighugelabs.com/api/2/0391721aed61e3ff23694461808b8d1c/"
                    + content + "/json",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                                String synonyms = getSynonyms(response.toString());
                                returnSynonym.setText(synonyms);
                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSynonyms(final String json) {
        if (json == null) {
            return null;
        }
        String synResult = "";
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        if (result.has("adjective") ) {
            JsonObject adjective = result.getAsJsonObject("adjective");
            JsonArray syn = adjective.getAsJsonArray("syn");
            for (int i = 0; i < syn.size() - 1; i++) {
                synResult += syn.get(i).getAsString() + ", ";
            }
            synResult += syn.get(syn.size() - 1).getAsString() + "!";
        } else if (result.has("noun") ) {
            JsonObject adjective = result.getAsJsonObject("noun");
            JsonArray syn = adjective.getAsJsonArray("syn");
            for (int i = 0; i < syn.size() - 1; i++) {
                synResult += syn.get(i).getAsString() + ", ";
            }
            synResult += syn.get(syn.size() - 1).getAsString() + "!";
        } else if (result.has("verb")) {
            JsonObject adjective = result.getAsJsonObject("verb");
            JsonArray syn = adjective.getAsJsonArray("syn");
            for (int i = 0; i < syn.size() - 1; i++) {
                synResult += syn.get(i).getAsString() + ", ";
            }
            synResult += syn.get(syn.size() - 1).getAsString() + "!";
        } else if (result.has("adverb")) {
            JsonObject adjective = result.getAsJsonObject("adverb");
            JsonArray syn = adjective.getAsJsonArray("syn");
            for (int i = 0; i < syn.size() - 1; i++) {
                synResult += syn.get(i).getAsString() + ", ";
            }
            synResult += syn.get(syn.size() - 1).getAsString() + "!";
        } else {
            synResult = "Sorry, can not find synonym of this word.";
        }
        return synResult;
    }

}
