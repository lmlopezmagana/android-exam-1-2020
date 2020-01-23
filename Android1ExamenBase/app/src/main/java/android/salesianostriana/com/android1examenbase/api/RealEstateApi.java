package android.salesianostriana.com.android1examenbase.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RealEstateApi {

    private String masterKey;
    private OkHttpClient client;

    public RealEstateApi(String key) {
        this.masterKey = key;
        this.client = new OkHttpClient.Builder()
                .callTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }



    public List<Vivienda> get50PrimerasViviendas() {
        String url = "https://realstatev2.herokuapp.com/properties?access_token="+masterKey+"&sort=+createdAt&limit=50";

        List<Vivienda> result = null;

        try {
            Response response = client.newCall(
                    new Request.Builder()
                            .url(url)
                            .build()
            ).execute();

            JSONArray array = new JSONArray(response.body().string());

            result = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                result.add(JSONViviendaToVivienda(array.getJSONObject(i)));
            }

            return result;
        } catch (IOException | JSONException ex) {
            Log.e("RealEstate", "Exception: " + ex.getMessage());
            return null;
        }

    }


    public Vivienda getViviendaById(String id) {

        String url = "https://realstatev2.herokuapp.com/properties/"+ id+ "?access_token="+masterKey;
        Vivienda result = null;

        try {

            Response response = client.newCall(new Request.Builder()
                    .url(url)
                    .build()
            ).execute();

            JSONObject jsonObject = new JSONObject(response.body().string());

            result = JSONViviendaToVivienda(jsonObject);

            return result;

        } catch (IOException | JSONException ex) {
            Log.e("RealEstate", "Exception: " + ex.getMessage());
            return null;
        }

    }



    private Vivienda JSONViviendaToVivienda(JSONObject jsonObject) throws JSONException {
        return Vivienda.builder()
                .id(jsonObject.getString("id"))
                .titulo(jsonObject.getString("title"))
                .descripcion(jsonObject.getString("description"))
                .precio(jsonObject.getDouble("precio"))
                .categoria(jsonObject.getJSONObject("categoryId").getString("name"))
                .url_foto(jsonObject.getJSONArray("photos").getString(0))
                .propietario(jsonObject.getJSONObject("ownerId").getString("name"))
                .url_propietario(jsonObject.getJSONObject("ownerId").getString("picture"))
                .build();



    }


}
