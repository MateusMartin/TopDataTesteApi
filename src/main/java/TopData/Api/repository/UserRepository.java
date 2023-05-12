package TopData.Api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import TopData.Api.document.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select case when (count(u.username) > 0)  then true else false end "
			+ "from User u where u.username = :username")
	Boolean userExistByName(@Param("username") String username);
	
	
	@Query("select u from User u where u.username = :username")
	User findByNome(@Param("username") String username);
	
	
	@Query("select u from User u where u.id = :id")
	User findUserById(@Param("id") String id);
}
