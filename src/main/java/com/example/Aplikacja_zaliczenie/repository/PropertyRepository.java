    package com.example.Aplikacja_zaliczenie.repository;

    import com.example.Aplikacja_zaliczenie.model.Property;
    import org.springframework.data.repository.CrudRepository;
    import java.util.List;

    public interface PropertyRepository extends CrudRepository<Property, Long> {
        List<Property> findAll();

        // Jeśli chcesz potem filtrować po właścicielu:
        List<Property> findByOwnerId(Long ownerId);
    }
