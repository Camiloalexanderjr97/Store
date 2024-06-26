package com.example.demo.User.Repository;

import com.example.demo.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	
	 Optional<User> findByUsername(String nombre);
	
	  boolean existsByUsername(String nombre);

	  boolean existsByMail(String mail);
	
	  @Modifying
	  @Query(value = "  UPDATE user SET user.name = :name , user.password = :password, user.username =  :username,  user.mail= :mail,user.modified = :modify WHERE user.id = :id",
//			  update Users u set u.status = ? where u.name = )
	    nativeQuery = true)
	  int updateUserSetStatusForNameNative(String name, String password, String username, int id,  String mail, LocalDateTime modify);

	  
	  @Modifying
	  @Query(value = "  INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (:idUser, '1')",
//			  update Users u set u.status = ? where u.name = )
	    nativeQuery = true)
	  int insert(int idUser);

	  
	  @Modifying
	  @Query(value = "  UPDATE user SET user.token = :token , user.modified = :modify, user.last_Login = :lastLogin WHERE user.id = :id",
//			  update Users u set u.status = ? where u.name = )
	    nativeQuery = true)
	  int updateToken(String token, int id,LocalDateTime modify, LocalDateTime lastLogin);


	  
	  
	  
}
