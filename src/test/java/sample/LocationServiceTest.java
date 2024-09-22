package sample;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LocationServiceTest {

    private LocationService locationService = new LocationService(1);
    @org.junit.jupiter.api.Test
    void findAll() {
        List<Location> all = locationService.findAll();

        assertNotNull(all);
    }

    @org.junit.jupiter.api.Test
    void getById() {
        Optional<Location> byId = locationService.getById(1L);

        assertNotNull(byId.get());
    }

    @org.junit.jupiter.api.Test
    void getByLocationIp() {
        Optional<Location> byLocationIp = locationService.getByLocationIp("161.185.160.93");

        assertNotNull(byLocationIp.get());
    }

    @org.junit.jupiter.api.Test
    void save() {
    }

    @org.junit.jupiter.api.Test
    void delete() {
    }

    @org.junit.jupiter.api.Test
    void deleteById() {
        locationService.deleteById("1");
    }
}