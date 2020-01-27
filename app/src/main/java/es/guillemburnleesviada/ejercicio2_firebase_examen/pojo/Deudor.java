package es.guillemburnleesviada.ejercicio2_firebase_examen.pojo;

import com.google.firebase.database.DatabaseReference;

public class Deudor {

    private String nombre;
    private float deuda;
    private DatabaseReference ref;

    public Deudor(String nombre, float deuda, DatabaseReference ref) {
        this.nombre = nombre;
        this.deuda = deuda;
        this.ref = ref;
    }

    public Deudor() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getDeuda() {
        return deuda;
    }

    public void setDeuda(float deuda) {
        this.deuda = deuda;
    }

    public DatabaseReference getRef() {
        return ref;
    }

    public void setRef(DatabaseReference ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "Deudor{" +
                "nombre='" + nombre + '\'' +
                ", deuda=" + deuda +
                '}';
    }
}
