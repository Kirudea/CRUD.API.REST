package crud.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import crud.api.rest.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	@Query("select u from User u where u.login = ?1")
	User findUserByLogin(String Login);
	
	@Query("select u from User u where u.email = ?1")
	User findUserByEmail(String Email);
	
}
