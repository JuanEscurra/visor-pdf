package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@RestController
	@CrossOrigin(origins = "*")
	class FileController {

		@GetMapping("/files/{filename:.+}")
		public ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request) throws IOException {
			// Cargar el archivo desde la carpeta de recursos
			Resource resource = new ClassPathResource("static/" + filename);

			// Verificar si el archivo existe
			if (resource.exists()) {
				// Obtener el tipo MIME del archivo
				String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

				// Crear la respuesta con el archivo adjunto
				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(contentType))
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
						.body(new InputStreamResource(resource.getInputStream()));
			} else {
				// Manejar el caso en el que el archivo no existe
				return ResponseEntity.notFound().build();
			}
		}
	}
}
