package com.aluracursos.LiteraluraV2.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String titulo;
    @ManyToOne
    private Autor autor;
    @Enumerated(EnumType.STRING)
    private Idioma lenguaje;


    public Libro(DatosLibros datosLibro){
        this.titulo = datosLibro.titulo();
        this.lenguaje = Idioma.fromString(datosLibro.idiomas().toString().split(",")[0].trim());
    }

    @Override
    public String toString() {
        String nombreAutor = (autor != null) ? autor.getNombre() : "Autor desconocido";
        return String.format("---------- Libro ----------%nTitulo:" +
                " %s%nAutor: %s%nIdioma: %s%nNumero de Descargar:" +
                " %d%n---------------------------%n",titulo,nombreAutor,lenguaje);
    }
}
