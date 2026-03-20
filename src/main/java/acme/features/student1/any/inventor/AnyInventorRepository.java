package acme.features.student1.any.inventor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;

@Repository
public interface AnyInventorRepository extends AbstractRepository {

	@Query("select i from Invention i where i.id = :id")
	Invention findInventionById(int id);

	@Query("select i from Inventor i where i.id = :id")
	Inventor findInventorById(int id);
}
