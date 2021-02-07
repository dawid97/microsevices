package com.javasolution.app.resourceservice.repositories;

import com.javasolution.app.resourceservice.entities.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {
    Optional<Resource> findByUserIdAndId(final long userId, final long id);
    List<Resource> findAllByUserId(final long userId);
    Resource findByNameAndType(final String name, final String type);
}
