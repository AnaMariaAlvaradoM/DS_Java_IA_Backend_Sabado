package com.devSenior.AppNotas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;                 // Mongo usa String para el _id

//    private String nombre;
//    private String apellido;
    private String username;
    private String password;           // siempre encriptada con BCrypt
    private Rol rol;

    // Constructor vacio: Spring Data lo necesita para construir el objeto
    public Usuario() {
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}