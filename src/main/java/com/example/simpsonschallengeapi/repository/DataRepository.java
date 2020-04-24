package com.example.simpsonschallengeapi.repository;

import java.util.List;
import java.util.Optional;

public interface DataRepository<T, ID> {

	List<T> findAll();
	
	Optional<?> findById(ID id);
	
	<S extends T> S save(S entity);
	
	void deleteById(ID id);
}
