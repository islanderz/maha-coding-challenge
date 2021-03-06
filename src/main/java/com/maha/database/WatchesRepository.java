package com.maha.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchesRepository extends MongoRepository<WatchesModel, String> {

}
