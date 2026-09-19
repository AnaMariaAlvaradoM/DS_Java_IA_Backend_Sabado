package com.JavaSabados.gestor_productos.dto;

public class RegistroRequest {
    private String email;
    private String password;
    //private Strin nombre;
    //private Strin apellido;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
