package projetosgdm.sgdm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetosgdm.sgdm.model.Entidade;

import java.util.List;

public interface EntidadeRepository extends JpaRepository<Entidade, Integer> {
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Entidade e " +
            "WHERE e.idDonoEntidade.id = :usuarioId")
    boolean usuarioTemEntidade(@Param("usuarioId") Long usuarioId);

    // Verifica se usuário é Responsável Técnico de alguma entidade
    @Query(value = "SELECT EXISTS(" +
            "SELECT 1 FROM entidades e " +
            "INNER JOIN farmaceuticos f ON e.farmaceutico_rt = f.id " +
            "WHERE f.id_usuario_pf = :usuarioId)",
            nativeQuery = true)
    boolean usuarioEhRT(@Param("usuarioId") Long usuarioId);

    // Busca todas as entidades onde o usuário é RT
    @Query("SELECT e FROM Entidade e " +
            "INNER JOIN e.farmaceuticoRt f " +
            "WHERE f.idUsuarioPf.id = :usuarioId")
    List<Entidade> findEntidadesByRT(@Param("usuarioId") Long usuarioId);



}
