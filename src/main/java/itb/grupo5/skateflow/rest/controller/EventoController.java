package itb.grupo5.skateflow.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import itb.grupo5.skateflow.model.entity.Evento;
import itb.grupo5.skateflow.model.entity.Usuario;
import itb.grupo5.skateflow.rest.exception.ResourceNotFoundException;
import itb.grupo5.skateflow.service.EventoService;

@RestController
@RequestMapping("/evento")
public class EventoController {
	
		
		private EventoService eventoService;
		
		public EventoController(EventoService eventoService) {
			super();
			this.eventoService = eventoService;
	}
		
		@GetMapping("/test")
		public String getTest() {
			return "Olá, Evento!";
		}
		
		@GetMapping("/findById/{id}")
		public ResponseEntity<Evento> findById(@PathVariable long id) {
			
			Evento evento = eventoService.findById(id);
			
			if (evento != null) {
				return new ResponseEntity<Evento>(evento, HttpStatus.OK);
			} else {
				throw new ResourceNotFoundException("Evento não encontrado!");
			}
		}		
			
		@GetMapping("/findAll")
		public ResponseEntity<List<Evento>> findAll(){
			
			List<Evento> eventos = eventoService.findAll();
			
			return new ResponseEntity<List<Evento>>(eventos, HttpStatus.OK);
		}
		
		@GetMapping("/listar")
		public ResponseEntity<List<Evento>> listarEventos() {
		    List<Evento> eventos = eventoService.findAll(); // método do service
		    if (eventos.isEmpty()) {
		        throw new ResourceNotFoundException("Nenhum evento encontrado!");
		    }
		    return ResponseEntity.ok(eventos);
		}	
		
		@PostMapping("/save")
		public ResponseEntity<?> save(@RequestBody Evento evento) {

			Evento _evento = eventoService.save(evento);
			if (_evento != null) {
				return ResponseEntity.ok("Evento cadastrado com sucesso");
			}

			throw new ResourceNotFoundException("Evento já cadastrado");
		}
		
		@PutMapping("/update/{id}")
		public ResponseEntity<Evento> update(@PathVariable long id, @RequestBody Evento evento) {
		    Evento eventoAtualizado = eventoService.update(id, evento);
		    if (eventoAtualizado != null) {
		        return ResponseEntity.ok(eventoAtualizado);
		    } else {
		        throw new ResourceNotFoundException("Evento não encontrado para atualização.");
		    }
		}
		
		@DeleteMapping("/delete/{id}")
		public ResponseEntity<?> delete(@PathVariable long id) {
		    boolean deleted = eventoService.delete(id);
		    if (deleted) {
		        return ResponseEntity.ok("Evento deletado com sucesso!");
		    }
		    throw new ResourceNotFoundException("Erro ao deletar evento!");
		}

		// Endpoints para obter fotos em Base64
		@GetMapping("/foto1/{id}")
		public ResponseEntity<String> getFoto1Base64(@PathVariable long id) {
			String foto = eventoService.getFoto1AsBase64(id);
			return foto != null ? ResponseEntity.ok(foto) : ResponseEntity.notFound().build();
		}

		@GetMapping("/foto2/{id}")
		public ResponseEntity<String> getFoto2Base64(@PathVariable long id) {
			String foto = eventoService.getFoto2AsBase64(id);
			return foto != null ? ResponseEntity.ok(foto) : ResponseEntity.notFound().build();
		}

		@GetMapping("/foto3/{id}")
		public ResponseEntity<String> getFoto3Base64(@PathVariable long id) {
			String foto = eventoService.getFoto3AsBase64(id);
			return foto != null ? ResponseEntity.ok(foto) : ResponseEntity.notFound().build();
		}

		// Endpoints para salvar fotos via Base64
		@PutMapping("/foto1/{id}")
		public ResponseEntity<?> salvarFoto1Base64(@PathVariable long id, @RequestBody String fotoBase64) {
			Evento evento = eventoService.salvarFoto1Base64(id, fotoBase64);
			return evento != null ? ResponseEntity.ok("Foto 1 salva com sucesso!") : ResponseEntity.badRequest().body("Erro ao salvar foto 1!");
		}

		@PutMapping("/foto2/{id}")
		public ResponseEntity<?> salvarFoto2Base64(@PathVariable long id, @RequestBody String fotoBase64) {
			Evento evento = eventoService.salvarFoto2Base64(id, fotoBase64);
			return evento != null ? ResponseEntity.ok("Foto 2 salva com sucesso!") : ResponseEntity.badRequest().body("Erro ao salvar foto 2!");
		}

		@PutMapping("/foto3/{id}")
		public ResponseEntity<?> salvarFoto3Base64(@PathVariable long id, @RequestBody String fotoBase64) {
			Evento evento = eventoService.salvarFoto3Base64(id, fotoBase64);
			return evento != null ? ResponseEntity.ok("Foto 3 salva com sucesso!") : ResponseEntity.badRequest().body("Erro ao salvar foto 3!");
		}
}
