package com.example.simpsonschallengeapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.simpsonschallengeapi.database.DataBase;
import com.example.simpsonschallengeapi.model.SimpsonsCharacter;
import com.example.simpsonschallengeapi.model.SimpsonsPhrase;
import com.example.simpsonschallengeapi.model.generateID.GenerateID;
import com.example.simpsonschallengeapi.repository.SimpsonsPhraseRepository;
import com.example.simpsonschallengeapi.service.execption.CharacterNonExistent;
import com.example.simpsonschallengeapi.service.execption.EmptyResultDataAccessException;


@Service
public class SimpsonsPhraseService implements SimpsonsPhraseRepository {
	
	@Autowired
	private DataBase dataBase;
	
	@Autowired
	private SimpsonsCharacterService simpsonsCharacterService;
		
	@Override
	public List<SimpsonsPhrase> findAll() {
		
		ArrayList<SimpsonsPhrase> list = this.dataBase.getData(SimpsonsPhrase.class);
		
		return list;
	}
	
	public Optional<ArrayList<SimpsonsPhrase>> findAllPhrases(String characterID) {
		
		Optional<SimpsonsCharacter> character = this.simpsonsCharacterService.findById(characterID);
		
		if (!character.isPresent()) {
			throw new CharacterNonExistent();
		} else {
			
			ArrayList<SimpsonsPhrase> list = this.dataBase.getData(SimpsonsPhrase.class);
			
			ArrayList<SimpsonsPhrase> listFiltered = new ArrayList<>();
			
			list.stream()
			.filter(p -> p.getCharacter().equals(characterID))
			.forEach(p -> listFiltered.add(p));			
			
			return Optional.of(listFiltered);
		}
	}
	
	public void deleteAllPhrases(String characterID) {
		
		Optional<SimpsonsCharacter> character = this.simpsonsCharacterService.findById(characterID);
		
		if (!character.isPresent()) {
			throw new CharacterNonExistent();
		} else {
			
			ArrayList<SimpsonsPhrase> list = this.dataBase.getData(SimpsonsPhrase.class);
			
			ArrayList<SimpsonsPhrase> listToRemove = new ArrayList<>();
			
			list.stream()
			.filter(p -> p.getCharacter().equals(characterID))
			.forEach(p -> listToRemove.add(p));
			
			if(listToRemove.isEmpty()) {	
				throw new EmptyResultDataAccessException(
						"No " + SimpsonsPhrase.class + " character entity with id " + characterID + " exists!");
			} else {
				list.removeAll(listToRemove);
				this.dataBase.persistData(list, SimpsonsPhrase.class);	
			}
		}
	}

	@Override
	public Optional<SimpsonsPhrase> findById(String id) {
		
		ArrayList<SimpsonsPhrase> list = this.dataBase.getData(SimpsonsPhrase.class);
		
		SimpsonsPhrase simpsonsPhrase = list.stream()
		.filter(c -> c.get_id().equals(id))
		.findAny()
		.orElse(null);
		
		if(simpsonsPhrase == null)
			return Optional.ofNullable(simpsonsPhrase);
		
		
		return Optional.of(simpsonsPhrase);
	}

	@Override
	public <S extends SimpsonsPhrase> S save(S entity) {
		
		Optional<SimpsonsCharacter> character = this.simpsonsCharacterService.findById(entity.getCharacter());

		if (!character.isPresent()) {
			throw new CharacterNonExistent();
		} else {
			
			entity.set_id(GenerateID.generateID());
			
			ArrayList<SimpsonsPhrase> list = this.dataBase.getData(SimpsonsPhrase.class);
			list.add(entity);
			
			this.dataBase.persistData(list, SimpsonsPhrase.class);
			
			return entity;
		}
	}

	@Override
	public void deleteById(String id) {
		
		SimpsonsPhrase savedPhrase = this.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(
						"No " + SimpsonsPhrase.class + " entity with id " + id + " exists!"));

		ArrayList<SimpsonsPhrase> list = this.dataBase.getData(SimpsonsPhrase.class);
		
		list.remove(savedPhrase);
		
		this.dataBase.persistData(list, SimpsonsPhrase.class);
	}

	public SimpsonsPhrase update(String id, SimpsonsPhrase phrase) {

		SimpsonsPhrase savedPhrase = findPhraseById(id);

		BeanUtils.copyProperties(phrase, savedPhrase, "_id");

		ArrayList<SimpsonsPhrase> list = this.dataBase.getData(SimpsonsPhrase.class);
		
		SimpsonsPhrase s = list.stream()
				.filter(c -> c.get_id().equals(id))
				.findAny()
				.orElse(null);
		
		int index = list.indexOf(s);
		
		list.set(index, savedPhrase);
		
		this.dataBase.persistData(list, SimpsonsPhrase.class);
		
		return savedPhrase;
	}
	
	private SimpsonsPhrase findPhraseById(String id) {
		
		SimpsonsPhrase savedPhrase = this.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(
						"No " + SimpsonsPhrase.class + " entity with id " + id + " exists!"));
		
		return savedPhrase;
	}
	
}
