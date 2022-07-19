package crud.api.rest.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import crud.api.rest.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	@Query("select u from User u where u.login = ?1")
	User findUserByLogin(String Login);
	
	@Query("select u from User u where u.email = ?1")
	User findUserByEmail(String Email);
	
	@Transactional
	@Modifying
	@Query("update User set senha = ?1 where id = ?2")
	void updatePassword(String Senha, Long UserId);
}
