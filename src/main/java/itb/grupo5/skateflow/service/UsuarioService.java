package itb.grupo5.skateflow.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import itb.grupo5.skateflow.model.entity.Usuario;
import itb.grupo5.skateflow.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.Base64;

@Service
public class UsuarioService {

	private UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	// Buscar usuário por ID
	public Usuario findById(long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if (usuario.isPresent()) {
			return usuario.get();
		}
		return null;
	}

	// Listar todos os usuários
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	// Salvar novo usuário
	@Transactional
	public Usuario save(Usuario usuario) {
		Usuario _usuario = usuarioRepository.findByEmail(usuario.getEmail());

		if (_usuario == null) {
			// Codificar a senha
			String senha = Base64.getEncoder().encodeToString(usuario.getSenha().getBytes());

			// Definir outros atributos
			usuario.setSenha(senha);
			usuario.setDataCadastro(LocalDateTime.now());
			usuario.setStatusUsuario("ATIVO");

			return usuarioRepository.save(usuario);
		}
		return null;
	}

	// Deletar usuário
	public boolean delete(long id) {
		Usuario usuario = findById(id);
		if (usuario != null) {
			usuarioRepository.delete(usuario);
			return true;
		}
		return false;
	}

	// Login de usuário
	@Transactional
	public Usuario login(String email, String senha) {
		Usuario _usuario = usuarioRepository.findByEmail(email);

		if (_usuario != null) {
			if (_usuario.getStatusUsuario().equals("ATIVO")) {
				byte[] decodedPass = Base64.getDecoder().decode(_usuario.getSenha());

				if (new String(decodedPass).equals(senha)) {
					return _usuario;
				}
			}
		}
		return null;
	}

	// Alterar senha do usuário
	@Transactional
	public Usuario alterarSenha(long id, Usuario usuario) {
		Optional<Usuario> _usuario = usuarioRepository.findById(id);

		if (_usuario.isPresent()) {
			Usuario usuarioAtualizado = _usuario.get();
			String senha = Base64.getEncoder().encodeToString(usuario.getSenha().getBytes());

			usuarioAtualizado.setSenha(senha);
			// Mantém a data de cadastro original
			usuarioAtualizado.setStatusUsuario("ATIVO");

			return usuarioRepository.save(usuarioAtualizado);
		}
		return null;
	}

	// Inativar usuário
	@Transactional
	public Usuario inativar(long id) {
		Optional<Usuario> _usuario = usuarioRepository.findById(id);

		String senhaPadrao = "12345678"; // Senha padrão para inativação

		if (_usuario.isPresent()) {
			Usuario usuarioAtualizado = _usuario.get();
			String senha = Base64.getEncoder().encodeToString(senhaPadrao.getBytes());

			usuarioAtualizado.setSenha(senha);
			usuarioAtualizado.setStatusUsuario("INATIVO");

			return usuarioRepository.save(usuarioAtualizado);
		}
		return null;
	}

	// Reativar usuário
	@Transactional
	public Usuario reativar(long id) {
		Optional<Usuario> _usuario = usuarioRepository.findById(id);

		String senhaPadrao = "12345678"; // Senha padrão para reativação

		if (_usuario.isPresent()) {
			Usuario usuarioAtualizado = _usuario.get();
			String senha = Base64.getEncoder().encodeToString(senhaPadrao.getBytes());

			usuarioAtualizado.setSenha(senha);
			usuarioAtualizado.setStatusUsuario("ATIVO");

			return usuarioRepository.save(usuarioAtualizado);
		}
		return null;
	}

	// Atualizar usuário (sem alterar a senha)
	@Transactional
	public Usuario atualizarUsuario(long id, Usuario usuario, MultipartFile foto) throws IOException {
		Optional<Usuario> _usuario = usuarioRepository.findById(id);

		if (_usuario.isPresent()) {
			Usuario usuarioAtualizado = _usuario.get();

			// Atualizar dados do usuário
			if (usuario.getNome() != null) {
				usuarioAtualizado.setNome(usuario.getNome());
			}
			if (usuario.getEmail() != null) {
				usuarioAtualizado.setEmail(usuario.getEmail());
			}
			if (usuario.getNivelAcesso() != null) {
				usuarioAtualizado.setNivelAcesso(usuario.getNivelAcesso());
			}
			if (usuario.getStatusUsuario() != null) {
				usuarioAtualizado.setStatusUsuario(usuario.getStatusUsuario());
			}

			// Se foto foi fornecida, atualize a foto do usuário
			if (foto != null && !foto.isEmpty()) {
				byte[] fotoBytes = foto.getBytes(); // Convertendo a foto para byte[]
				usuarioAtualizado.setFoto(fotoBytes);
			}

			return usuarioRepository.save(usuarioAtualizado);
		}

		return null;
	}

	// Converter foto do usuário para Base64
	public String getFotoAsBase64(long id) {
		Usuario usuario = findById(id);
		if (usuario != null && usuario.getFoto() != null) {
			return Base64.getEncoder().encodeToString(usuario.getFoto());
		}
		return null;
	}

	// Salvar foto a partir de Base64
	@Transactional
	public Usuario salvarFotoBase64(long id, String fotoBase64) {
		Optional<Usuario> _usuario = usuarioRepository.findById(id);
		if (_usuario.isPresent() && fotoBase64 != null && !fotoBase64.trim().isEmpty()) {
			Usuario usuario = _usuario.get();
			try {
				byte[] fotoBytes = Base64.getDecoder().decode(fotoBase64);
				usuario.setFoto(fotoBytes);
				return usuarioRepository.save(usuario);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("String Base64 inválida para foto", e);
			}
		}
		return null;
	}
}
