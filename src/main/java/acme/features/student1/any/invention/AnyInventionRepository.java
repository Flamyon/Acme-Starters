package acme.features.student1.any.invention;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;

@Repository
public interface AnyInventionRepository extends AbstractRepository {

	@Query("select i from Invention i where i.draftMode = false")
	List<Invention> findPublishedInventions();

	@Query("select i from Invention i where i.id = :id")
	Invention findInventionById(int id);

	@Query("select i.inventor from Invention i where i.id = :id")
	Inventor findInventorByInventionId(int id);
}
