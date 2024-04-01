package datn.backend.service.impl;

import datn.backend.dto.UserDTO;
import datn.backend.entities.ProjectUserEntity;
import datn.backend.entities.UserEntity;
import datn.backend.repositories.jpa.ProjectUserRepositoryJPA;
import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.service.ProjectUserService;
import datn.backend.utils.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectUserServiceImpl implements ProjectUserService {
    final ProjectUserRepositoryJPA projectUserRepositoryJPA;
    final UserRepositoryJPA userRepositoryJPA;
    final ModelMapper modelMapper;

    @Override
    public List<UserDTO.UserResponseDTO> getUsers(Integer projectId) {
        List<UserDTO.UserResponseDTO> dtos = new ArrayList<>();
        List<ProjectUserEntity> projectUserEntities = projectUserRepositoryJPA.getProjectUserEntitiesByProjectId(projectId);
        for (ProjectUserEntity projectUserEntity : projectUserEntities) {
            UserEntity userEntity = userRepositoryJPA.findByIdAndEnabled(projectUserEntity.getUserId(), Constants.STATUS.ACTIVE.value);
            UserDTO.UserResponseDTO userResponseDTO = new UserDTO.UserResponseDTO();
            modelMapper.map(userEntity, userResponseDTO);
            dtos.add(userResponseDTO);
        }
        return dtos;
    }
}
