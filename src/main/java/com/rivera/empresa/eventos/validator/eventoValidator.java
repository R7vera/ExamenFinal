package com.rivera.empresa.eventos.validator;
import com.rivera.empresa.eventos.entity.*;
import com.rivera.empresa.eventos.exception.ValidateException;
public class eventoValidator {
	public static void save(evento registro) {
        // Validar que el nombre no sea nulo o vacío
        if (registro.getNombre() == null || registro.getNombre().trim().isEmpty()) {
            throw new ValidateException("El nombre es requerido");
        }

        // Validar que el nombre no exceda los 255 caracteres
        if (registro.getNombre().length() > 255) {
            throw new ValidateException("El nombre no debe exceder los 255 caracteres");
        }

        // Validar que la descripción no sea nula o vacía
        if (registro.getDescripcion() == null || registro.getDescripcion().trim().isEmpty()) {
            throw new ValidateException("La descripción es requerida");
        }

        // Validar que la fecha de inicio no sea nula
        if (registro.getFechaInicio() == null) {
            throw new ValidateException("La fecha de inicio es requerida");
        }

        // Validar que la fecha de fin no sea nula
        if (registro.getFechaFin() == null) {
            throw new ValidateException("La fecha de fin es requerida");
        }

        // Validar que la fecha de fin sea posterior a la fecha de inicio
        if (registro.getFechaInicio() != null && registro.getFechaFin() != null &&
            registro.getFechaFin().before(registro.getFechaInicio())) {
            throw new ValidateException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

  
    }
}
