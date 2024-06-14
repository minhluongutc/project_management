package datn.backend.service.impl;

import datn.backend.dto.PostInsertDTO;
import datn.backend.dto.PostResponseDTO;
import datn.backend.entities.PostEntity;
import datn.backend.repositories.jpa.DocumentRepositoryJPA;
import datn.backend.repositories.jpa.PostRepositoryJPA;
import datn.backend.service.DocumentService;
import datn.backend.service.PostService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostServiceImpl implements PostService {

    final DocumentService documentService;

    final PostRepositoryJPA postRepositoryJPA;
    final DocumentRepositoryJPA documentRepositoryJPA;

    final ModelMapper modelMapper;

    @Override
    @Transactional
    public Object insertPost(Authentication authentication, PostInsertDTO dto, MultipartFile[] files) {
        // save entity
        PostEntity postEntity = new PostEntity();
        modelMapper.map(dto, postEntity);
        postEntity.setId(AuditUtils.generateUUID());
        postEntity.setCreateUserId(AuditUtils.getUserId(authentication));
        postEntity.setCreateTime(AuditUtils.createTime());
        postRepositoryJPA.save(postEntity);

        // save file
        if (files != null) {
            documentService.addAttachments(authentication, postEntity.getId(), files, Constants.DOCUMENT_TYPE.POST.value);
        }
        return "Create post success";
    }

    @Override
    public Object updatePost(Authentication authentication, String id, PostInsertDTO dto) {
        PostEntity postEntity = postRepositoryJPA.findById(id).orElse(null);
        if (postEntity != null) {
            modelMapper.map(dto, postEntity);
            postEntity.setId(id);
            postEntity.setUpdateUserId(AuditUtils.getUserId(authentication));
            postEntity.setUpdateTime(AuditUtils.createTime());
            postRepositoryJPA.save(postEntity);
            return "Update post success";
        }
        return null;
    }

    @Override
    public Object deletePost(Authentication authentication, String id) {
        PostEntity postEntity = postRepositoryJPA.findById(id).orElse(null);
        if (postEntity != null) {
            postEntity.setEnabled(Constants.STATUS.IN_ACTIVE.value);
            return "Delete post success";
        }
        return null;
    }

    @Override
    public Object getPost(Authentication authentication, String id) {
        return postRepositoryJPA.findById(id).orElse(null);
    }

    @Override
    public List<PostResponseDTO> getPosts(Authentication authentication, String projectId) {
        List<PostResponseDTO> postResponseDTOS = postRepositoryJPA.findAllByProjectId(projectId);
        for (PostResponseDTO postResponseDTO : postResponseDTOS) {
            postResponseDTO.setAttachments(documentRepositoryJPA.getAttachmentsByObjectIdAndType(postResponseDTO.getId(), Constants.DOCUMENT_TYPE.POST.value));
        }
        return postResponseDTOS;
    }
}
