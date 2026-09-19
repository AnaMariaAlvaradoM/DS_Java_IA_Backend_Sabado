package com.devsenior.campusflow.usuarios.model;

import java.util.ArrayList;
import java.util.List;
import com.devsenior.campusflow.cursos.model.Curso;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    @Embedded
    private PreferenciasUsuario preferencias = new PreferenciasUsuario();

    @OneToMany(mappedBy = "instructor")
    private List<Curso> cursosDictados = new ArrayList<>();

    @ManyToMany(mappedBy = "estudiantes")
    private List<Curso> cursosInscritos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

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

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public PreferenciasUsuario getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(PreferenciasUsuario preferencias) {
        this.preferencias = preferencias;
    }

    public List<Curso> getCursosDictados() {
        return cursosDictados;
    }

    public void setCursosDictados(List<Curso> cursosDictados) {
        this.cursosDictados = cursosDictados;
    }

    public List<Curso> getCursosInscritos() {
        return cursosInscritos;
    }

    public void setCursosInscritos(List<Curso> cursosInscritos) {
        this.cursosInscritos = cursosInscritos;
    }
}
