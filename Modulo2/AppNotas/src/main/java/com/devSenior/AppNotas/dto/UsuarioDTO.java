package com.devSenior.AppNotas.dto;


//! Datos que envia el cliente para registrarse

import com.devSenior.AppNotas.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "El usuario debe estar entre 3 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatorio")
    @Size(min = 6, message = "La contraseña debe tener min 6 caracteres")
    private String password;

    private Rol rol;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
