package com.aluracursos.LiteraluraV2;

import com.aluracursos.LiteraluraV2.Controller.LibroController;
import com.aluracursos.LiteraluraV2.Repository.AutorRepository;
import com.aluracursos.LiteraluraV2.Repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraV2Application implements CommandLineRunner {
	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraV2Application.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		LibroController libroController = new LibroController(libroRepository, autorRepository);
		libroController.muestraElMenu();
	}
}