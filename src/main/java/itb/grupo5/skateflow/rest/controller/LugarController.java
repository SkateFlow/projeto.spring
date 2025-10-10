package itb.grupo5.skateflow.rest.controller;

import java.util.List;
import java.util.Base64;
import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itb.grupo5.skateflow.model.entity.Lugar;
import itb.grupo5.skateflow.model.entity.Categoria;
import itb.grupo5.skateflow.model.entity.Usuario;
import itb.grupo5.skateflow.model.dto.LugarDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import itb.grupo5.skateflow.rest.exception.ResourceNotFoundException;
import itb.grupo5.skateflow.service.LugarService;
import itb.grupo5.skateflow.service.CategoriaService;
import itb.grupo5.skateflow.service.UsuarioService;

@RestController
@RequestMapping("/lugar")
public class LugarController {

	private final LugarService lugarService;
	private final CategoriaService categoriaService;
	private final UsuarioService usuarioService;

	// Construtor que injeta os services
	public LugarController(LugarService lugarService, CategoriaService categoriaService, UsuarioService usuarioService) {
		this.lugarService = lugarService;
		this.categoriaService = categoriaService;
		this.usuarioService = usuarioService;
	}

	// Teste básico para verificar se a API está funcionando
	@GetMapping("/test")
	public String test() {
		return "Olá, Lugar!";
	}

	// Endpoint para salvar um novo lugar
	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody java.util.Map<String, Object> request) {
		try {
			// Verificar se tem categoriaId (indica que é DTO)
			if (request.containsKey("categoriaId")) {
				System.out.println("=== PROCESSANDO DTO ===");
				System.out.println("Request: " + request);
				
				ObjectMapper mapper = new ObjectMapper();
				LugarDTO lugarDTO = mapper.convertValue(request, LugarDTO.class);
				
				System.out.println("CategoriaId: " + lugarDTO.getCategoriaId());
				System.out.println("UsuarioId: " + lugarDTO.getUsuarioId());
				
				// Buscar categoria
				Categoria categoria = categoriaService.findById(lugarDTO.getCategoriaId());
				System.out.println("Categoria encontrada: " + (categoria != null ? categoria.getNome() : "null"));
				if (categoria == null) {
					throw new ResourceNotFoundException("Categoria não encontrada com ID: " + lugarDTO.getCategoriaId());
				}

				// Buscar usuário
				Usuario usuario = usuarioService.findById(lugarDTO.getUsuarioId());
				System.out.println("Usuario encontrado: " + (usuario != null ? usuario.getNome() : "null"));
				if (usuario == null) {
					throw new ResourceNotFoundException("Usuário não encontrado com ID: " + lugarDTO.getUsuarioId());
				}

				// Criar entidade Lugar
				Lugar lugar = new Lugar();
				lugar.setNome(lugarDTO.getNome());
				lugar.setDescricao(lugarDTO.getDescricao());
				lugar.setTipo(lugarDTO.getTipo());
				lugar.setCep(lugarDTO.getCep());
				lugar.setNumero(lugarDTO.getNumero());
				lugar.setLatitude(lugarDTO.getLatitude());
				lugar.setLongitude(lugarDTO.getLongitude());
				lugar.setValor(lugarDTO.getValor() != null ? lugarDTO.getValor() : BigDecimal.ZERO);
				lugar.setStatusPista(lugarDTO.getStatusPista());
				lugar.setCategoria(categoria);
				lugar.setUsuario(usuario);

				// Converter fotos base64 para byte[]
				if (lugarDTO.getFoto1() != null && !lugarDTO.getFoto1().isEmpty()) {
					try {
						lugar.setFoto1(Base64.getDecoder().decode(lugarDTO.getFoto1()));
					} catch (Exception e) {
						System.err.println("Erro ao decodificar foto1: " + e.getMessage());
					}
				}
				if (lugarDTO.getFoto2() != null && !lugarDTO.getFoto2().isEmpty()) {
					try {
						lugar.setFoto2(Base64.getDecoder().decode(lugarDTO.getFoto2()));
					} catch (Exception e) {
						System.err.println("Erro ao decodificar foto2: " + e.getMessage());
					}
				}
				if (lugarDTO.getFoto3() != null && !lugarDTO.getFoto3().isEmpty()) {
					try {
						lugar.setFoto3(Base64.getDecoder().decode(lugarDTO.getFoto3()));
					} catch (Exception e) {
						System.err.println("Erro ao decodificar foto3: " + e.getMessage());
					}
				}

				Lugar novoLugar = lugarService.save(lugar);
				if (novoLugar != null) {
					return ResponseEntity.ok("Lugar criado com sucesso");
				}
			} else {
				// Processar como entidade Lugar normal
				ObjectMapper mapper = new ObjectMapper();
				Lugar lugar = mapper.convertValue(request, Lugar.class);
				Lugar novoLugar = lugarService.save(lugar);
				if (novoLugar != null) {
					return ResponseEntity.ok("Lugar cadastrado com sucesso");
				}
			}
			throw new ResourceNotFoundException("Erro ao cadastrar lugar");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotFoundException("Erro ao processar solicitação: " + e.getMessage());
		}
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

	// Endpoints para obter fotos em Base64
	@GetMapping("/foto1/{id}")
	public ResponseEntity<String> getFoto1Base64(@PathVariable Long id) {
		String foto = lugarService.getFoto1AsBase64(id);
		return foto != null ? ResponseEntity.ok(foto) : ResponseEntity.notFound().build();
	}

	@GetMapping("/foto2/{id}")
	public ResponseEntity<String> getFoto2Base64(@PathVariable Long id) {
		String foto = lugarService.getFoto2AsBase64(id);
		return foto != null ? ResponseEntity.ok(foto) : ResponseEntity.notFound().build();
	}

	@GetMapping("/foto3/{id}")
	public ResponseEntity<String> getFoto3Base64(@PathVariable Long id) {
		String foto = lugarService.getFoto3AsBase64(id);
		return foto != null ? ResponseEntity.ok(foto) : ResponseEntity.notFound().build();
	}

	// Endpoints para salvar fotos via Base64
	@PutMapping("/foto1/{id}")
	public ResponseEntity<?> salvarFoto1Base64(@PathVariable Long id, @RequestBody String fotoBase64) {
		Lugar lugar = lugarService.salvarFoto1Base64(id, fotoBase64);
		return lugar != null ? ResponseEntity.ok("Foto 1 salva com sucesso!") : ResponseEntity.badRequest().body("Erro ao salvar foto 1!");
	}

	@PutMapping("/foto2/{id}")
	public ResponseEntity<?> salvarFoto2Base64(@PathVariable Long id, @RequestBody String fotoBase64) {
		Lugar lugar = lugarService.salvarFoto2Base64(id, fotoBase64);
		return lugar != null ? ResponseEntity.ok("Foto 2 salva com sucesso!") : ResponseEntity.badRequest().body("Erro ao salvar foto 2!");
	}

	@PutMapping("/foto3/{id}")
	public ResponseEntity<?> salvarFoto3Base64(@PathVariable Long id, @RequestBody String fotoBase64) {
		Lugar lugar = lugarService.salvarFoto3Base64(id, fotoBase64);
		return lugar != null ? ResponseEntity.ok("Foto 3 salva com sucesso!") : ResponseEntity.badRequest().body("Erro ao salvar foto 3!");
	}
}
