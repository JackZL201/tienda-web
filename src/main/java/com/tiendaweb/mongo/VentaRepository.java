package com.tiendaweb.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VentaRepository extends MongoRepository<VentaDoc, String> { }
