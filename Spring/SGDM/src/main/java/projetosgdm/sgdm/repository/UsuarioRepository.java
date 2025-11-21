package projetosgdm.sgdm.repository;

import projetosgdm.sgdm.model.Usuario;



import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}