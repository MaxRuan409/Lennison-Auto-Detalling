package com.lennison.vistas;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Motor de renderizado de plantillas HTML del lado del servidor.
 * Carga el archivo HTML y reemplaza marcadores dinámicos.
 */
public class Vista {

    private static final String DIRECTORIO_PLANTILLAS = "./web";

    /**
     * Carga una plantilla HTML de la carpeta web y reemplaza los marcadores {{PLACEHOLDER}}.
     * También limpia cualquier etiqueta script o JavaScript embebido.
     */
    public static String render(String nombrePlantilla, Map<String, String> marcadores) throws IOException {
        Path pathArchivo = Path.of(DIRECTORIO_PLANTILLAS, nombrePlantilla);
        if (!Files.exists(pathArchivo)) {
            return "<h1>Error: Plantilla " + nombrePlantilla + " no encontrada.</h1>";
        }

        // Leer todo el archivo HTML
        String html = Files.readString(pathArchivo, StandardCharsets.UTF_8);

        // Reemplazar marcadores dinámicos
        if (marcadores != null) {
            for (Map.Entry<String, String> entrada : marcadores.entrySet()) {
                String valor = entrada.getValue() == null ? "" : entrada.getValue();
                html = html.replace("{{" + entrada.getKey() + "}}", valor);
            }
        }

        // Remover etiquetas <script> y su contenido de forma estricta (Requisito: 100% Sin JS)
        html = html.replaceAll("(?i)<script\\b[^<]*(?:(?!</script>)<[^<]*)*</script>", "");
        
        // Remover gestores de eventos inline (ej. onclick, onload)
        html = html.replaceAll("(?i)\\son[a-z]+\\s*=\\s*\"[^\"]*\"", "");
        html = html.replaceAll("(?i)\\son[a-z]+\\s*=\\s*'[^']*'", "");

        return html;
    }
}
