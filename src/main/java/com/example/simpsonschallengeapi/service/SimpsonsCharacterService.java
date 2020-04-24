package com.example.simpsonschallengeapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.simpsonschallengeapi.database.DataBase;
import com.example.simpsonschallengeapi.model.SimpsonsCharacter;
import com.example.simpsonschallengeapi.model.generateID.GenerateID;
import com.example.simpsonschallengeapi.repository.SimpsonsCharacterRepository;
import com.example.simpsonschallengeapi.service.execption.EmptyResultDataAccessException;


@Service
public class SimpsonsCharacterService implements SimpsonsCharacterRepository {
	
	@Autowired
	private DataBase dataBase;
		
	@Override
	public List<SimpsonsCharacter> findAll() {
		
		ArrayList<SimpsonsCharacter> list = this.dataBase.getData(SimpsonsCharacter.class);
		
		return list;
	}

	@Override
	public Optional<SimpsonsCharacter> findById(String id) {
		
		ArrayList<SimpsonsCharacter> list = this.dataBase.getData(SimpsonsCharacter.class);
		
		SimpsonsCharacter simpsonsCharacter = list.stream()
		.filter(c -> c.get_id().equals(id))
		.findAny()
		.orElse(null);
		
		if(simpsonsCharacter == null)
			return Optional.ofNullable(simpsonsCharacter);
		
		
		return Optional.of(simpsonsCharacter);
	}

	@Override
	public <S extends SimpsonsCharacter> S save(S entity) {
		
		entity.set_id(GenerateID.generateID());
		
		ArrayList<SimpsonsCharacter> list = this.dataBase.getData(SimpsonsCharacter.class);
		list.add(entity);
		
		this.dataBase.persistData(list, SimpsonsCharacter.class);
		
		return entity;
	}

	@Override
	public void deleteById(String id) {
		
		SimpsonsCharacter savedCharacter = this.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(
						"No " + SimpsonsCharacter.class + " entity with id " + id + " exists!"));

		ArrayList<SimpsonsCharacter> list = this.dataBase.getData(SimpsonsCharacter.class);
		
		list.remove(savedCharacter);
		
		this.dataBase.persistData(list, SimpsonsCharacter.class);
	}

	public SimpsonsCharacter update(String id, SimpsonsCharacter character) {

		SimpsonsCharacter savedCharacter = findCharacterById(id);

		BeanUtils.copyProperties(character, savedCharacter, "_id");
		
		ArrayList<SimpsonsCharacter> list = this.dataBase.getData(SimpsonsCharacter.class);
		
		SimpsonsCharacter s = list.stream()
				.filter(c -> c.get_id().equals(id))
				.findAny()
				.orElse(null);
		
		int index = list.indexOf(s);
		
		list.set(index, savedCharacter);
		
		this.dataBase.persistData(list, SimpsonsCharacter.class);
		
		return savedCharacter;
	}
	
	private SimpsonsCharacter findCharacterById(String id) {
		
		SimpsonsCharacter savedCharacter = this.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(
						"No " + SimpsonsCharacter.class + " entity with id " + id + " exists!"));
		
		return savedCharacter;
	}
	
}
