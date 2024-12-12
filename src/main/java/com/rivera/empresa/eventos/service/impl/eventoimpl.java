package com.rivera.empresa.eventos.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.rivera.empresa.eventos.entity.evento;
import com.rivera.empresa.eventos.exception.*;
import com.rivera.empresa.eventos.repository.eventoRepository;
import com.rivera.empresa.eventos.service.*;
import com.rivera.empresa.eventos.validator.*;

@Service
public class eventoimpl implements eventoService{
	@Autowired
	private eventoRepository repository;
	
	
	@Override
	@Transactional(readOnly = true)
	public List<evento> findAll(Pageable page) {
		try {
			List<evento> registros=repository.findAll(page).toList();
			return registros;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		}
		catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}		
	}

	@Override
	@Transactional(readOnly = true)
	public List<evento> finByNombre(String nombre, Pageable page) {
		try {
			List<evento> registros=repository.findByNombreContaining(nombre,page);
			return registros;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		}
		catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public evento findById(int id) {
		try {
			evento registro=repository.findById(id).
					orElseThrow(()->new NoDataFoundException("No existe un registro con ese ID"));
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		}
		catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional
	public evento save(evento categoria) {
		try {
			
			eventoValidator.save(categoria);
			//Nuevo registro
			if (categoria.getId()==0) {
				evento nuevo=repository.save(categoria);
				return nuevo;				
			}
			//editar registro
			evento registro=repository.findById(categoria.getId()).orElseThrow(()->new NoDataFoundException("No existe un registro con ese ID"));
			registro.setNombre(categoria.getNombre());
	
			repository.save(registro);
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		}
		catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional
	public void delete(int id) {
		try {
			evento registro=repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe un registro con ese ID"));
			repository.delete(registro);
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		}
		catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<evento> findAll() {
		try {
			List<evento> registros=repository.findAll();
			return registros;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		}
		catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}		
	}
}




