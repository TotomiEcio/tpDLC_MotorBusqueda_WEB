/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicaHash;

import java.util.Objects;

/**
 *
 * @author tecio
 */
public class Documento implements Comparable<Documento>{
    public String nombre;
    public int termFrec = 0;
    public int idf;
    public double idr;
    public String path;

    public Documento(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCant() {
        return termFrec;
    }

    public void addCant() {
        this.termFrec ++;
    }
    
    public double getIdr(){
        return idr;
    }
    public void setIdr(double idr){
        this.idr = idr;
    }
    
    public void setPath(String path){
        this.path = path;
    }
    public String getPath(){
        return this.path;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.nombre);
        return hash;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Documento other = (Documento) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Documento{" + "nombre=" + nombre + ", idr=" + idr + ", path=" + path  + "} \n";
    }

    @Override
    public int compareTo(Documento o) {
        return (int) (o.idr * 1000 - this.idr * 1000); 
    }
}
