package com.JavaSabados.gestor_productos.controller;

import com.JavaSabados.gestor_productos.model.Producto;
import com.JavaSabados.gestor_productos.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> obtenerProductos() {
        return productoService.listarProductos();
    }

    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.agregarProducto(producto);
    }
}