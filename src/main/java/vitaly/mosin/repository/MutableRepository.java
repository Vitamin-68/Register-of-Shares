package vitaly.mosin.repository;

import vitaly.mosin.entity.Share;

public interface MutableRepository {
    Share create(Share share);

    Share update(Share share);

    Share delete(int code);

}
