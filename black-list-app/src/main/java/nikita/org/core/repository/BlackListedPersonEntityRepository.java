package nikita.org.core.repository;

import nikita.org.core.domain.BlackListedPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlackListedPersonEntityRepository extends JpaRepository<BlackListedPersonEntity, Long> {

    @Query("SELECT pe from BlackListedPersonEntity pe " +
            "where pe.firstName = :firstName " +
            "      and pe.lastName = :lastName")
    Optional<BlackListedPersonEntity> findBy(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
    );

}
