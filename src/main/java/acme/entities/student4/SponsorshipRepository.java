
package acme.entities.student4;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface SponsorshipRepository extends AbstractRepository {

	// Realiza el sumatorio del atributo 'amount' que está dentro del objeto 'money' 
	// en la entidad 'Donation' (d), filtrando solo aquellas donaciones que 
	// pertenecen a un patrocinio (sponsorship) específico identificado por su ID.
	@Query("select sum(d.money.amount) from Donation d where d.sponsorship.id = :sponsorshipId")
	Double calculateTotalMoney(int sponsorshipId);

	// Cuenta el número total de objetos 'Donation' (d) existentes en la base de datos 
	// cuyo identificador de patrocinio coincida con el parámetro ':id' proporcionado.
	// Retorna un número entero (Integer).
	@Query("select count(d) from Donation d where d.sponsorship.id = :id")
	Integer countDonationsBySponsorshipId(int id);

	// Selecciona el objeto completo 'Sponsorship' (s) donde el atributo 'ticker' 
	// sea exactamente igual al valor pasado como parámetro. 
	// Se utiliza normalmente para recuperar toda la información de un patrocinio 
	// conociendo su código de referencia.
	@Query("select s from Sponsorship s where s.ticker = :ticker")
	Sponsorship findSponsorshipByTicker(String ticker);
}
