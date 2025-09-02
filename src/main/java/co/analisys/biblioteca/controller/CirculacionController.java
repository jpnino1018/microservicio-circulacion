package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.model.Prestamo;
import co.analisys.biblioteca.model.PrestamoId;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.service.CirculacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/circulacion")
public class CirculacionController {
    @Autowired
    private CirculacionService circulacionService;

    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Libro prestado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Libro o usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Operation(summary = "Prestar un libro", description = "Permite prestar un libro a un usuario")
    @PostMapping("/prestar")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public void prestarLibro(@RequestParam String usuarioId, @RequestParam String libroId) {
        circulacionService.prestarLibro(new UsuarioId(usuarioId), new LibroId(libroId));
    }

    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Libro devuelto exitosamente"),
        @ApiResponse(responseCode = "404", description = "Préstamo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Operation(summary = "Devolver un libro", description = "Permite devolver un libro prestado por un usuario")
    @PostMapping("/devolver")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public void devolverLibro(@RequestParam String prestamoId) {
        circulacionService.devolverLibro(new PrestamoId(prestamoId));
    }

    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron préstamos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Operation(summary = "Consultar todos los préstamos", description = "Permite obtener la lista de todos los préstamos")
    @GetMapping("/prestamos")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public List<Prestamo> obtenerTodosPrestamos() {
        return circulacionService.obtenerTodosPrestamos();
    }
}
