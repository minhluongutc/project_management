package datn.backend.service.jpa;

import datn.backend.repositories.jpa.CategoryRepositoryJPA;
import datn.backend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceJPA {
    final CategoryRepositoryJPA categoryRepositoryJPA;

    public Object getCategoryEntitiesByProjectId(String projectId, String keySearch) {
        return categoryRepositoryJPA.getCategories(projectId, keySearch);
    }
}
