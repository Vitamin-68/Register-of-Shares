package vitaly.mosin.repository;

import org.slf4j.Logger;
import vitaly.mosin.entity.Share;
import vitaly.mosin.entity.ShareChanges;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrudRepository implements ImmutableRepository, MutableRepository {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CrudRepository.class);
    private final EntityManager shareEntityManager;
    private final EntityManager changeEntityManager;
    private static final String QUERY_SELECT_ALL = "SELECT e FROM %s e";
    private static final String QUERY_SELECT_BY_ID = "SELECT e FROM %s e WHERE e.id = %s";
    private static final String QUERY_SELECT_BY_FIELD = "SELECT e FROM %s e WHERE e.%s = %s";

    public CrudRepository(EntityManagerFactory shareFactory, EntityManagerFactory changeFactory) {
        this.shareEntityManager = shareFactory.createEntityManager();
        this.changeEntityManager = changeFactory.createEntityManager();
    }

    @Override
    public Optional<List<Share>> findAll() {
        EntityTransaction transaction = shareEntityManager.getTransaction();
        transaction.begin();
        String queryString = String.format(QUERY_SELECT_ALL, Share.class);
        final List<Share> result = shareEntityManager.createQuery(queryString).getResultList();
        transaction.commit();
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<List<Share>> findByField(String fieldName, Object value) {
        EntityTransaction transaction = shareEntityManager.getTransaction();
        transaction.begin();
        String queryString = String.format(QUERY_SELECT_BY_FIELD, Share.class, fieldName, value);
        final List<Share> result = shareEntityManager.createQuery(queryString).getResultList();
        transaction.commit();
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Share create(Share entity) {
        if (isEntityExists(entity)) {
            log.error("Entity already exists");
            throw new IllegalStateException("Entity already exist");
        }
        EntityTransaction transaction = shareEntityManager.getTransaction();
        transaction.begin();
        entity.setStatus(true);
        entity.setCost(entity.getQuantity()*entity.getPrice());
        shareEntityManager.merge(entity);
        transaction.commit();
        return entity;
    }

    @Override
    public Share update(Share entity) {
        Optional<List<Share>> result;
        Share oldEntity;
        EntityTransaction transaction = shareEntityManager.getTransaction();
        transaction.begin();
        result = findByField("edrpou", entity.getEdrpou());
        if (result.isPresent()) {
             oldEntity = result.get().get(0);
             entity.setCost(entity.getQuantity()*entity.getPrice());
            List<ShareChanges> changesList =  notifyChanges(oldEntity, entity);
            shareEntityManager.merge(entity);
            mergeChanges(changesList);
        } else log.info("Share not found");
        transaction.commit();
        return entity;
    }

    @Override
    public Share delete(int code) {
        Optional<List<Share>> result;
        Share delEntity;
        EntityTransaction transaction = shareEntityManager.getTransaction();
        transaction.begin();
        result = findByField("edrpou", code);
        if (result.isPresent()) {
            delEntity = result.get().get(0);
            delEntity.setStatus(false);
            shareEntityManager.merge(delEntity);
            List<ShareChanges> changesList = new ArrayList<>();
            ShareChanges changes = statusDeleted(delEntity);
            changesList.add(changes);
            mergeChanges(changesList);
        } else log.info("Share not found");
        transaction.commit();
        return null;
    }

    private ShareChanges statusDeleted(Share entity){
        ShareChanges changes = new ShareChanges();
        changes.setEdrpou(entity.getEdrpou());
        changes.setfName("status");
        changes.setOldValue("Active");
        changes.setNewValue("Deleted");
        return changes;
    }

    private boolean isEntityExists(Share entity) {
        return (shareEntityManager.find(entity.getClass(), entity.getEdrpou()) != null);
    }

    private void mergeChanges(List<ShareChanges> list){
        EntityTransaction transactionChange = changeEntityManager.getTransaction();
        transactionChange.begin();
        for(ShareChanges sChange : list) {
            changeEntityManager.merge(sChange);
        }
        transactionChange.commit();
    }

    private List<ShareChanges> notifyChanges(Share oldEntity, Share newEntity) {
        List<ShareChanges> listChanges = new ArrayList<>();
        ShareChanges shChange = new ShareChanges();
        if (oldEntity.getEdrpou() != newEntity.getEdrpou()) {
            shChange.setEdrpou(oldEntity.getEdrpou());
            shChange.setfName("edrpou");
            shChange.setOldValue(Integer.toString(oldEntity.getEdrpou()));
            shChange.setNewValue(Integer.toString(newEntity.getEdrpou()));
            listChanges.add(shChange);
        }
        if (oldEntity.getQuantity() != newEntity.getQuantity()) {
            shChange.setEdrpou(oldEntity.getEdrpou());
            shChange.setfName("quantity");
            shChange.setOldValue(Integer.toString(oldEntity.getQuantity()));
            shChange.setNewValue(Integer.toString(newEntity.getQuantity()));
            listChanges.add(shChange);
        }
        if (oldEntity.getPrice() != newEntity.getPrice()) {
            shChange.setEdrpou(oldEntity.getEdrpou());
            shChange.setfName("price");
            shChange.setOldValue(Double.toString(oldEntity.getPrice()));
            shChange.setNewValue(Double.toString(newEntity.getPrice()));
            listChanges.add(shChange);
        }
        if (oldEntity.getCost() != newEntity.getQuantity()*newEntity.getPrice()) {
            shChange.setEdrpou(oldEntity.getEdrpou());
            shChange.setfName("cost");
            shChange.setOldValue(Double.toString(oldEntity.getCost()));
            shChange.setNewValue(Double.toString(newEntity.getCost()));
            listChanges.add(shChange);
        }
        if (!oldEntity.getDate().equals(newEntity.getDate())) {
            shChange.setEdrpou(oldEntity.getEdrpou());
            shChange.setfName("date");
            shChange.setOldValue(oldEntity.getDate().toString());
            shChange.setNewValue(newEntity.getDate().toString());
            listChanges.add(shChange);
        }
        if (!oldEntity.getComment().equals(newEntity.getComment())) {
            shChange.setEdrpou(oldEntity.getEdrpou());
            shChange.setfName("comment");
            shChange.setOldValue(oldEntity.getComment());
            shChange.setNewValue(newEntity.getComment());
            listChanges.add(shChange);
        }
        if (oldEntity.isStatus() != newEntity.isStatus()) {
            shChange.setEdrpou(oldEntity.getEdrpou());
            shChange.setfName("status");
            shChange.setOldValue(oldEntity.isStatus() ? "Active" : "Deleted");
            shChange.setNewValue(newEntity.isStatus() ? "Active" : "Deleted");
            listChanges.add(shChange);
        }
        return listChanges;
    }
}
