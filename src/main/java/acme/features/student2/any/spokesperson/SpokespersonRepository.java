
package acme.features.student2.any.spokesperson;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.Spokesperson;

@Repository
public interface SpokespersonRepository extends AbstractRepository {

	@Query("select s from Spokesperson s where s.id = :id")
	Spokesperson findSpokespersonById(int id);

	@Query("select s from Spokesperson s")
	Collection<Spokesperson> findAllSpokespersons();
}
