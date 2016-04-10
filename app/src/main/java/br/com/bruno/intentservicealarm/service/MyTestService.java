package br.com.bruno.intentservicealarm.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.com.bruno.intentservicealarm.QueryDB.PlaceQueryDB;
import br.com.bruno.intentservicealarm.model.Place;

/**
 * Created by Bruno on 10/04/2016.
 */
public class MyTestService extends IntentService{

    public MyTestService() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Do the task here
        Log.d("MyTaskService", "Service Running");

        List<Place> places = new ArrayList<>();

        try {
            HttpResponse response = null;

            String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20local.search%20where%20zip%3D'94085'%20and%20query%3D'pharmacy'&format=json&diagnostics=true&callback=";

            response = connectionGet(url);

            if(response.getEntity() != null){
                InputStream content = response.getEntity().getContent();
                final String resultString = convertStreamToString(content);

                JSONObject jsonObject = new JSONObject(resultString);
                JSONObject queryObj = jsonObject.getJSONObject("query");

                JSONObject resultsObj = queryObj.getJSONObject("results");
                JSONArray arrayResults = resultsObj.getJSONArray("Result");

                for(int i = 0; i < arrayResults.length(); i++){
                    JSONObject obj = arrayResults.getJSONObject(i);

                    Place place = new Place();
                    place.setPlace_id(Integer.valueOf(obj.getString("id")));
                    place.setTitle(obj.getString("Title"));
                    place.setAddress(obj.getString("Address"));
                    place.setCity(obj.getString("City"));
                    place.setState(obj.getString("State"));
                    place.setPhone(obj.getString("Phone"));
                    place.setLatitude(obj.getString("Latitude"));
                    place.setLongitude(obj.getString("Longitude"));

                    JSONObject objRating = obj.getJSONObject("Rating");

                    place.setAvarageRating(objRating.getString("AverageRating"));
                    place.setLastReviewIntro(objRating.getString("LastReviewIntro"));

                    place.setBusinessUrl(obj.getString("BusinessUrl"));

                    places.add(place);
                }

                content.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            for(Place place : places){
                Log.d("Place: ", place.getTitle());
            }

            PlaceQueryDB.insertPlaces(this, places);
        }
    }

    private HttpResponse connectionGet(String url) throws Exception{
        HttpContext localContext = new BasicHttpContext();
        HttpClient client = getNewHttpClient();


        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");

        return client.execute(httpGet, localContext);
    }

    private class CustomX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private class MySSLSocketFactory1 extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory1(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new CustomX509TrustManager();
            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        public MySSLSocketFactory1(SSLContext context) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
            super(null);
            sslContext = context;
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }

    }

    private HttpClient getNewHttpClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{new CustomX509TrustManager()},
                    new SecureRandom());

            HttpClient client = new DefaultHttpClient();

            SSLSocketFactory ssf = new MySSLSocketFactory1(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(params, 50000000);
            HttpConnectionParams.setConnectionTimeout(params, 50000000);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, params);

        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    //TODO melhorar essa rotina para convert Stream to Classe mapeada utilizar o GSon
    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {

                e.printStackTrace();

            }
        }
        return sb.toString();
    }
}
