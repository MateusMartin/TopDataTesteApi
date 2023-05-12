package TopData.Api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import TopData.Api.document.Profile;
import TopData.Api.document.UserTopData;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>{
	
	
	@Query("select p from Profile p where p.id = :id")
	Profile findByidInt(@Param("id") int id);
	
	
	
	
	
	

}
