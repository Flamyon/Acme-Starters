package acme.features.authenticated.student2.spokesperson;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.student2.Spokesperson;

@Repository
public interface AuthenticatedSpokespersonRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select s from Spokesperson s where s.userAccount.id = :id")
	Spokesperson findSpokespersonByUserAccountId(int id);
}
