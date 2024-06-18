package com.aluracursos.LiteraluraV2.Controller;

import com.aluracursos.LiteraluraV2.Entity.*;
import com.aluracursos.LiteraluraV2.Repository.AutorRepository;
import com.aluracursos.LiteraluraV2.Repository.LibroRepository;
import com.aluracursos.LiteraluraV2.Service.ConsumoAPI;
import com.aluracursos.LiteraluraV2.Service.ConvierteDatos;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@RestController
public class LibroController {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> listasLibros;
    private List<Autor> datosAutors;

    public LibroController(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                                   Proyecto Literalura mnj,
                                        
                                  Opciones disponibles:
                                        
                    ____________________________________________________
                    | 1.- Buscar Libro por titulo.                     |
                    | 2.- Listar libros registrados.                   |
                    | 3.- Listar autores registrados.                  |
                    | 4.- Listar autores vivos en determinado periodo. |
                    | 5.- Listar libros por idioma.                    |
                    |           *Idiomas Disponibles*                  |
                    |      - Español   (es)                            |
                    |      - Ingles    (en)                            |
                    |      - Frances   (fr)                            |
                    |      - Portugues (pt)                            |
                    |                                                  |
                    | 0.- Salir.                                       |
                    |__________________________________________________|
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;

                case 2:
                    listarLibrosRegistrados();
                    break;

                case 3:
                    mostrarAutores();
                    break;

                case 4:
                    listarAutoresVivos();
                    break;

                case 5:
                    buscarLibroPorIdioma();
                    break;

                case 0:
                    System.out.println("*** Cerrando la aplicación ***");
                    break;
                default:
                    System.out.println("Opcion inválida");
            }
        }
    }

    private DatosBusqueda getBusqueda() {
        System.out.println("Nombre del libro a buscar: ");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        DatosBusqueda datos = conversor.convierteDatos(json, DatosBusqueda.class);
        return datos;

    }

    private void buscarLibroPorTitulo() {
        DatosBusqueda datosBusqueda = getBusqueda();
        if (datosBusqueda != null && !datosBusqueda.resultado().isEmpty()) {
            DatosLibros primerLibro = datosBusqueda.resultado().get(0);


            Libro libro = new Libro(primerLibro);
            System.out.println("Resultado de la busqueda de Libro");
            System.out.println(libro);


            Optional<Libro> libroExiste = libroRepository.findByTitulo(libro.getTitulo());
            if (libroExiste.isPresent()){
                System.out.println("Libro ya registrado");
            }else {

                if (!primerLibro.autor().isEmpty()) {
                    DatosAutor autor = primerLibro.autor().get(0);
                    Autor autor1 = new Autor(autor);
                    Optional<Autor> autorOptional = autorRepository.findByNombre(autor1.getNombre());

                    if (autorOptional.isPresent()) {
                        Autor autorExiste = autorOptional.get();
                        libro.setAutor(autorExiste);
                        libroRepository.save(libro);
                    } else {
                        Autor autorNuevo = autorRepository.save(autor1);
                        libro.setAutor(autorNuevo);
                        libroRepository.save(libro);
                    }

                } else {
                    System.out.println("Autor no encontrado");
                }
            }
        } else {
            System.out.println("libro no encontrado");
        }
    }
    private void listarLibrosRegistrados() {
        listasLibros = libroRepository.findAll();
        listasLibros.stream()
                .forEach(System.out::println);
    }

    private void mostrarAutores() {
        datosAutors = autorRepository.findAll();
        datosAutors.stream()
                .forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año para la busqueda: ");
        var anio = teclado.nextInt();
        datosAutors = autorRepository.listaAutoresVivosPorAnio(anio);
        datosAutors.stream()
                .forEach(System.out::println);
    }

    private List<Libro> datosBusquedaLenguaje(String idioma){
        var dato = Idioma.fromString(idioma);
        System.out.println("Lenguaje: " + dato);

        List<Libro> libroPorIdioma = libroRepository.findByLenguaje(dato);
        return libroPorIdioma;
    }

    private void buscarLibroPorIdioma(){
        System.out.println("Indique el lenguaje a buscar");

        var opcion = -1;
        while (opcion != 0) {
            var opciones = """
                               *Idiomas Disponibles*                  
                         1 - Español   (es)                            
                         2 - Ingles    (en)                            
                         3 - Frances   (fr)                            
                         4 - Portugues (pt)   
                    
                    0. Volver a Las opciones anteriores
                    """;
            System.out.println(opciones);
            while (!teclado.hasNextInt()) {
                System.out.println("Formato inválido, ingrese un número que esté disponible en el menú");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    List<Libro> libroES = datosBusquedaLenguaje("[es]");
                    libroES.forEach(System.out::println);
                    break;
                case 2:
                    List<Libro> libroEN = datosBusquedaLenguaje("[en]");
                    libroEN.forEach(System.out::println);
                    break;
                case 3:
                    List<Libro> libroFR = datosBusquedaLenguaje("[fr]");
                    libroFR.forEach(System.out::println);
                    break;
                case 4:
                    List<Libro> libroPT = datosBusquedaLenguaje("[pt]");
                    libroPT.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Ningún idioma seleccionado");
            }
        }
    }
}
