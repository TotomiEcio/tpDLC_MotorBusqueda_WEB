/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import JpaControllers.DocumentosJpaController;
import funciones.BuscarDocumento;
import funciones.ScannerTxt;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import persistencia.*;

/**
 *
 * @author tecio
 */

@Path("/")
public class BuscadorEndpoint {
    @Inject public BuscarDocumento bd;
    @Inject public ScannerTxt sc;

    @GET
    @Path("/buscar/{cadena}")
    @Produces("application/json")
    public Response buscar(@PathParam("cadena") String str){
        Object[] l = bd.buscar(str, 5);
        return Response.ok(l).build();
    }
    
    @GET
    @Path("/indexar")
    @Produces("application/json")
    public Response indexar(){
        try {
            sc.indexarCarpeta();
        } catch (Exception e) {
        }
        return Response.ok("Documentos indexados").build();
    }
    
    @GET
    public Response index(){
        return Response.ok("Funciona la poronga esta").build();
    }
}
