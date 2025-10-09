package itb.grupo5.skateflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import itb.grupo5.skateflow.model.entity.Lugar;
import itb.grupo5.skateflow.repository.LugarRepository;
import java.util.Base64;
import jakarta.transaction.Transactional;

@Service
public class LugarService {

	private final LugarRepository lugarRepository;

	public LugarService(LugarRepository lugarRepository) {
		this.lugarRepository = lugarRepository;
	}

	public Lugar save(Lugar lugar) {
		return lugarRepository.save(lugar);
	}

	public Lugar findById(Long id) {
		return lugarRepository.findById(id).orElse(null);
	}

	public List<Lugar> findAll() {
		return lugarRepository.findAll();
	}

	public Lugar update(Long id, Lugar novoLugar) {
		Optional<Lugar> optionalLugar = lugarRepository.findById(id);

		if (optionalLugar.isPresent()) {
			Lugar lugarExistente = optionalLugar.get();

			lugarExistente.setNome(novoLugar.getNome());
			lugarExistente.setDescricao(novoLugar.getDescricao());
			lugarExistente.setTipo(novoLugar.getTipo());
			lugarExistente.setCep(novoLugar.getCep());
			lugarExistente.setLatitude(novoLugar.getLatitude());
			lugarExistente.setLongitude(novoLugar.getLongitude());
			lugarExistente.setNumero(novoLugar.getNumero());
			lugarExistente.setFoto1(novoLugar.getFoto1());
			lugarExistente.setFoto2(novoLugar.getFoto2());
			lugarExistente.setFoto3(novoLugar.getFoto3());
			lugarExistente.setValor(novoLugar.getValor());
			lugarExistente.setStatusPista(novoLugar.getStatusPista());
			lugarExistente.setCategoria(novoLugar.getCategoria());
			lugarExistente.setUsuario(novoLugar.getUsuario()); // Atualizando o usuário

			return lugarRepository.save(lugarExistente);
		}

		return null;
	}

	public boolean delete(Long id) {
		Optional<Lugar> optionalLugar = lugarRepository.findById(id);
		if (optionalLugar.isPresent()) {
			lugarRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// Converter fotos da pista para Base64
	public String getFoto1AsBase64(Long id) {
		Lugar lugar = findById(id);
		return lugar != null && lugar.getFoto1() != null ? Base64.getEncoder().encodeToString(lugar.getFoto1()) : null;
	}

	public String getFoto2AsBase64(Long id) {
		Lugar lugar = findById(id);
		return lugar != null && lugar.getFoto2() != null ? Base64.getEncoder().encodeToString(lugar.getFoto2()) : null;
	}

	public String getFoto3AsBase64(Long id) {
		Lugar lugar = findById(id);
		return lugar != null && lugar.getFoto3() != null ? Base64.getEncoder().encodeToString(lugar.getFoto3()) : null;
	}

	// Salvar fotos a partir de Base64
	@Transactional
	public Lugar salvarFoto1Base64(Long id, String fotoBase64) {
		Optional<Lugar> _lugar = lugarRepository.findById(id);
		if (_lugar.isPresent() && fotoBase64 != null && !fotoBase64.trim().isEmpty()) {
			Lugar lugar = _lugar.get();
			lugar.setFoto1(Base64.getDecoder().decode(fotoBase64));
			return lugarRepository.save(lugar);
		}
		return null;
	}

	@Transactional
	public Lugar salvarFoto2Base64(Long id, String fotoBase64) {
		Optional<Lugar> _lugar = lugarRepository.findById(id);
		if (_lugar.isPresent() && fotoBase64 != null && !fotoBase64.trim().isEmpty()) {
			Lugar lugar = _lugar.get();
			lugar.setFoto2(Base64.getDecoder().decode(fotoBase64));
			return lugarRepository.save(lugar);
		}
		return null;
	}

	@Transactional
	public Lugar salvarFoto3Base64(Long id, String fotoBase64) {
		Optional<Lugar> _lugar = lugarRepository.findById(id);
		if (_lugar.isPresent() && fotoBase64 != null && !fotoBase64.trim().isEmpty()) {
			Lugar lugar = _lugar.get();
			lugar.setFoto3(Base64.getDecoder().decode(fotoBase64));
			return lugarRepository.save(lugar);
		}
		return null;
	}
}
