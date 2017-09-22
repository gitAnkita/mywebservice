package hello;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface GreetingRepository extends JpaRepository<Greeting,Long>{
    @Query("SELECT g from Greeting g where id = ?")
    List<Greeting> findById(long id);

    @Query("select g from Greeting g inner join fetch g.sender p where p.name = ?")
    List<Greeting> findBySenderName(String name);

    @EntityGraph(value = "Greeting.senders", type = EntityGraph.EntityGraphType.FETCH)
    List<Greeting> findBySender_Name(String name);
}
