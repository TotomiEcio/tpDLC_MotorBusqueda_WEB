/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import funciones.BuscarDocumento;
import funciones.ScannerTxt;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;


/**
 *
 * @author tecio
 */

@Path("/")
public class BuscadorEndpoint {
    @Inject BuscarDocumento bd;
    @Inject ScannerTxt sc;
    
    @GET
    @Path("/buscar/{cadena}/{cant}")
    @Produces("application/json")
    public Response buscar(@PathParam("cadena") String str, @PathParam("cant") int docs){
        Object[] l = bd.buscar(str, docs);
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
        return Response.ok().build();
    }
}
