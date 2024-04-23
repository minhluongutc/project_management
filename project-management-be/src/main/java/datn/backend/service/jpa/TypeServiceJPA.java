package datn.backend.service.jpa;

import datn.backend.entities.TypeEntity;
import datn.backend.repositories.jpa.TypeRepositoryJPA;
import datn.backend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TypeServiceJPA {
    final TypeRepositoryJPA typeRepositoryJPA;

    public List<TypeEntity> getTypes(String projectId, String keySearch) {
        return typeRepositoryJPA.getTypes(projectId, keySearch);
    }
}
