package UF1.Threads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    String error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        Button b = findViewById(R.id.button);
        TextView txt = findViewById(R.id.textView);

        b.setOnClickListener(view -> executor.execute(() -> {
            String res = getDataFromUrl("https://api.myip.com");
            Log.i("MYAPP",res);
            handler.post(() -> txt.append(res));
        }));

    }

    private String getDataFromUrl(String demoIdUrl) {
        String result = null;
        int resCode;
        InputStream in;
        try {
            URL url = new URL(demoIdUrl);
            URLConnection urlConn = url.openConnection();

            HttpsURLConnection httpsConn = (HttpsURLConnection) urlConn;
            httpsConn.setAllowUserInteraction(false);
            httpsConn.setInstanceFollowRedirects(true);
            httpsConn.setRequestMethod("GET");
            httpsConn.connect();
            resCode = httpsConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpsConn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, StandardCharsets.ISO_8859_1), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                in.close();
                result = sb.toString();
            } else {
                error += resCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}