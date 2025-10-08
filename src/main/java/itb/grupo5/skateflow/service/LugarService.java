package itb.grupo5.skateflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import itb.grupo5.skateflow.model.entity.Lugar;
import itb.grupo5.skateflow.repository.LugarRepository;

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
			lugarExistente.setFoto(novoLugar.getFoto());
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
}
