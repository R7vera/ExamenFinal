package com.rivera.empresa.eventos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rivera.empresa.eventos.entity.Usuario;
import com.rivera.empresa.eventos.exception.GeneralException;
import com.rivera.empresa.eventos.exception.NoDataFoundException;
import com.rivera.empresa.eventos.exception.ValidateException;
import com.rivera.empresa.eventos.repository.UsuarioRepository;
import com.rivera.empresa.eventos.service.UsuarioService;
import com.rivera.empresa.eventos.util.Encoder;
import com.rivera.empresa.eventos.validator.UsuarioValidator;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private Encoder encoder;

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll(Pageable page) {
		try {
			List<Usuario> registros = repository.findAll(page).toList();
			return registros;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		try {
			List<Usuario> registros = repository.findAll();
			return registros;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByEmail(String email) {
		try {
			Usuario registro = repository.findByEmail(email);
			if (registro == null) {
				throw new NoDataFoundException("Error del servidor");
			}
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findById(int id) {
		try {
			Usuario registro = repository.findById(id)
					.orElseThrow(() -> new NoDataFoundException("No existe un registro con ese ID"));
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional
	public Usuario save(Usuario usuario) {
		try {

			UsuarioValidator.save(usuario);
			// Nuevo registro
			if (usuario.getId() == 0) {
				usuario.setActivo(true);
				usuario.setPassword(encoder.encodePassword(usuario.getPassword()));
				Usuario nuevo = repository.save(usuario);				
				return nuevo;
			}
			// editar registro
			Usuario registro = repository.findById(usuario.getId())
					.orElseThrow(() -> new NoDataFoundException("No existe un registro con ese ID"));
			registro.setEmail(usuario.getEmail());
			String pass=encoder.encodePassword(usuario.getPassword());
			registro.setPassword(pass);
			repository.save(registro);
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional
	public Usuario deactivate(int id) {
		try {
			Usuario registro = repository.findById(id)
					.orElseThrow(() -> new NoDataFoundException("No existe un registro con ese ID"));
			registro.setActivo(false);
			repository.save(registro);
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional
	public Usuario activate(int id) {
		try {
			Usuario registro = repository.findById(id)
					.orElseThrow(() -> new NoDataFoundException("No existe un registro con ese ID"));
			registro.setActivo(true);
			repository.save(registro);
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}
}
