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
import com.example.simpsonschallengeapi.model.SimpsonsCharacter;
import com.example.simpsonschallengeapi.service.SimpsonsCharacterService;

@RestController
@RequestMapping("/api/v1/simpsonscharacters")
public class SimpsonsCharacterResource {

	private ArrayList<String> listPictures = new ArrayList<>();
	
	@Autowired
	private SimpsonsCharacterService simpsonsCharacterService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<SimpsonsCharacter> listAll() {
		return simpsonsCharacterService.findAll();
	}

	@PostMapping
	public ResponseEntity<SimpsonsCharacter> create(@Valid @RequestBody SimpsonsCharacter character, HttpServletResponse response) {
		SimpsonsCharacter savedCharacter = simpsonsCharacterService.save(character);
		publisher.publishEvent(new ResourceCreatedEvent(this, response, savedCharacter.get_id()));
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCharacter);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SimpsonsCharacter> findById(@PathVariable String id) {
		Optional<SimpsonsCharacter> character = this.simpsonsCharacterService.findById(id);
		return character.isPresent() ? ResponseEntity.ok(character.get()) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable String id) {
		simpsonsCharacterService.deleteById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SimpsonsCharacter> update(@PathVariable String id, @Valid @RequestBody SimpsonsCharacter character) {
		SimpsonsCharacter savedCharacter = simpsonsCharacterService.update(id, character);
		return ResponseEntity.ok(savedCharacter);
	}
	
	@GetMapping("/listPictures")
	public ArrayList<String> listPictures() {
		List<SimpsonsCharacter> findAll = simpsonsCharacterService.findAll();
		for (SimpsonsCharacter c : findAll) {
			String s = c.getPicture();
			if(s != null && !listPictures.contains(s)) {
				listPictures.add(s);
			}
		}
		return listPictures;
	}
	
}
