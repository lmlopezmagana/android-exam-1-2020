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

/**
 * Clase que permite realizar peticiones al api Real Estate, y obtener las ofertas
 */
public class RealEstateApi {

    private String masterKey;
    private OkHttpClient client;

    /**
     * Constructor
     * @param key MASTER KEY necesaria para poder hacer peticiones
     */
    public RealEstateApi(String key) {
        this.masterKey = key;
        this.client = new OkHttpClient.Builder()
                .callTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Método que obtiene las primeras 50 ofertas de viviendas
     * @return Lista con las 50 primeras viviendas. Null en caso de error
     */
    public List<Vivienda> get50PrimerasViviendas() {
        return getNViviendas(50, 1);
    }

    /**
     * Método que permite obtener un conjunto de viviendas del API
     * @param cantidad Cantidad de viviendas a solicitar
     * @param numPagina Número de página de resultados, comenzando en 1
     * @return Lista con las ofertas de viviendas
     */
    public List<Vivienda> getNViviendas(int cantidad, int numPagina) {
        String url = "https://realstatev2.herokuapp.com/properties?access_token="+masterKey+"&sort=+createdAt&limit="+ cantidad + "&page="+numPagina;
        Log.d("URL", url);

        List<Vivienda> result = null;

        try {
            Response response = client.newCall(
                    new Request.Builder()
                            .url(url)
                            .build()
            ).execute();

            JSONObject objeto = new JSONObject(response.body().string());
            JSONArray array = objeto.getJSONArray("rows"); //new JSONArray(objeto.getJSONArray("rows"));

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

    /**
     * Método que permite obtener la oferta de una vivienda por su ID
     * @param id ID de la oferta
     * @return Oferta de la vivienda, si existe. Nulo en otro caso.
     */
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


    /**
     * Método que transforma el objeto JSON en un objeto java.
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private Vivienda JSONViviendaToVivienda(JSONObject jsonObject) throws JSONException {
        return Vivienda.builder()
                .id(jsonObject.getString("id"))
                .titulo(jsonObject.getString("title"))
                .descripcion(jsonObject.getString("description"))
                .precio(jsonObject.optDouble("price",-1))
                .categoria(jsonObject.getJSONObject("categoryId").getString("name"))
                .url_foto(jsonObject.getJSONArray("photos").getString(0))
                .propietario(jsonObject.getJSONObject("ownerId").getString("name"))
                .url_propietario(jsonObject.getJSONObject("ownerId").getString("picture"))
                .build();



    }


}
