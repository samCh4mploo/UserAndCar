package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   private final SessionFactory sessionFactory;

   @Autowired
   public UserDaoImp(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   public List<User> listUsers() {
      TypedQuery<User> query = sessionFactory.getCurrentSession()
              .createQuery("select u from User u join fetch u.car", User.class);
      return query.getResultList();
   }

   @Override
   public User getUserByCar(String model, int series) {
      EntityGraph<?> entityGraph = sessionFactory.getCurrentSession()
              .getEntityGraph("user-with-car");

      return sessionFactory.getCurrentSession()
              .createQuery("select u from User u where u.car.model = :model and u.car.series = :series", User.class)
              .setHint("javax.persistence.fetchgraph", entityGraph)
              .setParameter("model", model)
              .setParameter("series", series)
              .uniqueResult();
   }
}
