package hello;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProfileRepository {

  private SessionFactory sessionFactory;


  @Autowired
  public ProfileRepository(SessionFactory sessionFactory){
    this.sessionFactory = sessionFactory;
  }

  private Session getSession() {
    return this.sessionFactory.getCurrentSession();
  }

  public String savePerson(Profile profile) {
    Long isSuccess = (Long)getSession().save(profile);
    if(isSuccess >= 1){
      return "Success";
    }else{
      return "Error while Saving Person";
    }

  }

  public List<Profile> getAllPersons() {
    Criteria criteria = getSession().createCriteria(Profile.class);
    return criteria.list();
  }

  public List<Profile> getPersonByName(String name){
    Criteria criteria = getSession().createCriteria(Profile.class);
    criteria.add(Restrictions.eq("name",name));
    return criteria.list();
  }

  public List<Profile> getPersonByNameAndAge(String name,int minAge){
    Criteria criteria = getSession().createCriteria(Profile.class);
    Criterion nameCr = Restrictions.eq("name",name);
    Criterion ageCr = Restrictions.ge("age",minAge);
    Criterion nameAndAgeCr = Restrictions.and(nameCr,ageCr);
    criteria.add(nameAndAgeCr);
    return criteria.list();
  }
}
