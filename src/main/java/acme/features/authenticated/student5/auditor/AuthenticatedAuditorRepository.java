
package acme.features.authenticated.student5.auditor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Auditor;

@Repository
public interface AuthenticatedAuditorRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select au from Auditor au where au.userAccount.id = :id")
	Auditor findAuditorByUserAccountId(int id);
}
