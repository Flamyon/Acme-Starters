package acme.features.student1.any.part;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.Invention;
import acme.entities.student1.Part;

@Repository
public interface AnyPartRepository extends AbstractRepository {

	@Query("select p from Part p where p.invention.id = :id")
	Collection<Part> findPartsByInventionId(int id);

	@Query("select p from Part p where p.id = :id")
	Part findPartById(int id);

	@Query("select i from Invention i where i.id = :id")
	Invention findInventionById(int id);
}
