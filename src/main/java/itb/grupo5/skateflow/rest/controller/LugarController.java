package itb.grupo5.skateflow.rest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itb.grupo5.skateflow.model.entity.Lugar;
import itb.grupo5.skateflow.rest.exception.ResourceNotFoundException;
import itb.grupo5.skateflow.service.LugarService;

@RestController
@RequestMapping("/lugar")
public class LugarController {

	private final LugarService lugarService;

	// Construtor que injeta o service LugarService
	public LugarController(LugarService lugarService) {
		this.lugarService = lugarService;
	}

	// Teste básico para verificar se a API está funcionando
	@GetMapping("/test")
	public String test() {
		return "Olá, Lugar!";
	}

	// Endpoint para salvar um novo lugar
	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody Lugar lugar) {
		Lugar novoLugar = lugarService.save(lugar); // Chama o serviço para salvar o lugar
		if (novoLugar != null) {
			return ResponseEntity.ok("Lugar cadastrado com sucesso");
		}
		throw new ResourceNotFoundException("Erro ao cadastrar lugar");
	}

	// Endpoint para listar todos os lugares
	@GetMapping("/listar")
	public ResponseEntity<List<Lugar>> findAll() {
		List<Lugar> lugares = lugarService.findAll(); // Busca todos os lugares no serviço
		if (lugares.isEmpty()) {
			throw new ResourceNotFoundException("Nenhum lugar encontrado");
		}
		return ResponseEntity.ok(lugares); // Retorna a lista de lugares
	}

	// Endpoint para buscar um lugar por ID
	@GetMapping("/findById/{id}")
	public ResponseEntity<Lugar> findById(@PathVariable Long id) {
		Lugar lugar = lugarService.findById(id); // Busca o lugar por ID no serviço
		if (lugar != null) {
			return ResponseEntity.ok(lugar); // Retorna o lugar encontrado
		}
		throw new ResourceNotFoundException("Lugar não encontrado");
	}

	// Endpoint para atualizar um lugar existente
	@PutMapping("/update/{id}")
	public ResponseEntity<Lugar> update(@PathVariable Long id, @RequestBody Lugar lugar) {
		Lugar atualizado = lugarService.update(id, lugar); // Chama o serviço para atualizar o lugar
		if (atualizado != null) {
			return ResponseEntity.ok(atualizado); // Retorna o lugar atualizado
		}
		throw new ResourceNotFoundException("Lugar não encontrado para atualização");
	}

	// Endpoint para deletar um lugar
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		boolean deleted = lugarService.delete(id); // Chama o serviço para deletar o lugar
		if (deleted) {
			return ResponseEntity.ok("Lugar deletado com sucesso!");
		}
		throw new ResourceNotFoundException("Erro ao deletar lugar!");
	}
}
