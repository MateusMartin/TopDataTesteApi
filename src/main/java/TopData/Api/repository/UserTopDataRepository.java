package TopData.Api.repository;

import java.util.List;

import javax.transaction.Transactional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import TopData.Api.document.UserTopData;
import TopData.Api.dto.UserSlcDTO;
import TopData.Api.document.Profile;

@Transactional
public interface UserTopDataRepository extends JpaRepository<UserTopData, Long>{

	
	@Query("select u from UserTopData u where u.email = :email")
	List<UserTopData> findByEmail(@Param("email") String email);
	
	
	@Query("select u from UserTopData u where u.email = :email and u.password = :senha")
	List<UserTopData> Logar(@Param("email") String email,
			@Param("senha") String senha
			);
	
	
	@Query("SELECT u.id, u.Nome AS nome, a.Nome AS profile, u.email FROM  UserTopData u "
			+ "  INNER JOIN Profile a ON a.id = u.profile "
			+ " where 1=1 "
			+ " AND (:nome is null or u.Nome like %:nome%) "
			+ " AND (:email is null or u.email = :email)"
			+ " AND (:profileID is null or a.id = cast(:profileID AS int)) "			
			)
	List<Object[]> usersQuery(@Param("nome")String nome,@Param("email") String email, @Param("profileID")String profileID);
	
	
	@Modifying
	@Query("UPDATE UserTopData u SET u.Nome = :Nome, u.email = :email, u.profile = :profile, "
	        + "u.password = CASE WHEN :password IS NULL THEN u.password ELSE :password END "
	        + "WHERE u.id = :id")
	int updateUser(@Param("Nome") String Nome, @Param("email") String email,@Param("profile") Profile profile,@Param("password") String password,@Param("id") int id);
	
	
}
