package acme.features.student1.inventor.invention;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.Invention;
import acme.entities.student1.Part;

@Repository
public interface InventorInventionRepository extends AbstractRepository {

	@Query("select i from Invention i where i.ticker = :ticker")
	Invention findInventionByTicker(String ticker);

	@Query("select i from Invention i where i.id = :id")
	Invention findInventionById(int id);

	@Query("select i from Invention i where i.inventor.id = :id")
	Collection<Invention> findInventionsByInventorId(int id);

	@Query("select p from Part p where p.invention.id = :id")
	Collection<Part> findPartsByInventionId(int id);

	@Query("select count(p) from Part p where p.invention.id = :id")
	Integer countPartsFromInventionId(int id);
}
