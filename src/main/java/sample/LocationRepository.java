package sample;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {

    List<Location> findAll();
    Optional<Location> getById(Long id);
    Optional<Location> getByLocationIp(String ip);
    void save(Location o);
    void delete(Location o);
    void deleteById(String o);
}
