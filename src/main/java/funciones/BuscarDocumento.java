
package funciones;

import java.io.File;
import java.util.*;
import logicaHash.*;
import dao.gestores.*;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import persistencia.*;

/**
 *
 * @author tecio
 */

@ApplicationScoped
public class BuscarDocumento {

    @Inject private DocumentosDao docDao;
    @Inject private PosteoDao postDao;
    @Inject private TerminosDao terDao;
    
    private Vocabulario voc;
    private TreeMap<Integer, Documento> listDocs;

    public BuscarDocumento() {   
    }
    
    public Object[] buscar(String str, int r){
        voc = new Vocabulario();
        listDocs = new TreeMap<>();

        voc.setCantTotalDocs(docDao.findAll().size());
        buscarTextoIngresado(str, r);
        
        Object[] ordenado = listDocs.values().toArray();
        Arrays.sort(ordenado);
        ordenado = Arrays.copyOf(ordenado, r);
        return ordenado;
    }
    
    // Carga al vocabulario el termino a buscar
    private void cargarAVocabulario(Termino t) {
        try {
            Terminos_EC tEC = terDao.retrieve(t.hashCode());
            Termino ter = new Termino(tEC);
            voc.put(ter);
        } catch (Exception e) {
            System.out.println("Error al cargar vocabulario. " + e.getMessage());
        }
    }
    
    // Recorre un string y devuelve las listas de posteo de cada uno, excluyendo las stopwords
    private void buscarTextoIngresado(String str, int r){
        Scanner sc = new Scanner(str);
        while(sc.hasNext()){
            String pal = sc.next();
            Termino t = new Termino(pal);
            
            cargarAVocabulario(t);
            buscarTermino(t, r);
        }
    }
        
    // Busca de la lista de posteo de un termino los r docs con mayor frecuencia y los agrega al vocabulario
    private void buscarTermino(Termino t, int r){
        // Me fijo si es o no una stopWord
        boolean stopWord = validarStopWord(t);
        if(!stopWord){
            try {
                List<Posteo_EC> posteo = postDao.findPosteoForTermino(t, r);
                cargarListaDocumentos(posteo);
            } catch (Exception e) {
                System.out.println("Error al buscar la lista de posteo para el termino: " + t.getNom() + " \nEl error es: " + e.getMessage() );
            }
            System.out.println("El termino: '" + t.getNom() + "' NO es stopword");
        }else{System.out.println("El termino: '" + t.getNom() + "' es stopword");}
    }
    
    // Valida si un termino es stopword o no
    // Defino el porcentaje maximo de documentos en los que puede aparecer un termino para que no sea STOPWORD
    private final double porcentaje = 0.5;
    
    private boolean validarStopWord(Termino t) {
        boolean stopword = false;
        int apariciones;
        try {
            apariciones = ((Termino)voc.getVocabulario().get(t.hashCode())).getCantDocs();
        } catch (Exception e) {
            return false;
        }
        int cantDocs = voc.getCantTotalDocs();
    
        double idfMin = cantDocs/(cantDocs*porcentaje);
        double idfCalc = cantDocs/apariciones;
        
        if(idfCalc == 0){return false;}
        if(idfMin > idfCalc){stopword = true;}        
        return stopword;
    }

    // Carga una lista provisoria con todos los documentos obtenidos para un terino
    private void cargarListaDocumentos(List<Posteo_EC> posteo) {
        for (Object o : posteo) {
            Object[] post = (Object[]) o;
            Documento d = new Documento(post[0].toString());
            Termino ter = new Termino(post[1].toString());
            int frec = (int)post[2];
            int apariciones = voc.getCantTotalDocs();
            try {
                apariciones = ((Termino)voc.getVocabulario().get(ter.hashCode())).getCantDocs();
            } catch (Exception e) { System.out.println("Error al buscar las apariciones de un termino. " + e.getMessage());}
            
            double peso = frec * Math.log(voc.getCantTotalDocs()/apariciones);
            d.setIdr(peso);
            insertarLD(d);
        }
    }

    // Ordena la lista segun el indice de relevancia
    private void insertarLD(Documento d) {
        try {
            String p = buscarPath(d.getNombre());
            d.setPath(p);
        } catch (Exception e) {
            System.out.println("Error al buscar el path: " + e.getMessage());
        }
        if(listDocs.get(d.hashCode()) == null){
            listDocs.put(d.hashCode(), d);
        }
        else{
            Documento doc = listDocs.get(d.hashCode());
            d.setIdr(doc.getIdr() + d.getIdr());
            listDocs.replace(doc.hashCode(), doc, d);
        }
    }

    // Variable para el algoritmo de busqueda
    private static boolean encontro = false;
    
    public String buscarPath(String nomDoc){
        // Directorio donde se pueden agregar los archivos
        String rutaInicial  = "D:\\Tomi\\Facultad\\4To\\DLC\\Motor de Busqueda";
        
        String str = buscarPath(rutaInicial, rutaInicial, nomDoc);
        encontro = false;
        return str;
    }
    
    // Busco recursivamente en las carpetas el path del documento buscado
    private String buscarPath(String path0, String path1, String nomDoc){
        String prevPath = path0;
        String finalPath = path1;
        File rutaOriginal = new File(finalPath);
        File[] files = rutaOriginal.listFiles();
        for(File f : files){
            if(f.getName().equals(nomDoc)){
                encontro = true;
                return finalPath.concat("\\" + f.getName());
            }else if(f.isDirectory()){
                finalPath += "\\" + f.getName();
                finalPath = buscarPath(prevPath, finalPath, nomDoc);
                File fi = new File(finalPath);
                if(fi.getName().equals(nomDoc)){
                    break;
                }
            }
        }
        if(encontro){
            return finalPath;
        }
        return prevPath;
    }
}
