package ch.dreipol.android.blinq.services;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import ch.dreipol.android.blinq.services.impl.NetworkService;
import ch.dreipol.android.blinq.services.model.Match;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;

import com.google.gson.reflect.TypeToken;

/**
 * Created by phil on 19/08/14.
 */
public class BetaNetworkService extends NetworkService {

    @Override
    protected RestAdapter.Builder getRestBuilder(Gson gson, String serverUrl) {
        RestAdapter.Builder restBuilder = super.getRestBuilder(gson, serverUrl);


        try {
            OkHttpClient client = new OkHttpClient();
            KeyStore keyStore = readKeyStore(); //your method to obtain KeyStore


            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);


            SSLSocketFactory socketFactory = context.getSocketFactory();
            client.setSslSocketFactory(socketFactory);
            client.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return hostname.equalsIgnoreCase("beta.blinqapp.ch");
                }
            });
            restBuilder = restBuilder.setClient(new OkClient(client));
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        return restBuilder;
    }

    private KeyStore readKeyStore() {

        KeyStore keyStore = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            Resources resources = getService().getContext().getResources();
            InputStream caInput = getInputStream(resources);

            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {

                try {
                    caInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyStore;
    }

    private InputStream getInputStream(Resources resources) {
        return resources.openRawResource(
                        resources.getIdentifier("raw/server",
                                "raw", getService().getContext().getPackageName()));
    }

    @Override
    public void loadMatches() {
        Resources resources = getService().getContext().getResources();
        Gson gson = getGson();
        InputStream inputStream = getMatchesStream(resources);
        getService().getMatchesService().loadMatches(Observable.<ArrayList<Match>>from(gson.<ArrayList<Match>>fromJson(new InputStreamReader(inputStream), new TypeToken<ArrayList<Match>>() {
        }.getType())));
    }
    
    
    private InputStream getMatchesStream(Resources resources) {
        return resources.openRawResource(
                resources.getIdentifier("raw/matches",
                        "raw", getService().getContext().getPackageName()));
    }
}
