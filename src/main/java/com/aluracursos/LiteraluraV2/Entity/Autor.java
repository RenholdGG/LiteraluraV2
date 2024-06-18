package com.aluracursos.LiteraluraV2.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "autores")

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private Integer fecha_nacimiento;
    private Integer fecha_deceso;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libro;

    public Autor(DatosAutor datosAutor){
        this.nombre=datosAutor.nombre();
        this.fecha_nacimiento = datosAutor.fechaNacimiento();
        this.fecha_deceso = datosAutor.fechaFallecimiento();
    }

    @Override
    public String toString() {
        StringBuilder librosStr = new StringBuilder();
        librosStr.append("Libros: ");

        for(int i = 0; i < libro.size() ; i++) {
            librosStr.append(libro.get(i).getTitulo());
            if (i < libro.size() - 1 ){
                librosStr.append(", ");
            }
        }
        return String.format("---------- Autor ----------%nNombre:" +
                " %s%n%s%nFecha de Nacimiento: %s%nFecha de Deceso:" +
                " %s%n---------------------------%n",nombre,librosStr.toString(),fecha_nacimiento,fecha_deceso);
    }
}
