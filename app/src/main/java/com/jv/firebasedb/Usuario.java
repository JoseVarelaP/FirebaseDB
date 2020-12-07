package com.jv.firebasedb;

public class Usuario {
    private String Nombres = "";
    private String Apellidos = "";
    private String Direccion = "";
    private int Edad = 0;
    private String Telefono = "";

    public Usuario() {}

    public Usuario(String nombre, String apellidos, String direccion, int edad, String telefono) {
        Nombres = nombre;
        Apellidos = apellidos;
        Direccion = direccion;
        Edad = edad;
        Telefono = telefono;
    }

    public String getNombre() { return Nombres; }
    public void setNombre(String nombre) { Nombres = nombre; }
    public String getApellidos() { return Apellidos; }
    public void setApellidos(String apellidos) { Apellidos = apellidos; }
    public String getDireccion() { return Direccion; }
    public void setDireccion(String direccion) { Direccion = direccion; }
    public int getEdad() { return Edad; }
    public void setEdad(int edad) { Edad = edad; }
    public String getTelefono() { return Telefono; }
    public void setTelefono(String telefono) { Telefono = telefono;  }
}
