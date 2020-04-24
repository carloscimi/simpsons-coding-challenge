package com.example.simpsonschallengeapi.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpsonschallengeapi.event.ResourceCreatedEvent;
import com.example.simpsonschallengeapi.model.SimpsonsPhrase;
import com.example.simpsonschallengeapi.service.SimpsonsPhraseService;

@RestController
@RequestMapping(value = "/api/v1/simpsonsphrases", produces={"application/json; charset=UTF-8"})
public class SimpsonsPhraseResource {

	@Autowired
	private SimpsonsPhraseService simpsonsPhraseService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<SimpsonsPhrase> listAll() {
		return simpsonsPhraseService.findAll();
	}
	
	@GetMapping("/character/{id}")
	public ResponseEntity<ArrayList<SimpsonsPhrase>> listAll(@PathVariable String id) {
		Optional<ArrayList<SimpsonsPhrase>> listPhrases = simpsonsPhraseService.findAllPhrases(id);
		return listPhrases.get().isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(listPhrases.get());
	}
	
	@DeleteMapping("/character/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAll(@PathVariable String id) {
		simpsonsPhraseService.deleteAllPhrases(id);
	}

	@PostMapping
	public ResponseEntity<SimpsonsPhrase> create(@Valid @RequestBody SimpsonsPhrase phrase, HttpServletResponse response) {
		SimpsonsPhrase savedPhrase = simpsonsPhraseService.save(phrase);
		publisher.publishEvent(new ResourceCreatedEvent(this, response, savedPhrase.get_id()));
		return ResponseEntity.status(HttpStatus.CREATED).body(savedPhrase);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SimpsonsPhrase> findById(@PathVariable String id) {
		Optional<SimpsonsPhrase> phrase = this.simpsonsPhraseService.findById(id);
		return phrase.isPresent() ? ResponseEntity.ok(phrase.get()) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable String id) {
		simpsonsPhraseService.deleteById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SimpsonsPhrase> update(@PathVariable String id, @Valid @RequestBody SimpsonsPhrase phrase) {
		SimpsonsPhrase savedPhrase = simpsonsPhraseService.update(id, phrase);
		return ResponseEntity.ok(savedPhrase);
	}
	
}
