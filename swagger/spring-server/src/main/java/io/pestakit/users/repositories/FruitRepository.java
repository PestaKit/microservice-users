package io.pestakit.users.repositories;

import io.pestakit.users.entities.FruitEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Olivier Liechti on 26/07/17.
 */
public interface FruitRepository extends CrudRepository<FruitEntity, Long>{

}