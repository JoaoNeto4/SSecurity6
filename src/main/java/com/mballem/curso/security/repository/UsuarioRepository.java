package com.mballem.curso.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mballem.curso.security.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	// We just need to verify the email, as Spring itself will take care of the password.
	@Query("select u from Usuario u where u.email like :email")
	Usuario findByEmail(@Param("email") String email);
}
