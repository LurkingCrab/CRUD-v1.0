package app.dao;

import app.models.Role;
import app.models.User;
import app.security.SecurityConfig;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> showAll() {
        return entityManager
                .createQuery("select distinct user from User user join fetch user.roles roles", User.class)
                .getResultList();
    }

    @Override
    public User showById(int id) {
        User user;

        try {
            user = entityManager
                    .createQuery("select distinct user from User user join fetch user.roles roles where user.id=:id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ignore) {
            return null;
        }

        return user;
    }

    @Override
    public User getUserByName(String login) {
        User user;

        try {
            user = entityManager
                    .createQuery("select distinct user from User user join fetch user.roles roles where user.login=:login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException ignore) {
            return null;
        }

        return user;
    }

    @Override
    public List<Role> showRoles() {
        return entityManager
                        .createQuery("select role from Role role", Role.class)
                        .getResultList();
    }

    @Override
    public void createUser(User user) {
        user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        if (!user.getPassword().equals(showById(user.getId()).getPassword())) {
            user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        }
        entityManager.merge(user);
    }

    @Override
    public void deleteById(int id) {
        entityManager.remove(showById(id));
    }
}