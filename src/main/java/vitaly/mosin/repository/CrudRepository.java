package vitaly.mosin.repository;

import org.slf4j.Logger;
import vitaly.mosin.entity.Share;
import vitaly.mosin.entity.ShareChanges;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrudRepository implements ImmutableRepository, MutableRepository {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CrudRepository.class);
    private final EntityManager entityManager;
    private static final String QUERY_SELECT_ALL = "SELECT e FROM %s e";
    private static final String QUERY_SELECT_BY_ID = "SELECT e FROM %s e WHERE e.id = %s";
    private static final String QUERY_SELECT_BY_FIELD = "SELECT e FROM %s e WHERE e.%s = %s";

    public CrudRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<List<Share>> findAll() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String queryString = String.format(QUERY_SELECT_ALL, Share.class);
        final List<Share> result = entityManager.createQuery(queryString).getResultList();
        transaction.commit();
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<List<Share>> findByField(String fieldName, Object value) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String queryString = String.format(QUERY_SELECT_BY_FIELD,Share.class, fieldName, value);
        final List<Share> result = entityManager.createQuery(queryString).getResultList();
        transaction.commit();
        return result.isEmpty() ? Optional.empty() : Optional.of(result);    }

    @Override
    public Share create(Share entity) {
        if (isEntityExists(entity)) {
            log.error("Entity already exists");
            throw new IllegalStateException( "Entity already exist");
        }
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entity.setStatus(true);
        entityManager.merge(entity);
        transaction.commit();
        return entity;
    }

    private boolean isEntityExists(Share entity) {
        return (entityManager.find(entity.getClass(), entity.getEdrpou()) != null);
    }

    @Override
    public Share update(Share entity) {
        Optional<List<Share>> result;
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        result = findByField("edrpou",entity.getEdrpou());

        entityManager.merge(entity);
        transaction.commit();
        return entity;
    }

    private List<ShareChanges> notifyChanges(Share oldEntity, Share newEntity) {
        List<ShareChanges> listChanges = new ArrayList<>();
        return listChanges;
    }

    @Override
    public Share delete(int code) {
        return null;
    }
}
