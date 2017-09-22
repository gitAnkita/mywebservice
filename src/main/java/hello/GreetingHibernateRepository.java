package hello;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class GreetingHibernateRepository {

  private SessionFactory sessionFactory;


  @Autowired
  public GreetingHibernateRepository(SessionFactory sessionFactory){
    this.sessionFactory = sessionFactory;
  }

  private Session getSession() {
    return this.sessionFactory.getCurrentSession();
  }

  public List<Greeting> getGreetingBySenderName(String name){
    //inner join
    Criteria criteria = getSession().createCriteria(Greeting.class,"greetingAlias");
    criteria.createCriteria("greetingAlias.sender","profileAlias");
    criteria.add(Restrictions.eq("profileAlias.name",name));
    return criteria.list();
  }

}
