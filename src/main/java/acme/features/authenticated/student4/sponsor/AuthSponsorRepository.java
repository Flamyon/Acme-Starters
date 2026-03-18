
package acme.features.authenticated.student4.sponsor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Sponsor;

@Repository
public interface AuthSponsorRepository extends AbstractRepository {

	@Query("select s from Sponsor s where s.id = :id")
	Sponsor findSponsorById(int id);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select s from Sponsor s where s.userAccount.id = :id")
	Sponsor findSponsorByUserAccountId(int id);
}
