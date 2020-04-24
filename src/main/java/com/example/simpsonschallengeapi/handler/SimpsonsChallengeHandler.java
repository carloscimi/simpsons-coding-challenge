package com.example.simpsonschallengeapi.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.simpsonschallengeapi.service.execption.CharacterNonExistent;
import com.example.simpsonschallengeapi.service.execption.EmptyResultDataAccessException;

@ControllerAdvice
public class SimpsonsChallengeHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
	    HttpHeaders headers, HttpStatus status, WebRequest request) {

	  String userMsg = this.messageSource.getMessage("invalid.message", null, LocaleContextHolder.getLocale());
	  String devMsg = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

	  List<Error> errors = Arrays.asList(new Error(userMsg, devMsg));

	  return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Error> errors = createErrorList(ex.getBindingResult());
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {
		
		String userMsg = messageSource.getMessage("resource.not-found", null,
				LocaleContextHolder.getLocale());
		
		String devMsg = ex.toString();
		
		List<Error> erros = Arrays.asList(new Error(userMsg, devMsg));
		
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ CharacterNonExistent.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativoException(CharacterNonExistent ex) {

		String userMsg = this.messageSource.getMessage("character.non-existent", null,
				LocaleContextHolder.getLocale());
		String devMsg = ex.toString();

		List<Error> errors = Arrays.asList(new Error(userMsg, devMsg));

		return ResponseEntity.badRequest().body(errors);
	}
	
	private List<Error> createErrorList(BindingResult bindingResult) {
		List<Error> errors = new ArrayList<>();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = fieldError.toString();
			errors.add(new Error(mensagemUsuario, mensagemDesenvolvedor));
		}

		return errors;
	}
	
	public static class Error {

		private String userMsg;
		private String devMsg;

		public Error(String userMsg, String devMsg) {
			this.userMsg = userMsg;
			this.devMsg = devMsg;
		}

		public String getUserMsg() {
			return userMsg;
		}
		public void setUserMsg(String userMsg) {
			this.userMsg = userMsg;
		}

		public String getDevMsg() {
			return devMsg;
		}
		public void setDevMsg(String devMsg) {
			this.devMsg = devMsg;
		}
	}
}
