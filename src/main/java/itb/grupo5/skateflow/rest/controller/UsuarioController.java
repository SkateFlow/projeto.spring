package itb.grupo5.skateflow.rest.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import itb.grupo5.skateflow.model.entity.Usuario;
import itb.grupo5.skateflow.rest.exception.ResourceNotFoundException;
import itb.grupo5.skateflow.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping("/test")
	public String getTest() {
		return "Olá, Usuario!";
	}

	// Buscar usuário por ID
	@GetMapping("/findById/{id}")
	public ResponseEntity<Usuario> findById(@PathVariable long id) {

		Usuario usuario = usuarioService.findById(id);

		if (usuario != null) {
			return ResponseEntity.ok(usuario);
		} else {
			throw new ResourceNotFoundException("Usuário não encontrado!");
		}
	}

	// Listar todos os usuários
	@GetMapping("/listar")
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = usuarioService.findAll();
		if (usuarios.isEmpty()) {
			throw new ResourceNotFoundException("Nenhum usuário encontrado!");
		}
		return ResponseEntity.ok(usuarios);
	}

	// Salvar novo usuário
	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody Usuario usuario) {

		Usuario _usuario = usuarioService.save(usuario);
		if (_usuario != null) {
			return ResponseEntity.ok("Usuário cadastrado com sucesso");
		}

		throw new ResourceNotFoundException("Usuário já cadastrado");
	}

	// Login de usuário
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Usuario usuario) {

		Usuario _usuario = usuarioService.login(usuario.getEmail(), usuario.getSenha());
		if (_usuario != null) {
			return ResponseEntity.ok(_usuario);
		}

		throw new ResourceNotFoundException("Dados Incorretos!");
	}

	// Alterar senha do usuário
	@PutMapping("/alterarSenha/{id}")
	public ResponseEntity<?> alterarSenha(@PathVariable long id, @RequestBody Usuario usuario) {

		Usuario _usuario = usuarioService.alterarSenha(id, usuario);
		if (_usuario != null) {
			return ResponseEntity.ok().body("Senha alterada com sucesso!");
		}

		throw new ResourceNotFoundException("Erro ao alterar a senha!");
	}

	// Inativar usuário
	@PutMapping("/inativar/{id}")
	public ResponseEntity<?> inativar(@PathVariable long id) {

		Usuario _usuario = usuarioService.inativar(id);
		if (_usuario != null) {
			return ResponseEntity.ok().body("Conta de usuário inativada com sucesso!");
		}

		throw new ResourceNotFoundException("Erro ao inativar a conta de usuário!");
	}

	// Reativar usuário
	@PutMapping("/reativar/{id}")
	public ResponseEntity<?> reativar(@PathVariable long id) {

		Usuario _usuario = usuarioService.reativar(id);
		if (_usuario != null) {
			return ResponseEntity.ok().body("Conta de usuário reativada com sucesso!");
		}

		throw new ResourceNotFoundException("Erro ao reativar a conta de usuário!");
	}

	// Deletar usuário
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		boolean deleted = usuarioService.delete(id);
		if (deleted) {
			return ResponseEntity.ok("Usuário deletado com sucesso!");
		}
		throw new ResourceNotFoundException("Erro ao deletar usuário!");
	}

	// Atualizar usuário e a foto (se fornecida)
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarUsuario(@PathVariable long id, @RequestParam(required = false) String nome,
			@RequestParam(required = false) String email, @RequestParam(required = false) String nivelAcesso,
			@RequestParam(required = false) String statusUsuario, @RequestParam(required = false) MultipartFile foto)
			throws IOException {

		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setNivelAcesso(nivelAcesso);
		usuario.setStatusUsuario(statusUsuario);

		Usuario _usuario = usuarioService.atualizarUsuario(id, usuario, foto);
		if (_usuario != null) {
			return ResponseEntity.ok().body("Usuário atualizado com sucesso!");
		}

		throw new ResourceNotFoundException("Erro ao atualizar o usuário!");
	}

	// Obter foto do usuário em Base64
	@GetMapping("/foto/{id}")
	public ResponseEntity<String> getFotoBase64(@PathVariable long id) {
		String fotoBase64 = usuarioService.getFotoAsBase64(id);
		if (fotoBase64 != null) {
			return ResponseEntity.ok(fotoBase64);
		}
		throw new ResourceNotFoundException("Foto não encontrada para o usuário!");
	}

	// Salvar foto do usuário via Base64
	@PutMapping("/foto/{id}")
	public ResponseEntity<?> salvarFotoBase64(@PathVariable long id, @RequestBody String fotoBase64) {
		Usuario usuario = usuarioService.salvarFotoBase64(id, fotoBase64);
		if (usuario != null) {
			return ResponseEntity.ok("Foto salva com sucesso!");
		}
		throw new ResourceNotFoundException("Erro ao salvar foto do usuário!");
	}
}
