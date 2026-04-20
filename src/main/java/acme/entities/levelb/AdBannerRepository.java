package acme.entities.levelb;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.helpers.RandomHelper;
import acme.client.repositories.AbstractRepository;

@Repository
public interface AdBannerRepository extends AbstractRepository {

	@Query("select a from AdBanner a where a.id = :id")
	AdBanner findAdBannerById(@Param("id") int id);

	@Query("select a from AdBanner a order by a.id asc")
	Collection<AdBanner> findAllAdBanners();

	@Query("select count(a) from AdBanner a")
	int countAdBanners();

	@Query("select a from AdBanner a")
	List<AdBanner> findAllAdBanners(PageRequest pageRequest);

	default AdBanner findRandomAdBanner() {
		AdBanner result;
		int count;
		int index;
		PageRequest page;
		List<AdBanner> list;

		count = this.countAdBanners();
		if (count == 0)
			result = null;
		else {
			index = RandomHelper.nextInt(0, count);
			page = PageRequest.of(index, 1, Sort.by(Direction.ASC, "id"));
			list = this.findAllAdBanners(page);
			result = list.isEmpty() ? null : list.get(0);
		}

		return result;
	}

}
