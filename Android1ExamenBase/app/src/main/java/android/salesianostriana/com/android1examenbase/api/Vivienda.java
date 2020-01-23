package android.salesianostriana.com.android1examenbase.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase modelo para la oferta de una vivienda
 */
@Data @NoArgsConstructor @AllArgsConstructor
@Builder
public class Vivienda {

    private String id;
    private String titulo;
    private String descripcion;
    private double precio;
    private String categoria;
    private String url_foto;
    private String propietario;
    private String url_propietario;


}
