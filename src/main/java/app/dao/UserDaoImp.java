package app.dao;

import app.models.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<User> showAll() {
        return entityManager
                .createQuery("select user from User user", User.class)
                .getResultList();
    }

    @Override
    public User showById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void createUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteById(int id) {
        entityManager.remove(showById(id));
    }
}