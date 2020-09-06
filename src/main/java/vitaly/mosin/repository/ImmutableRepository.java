package vitaly.mosin.repository;

import vitaly.mosin.entity.Share;

import java.util.List;
import java.util.Optional;

public interface ImmutableRepository {
    Optional<List<Share>> findAll(int pages);

    Optional<List<Share>> findByField(String fieldName, int pages);


}
