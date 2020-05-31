/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones;

import JpaControllers.*;
import dao.gestores.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Scanner;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import logicaHash.*;
import persistencia.*;

/**
 *
 * @author tecio
 */

public class ScannerTxt { 
    private Vocabulario vocabulario = new Vocabulario();
    private final String todos = "D:\\Tomi\\Facultad\\4To\\DLC\\Motor de Busqueda\\DocumentosTP1-20200417T134605Z-001\\DocumentosTP1";
    private final String prueba = "D:\\Tomi\\Facultad\\4To\\DLC\\Motor de Busqueda\\DocumentosPrueba";
    private final String prueba2 = "D:\\Tomi\\Facultad\\4To\\DLC\\Motor de Busqueda\\DocumentosPrueba2";
    private final String carpetaIndexar = "D:\\Tomi\\Facultad\\4To\\DLC\\Motor de Busqueda\\Carpeta a Indexar";
    
//    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("doc_PU");
//    private DocumentosJpaController docJpa;
//    private TerminosJpaController terJpa;
//    private PosteoJpaController postJpa;
    
    @Inject private DocumentosDao docDao;
    @Inject private PosteoDao postDao;
    @Inject private TerminosDao terDao;
    
    public ScannerTxt() {
    }

    public void indexarCarpeta() throws FileNotFoundException, Exception{
        File c = new File(carpetaIndexar);
        File[] ficheros = c.listFiles();
        int cont = 0;
        for(File arch : ficheros) {
            cont ++;
            System.out.println(cont);
            
            Documento d = new Documento(arch.getName());
            Documentos_EC doc = new Documentos_EC(d.hashCode(), d.getNombre());
            
            // Se fija si ya existe el documento dentro de la BDD
            if(docDao.retrieve(doc.getHashDocumentos()) == null){
                try {
                    docDao.create(doc);
                    System.out.println("Docuemto cargado con exito");
                } catch (Exception e) {
                    System.out.println("No carga el documento: " + e.getMessage());
                }
            } else{ System.out.println("Ya existe el documento");}
            
            // Escanea el archivo y carga la tabla Hash de Vocabulario con los terminos que tiene ese documento
            scannerArchivo(arch);
        }
        
        cargarDatosABDD();
    }

    private void scannerArchivo(File arch) throws FileNotFoundException {
        Scanner sc = new Scanner(arch);
        try{
            while(sc.hasNext()){
                String pal = sc.next();
                pal = pal.toLowerCase();
                pal = pal.replaceAll("[^a-z]","");
                meterEnHash(pal, arch);
            }
        }catch(Exception e){
            System.out.println("Error al insertar Documento: " + arch.getName()+ "---Error: " + e.getMessage());
        }
    }

    private void meterEnHash(String pal, File arch) {
        Documento doc = new Documento(arch.getName());
        Termino termino = new Termino(pal);
        
        vocabulario.put(termino, doc);
    }

    private void cargarDatosABDD() {
        Enumeration e = vocabulario.getVocabulario().elements();

        while(e.hasMoreElements()){
            Termino pal = (Termino) e.nextElement();
            Terminos_EC ter = new Terminos_EC(pal.hashCode(), pal.getNom(), pal.getMTF(), pal.getPosteo().size());
            
            // Se fija si ya existe el termino en la base de datos
            // Lo crea/edita dependiendo del caso
            
            Terminos_EC terBD = terDao.retrieve(ter.getHashTermino());
            if(terBD == null){
                try {
                    terDao.create(terBD);
                } catch (Exception ex) {
                    System.out.println("Error al insertar el Hash del termino: " + pal.hashCode() + "---Error: " + ex.getMessage());
                }
            }
            else
            {
                if(terBD.getMaxTermFrec() <= ter.getMaxTermFrec()){terBD.setMaxTermFrec(ter.getMaxTermFrec());}
                terBD.setCantDocumentos(terBD.getCantDocumentos() + ter.getCantDocumentos());
                try {
                    terDao.update(terBD);
                } catch (Exception ex) {
                    System.out.println("Error al insertar el Hash del termino: " + pal.hashCode() + "---Error: " + ex.getMessage());
                }
            }
            
            // Inserto creo la lista de posteo para el termino
            Enumeration f = pal.getPosteo().elements();

            while(f.hasMoreElements()){
                Documento d = (Documento) f.nextElement();
                Documentos_EC doc = new Documentos_EC(d.hashCode(), d.getNombre());

                Posteo_EC post = new Posteo_EC(pal.hashCode(), doc.hashCode(), d.getCant());
                System.out.println();
                try {
                    postDao.create(post);
                } catch (Exception ex) {
                    System.out.println("Error al insertar Posteo: " + ex.getMessage());
                }
            }
        }
    }
}
