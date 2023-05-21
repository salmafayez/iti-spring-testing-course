package gov.iti.jets.testing.service;

import gov.iti.jets.testing.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(Long id);

    User addUser(User user);

    void addUserPhoto(Long id, MultipartFile image);

    void deleteUser(Long id);
}
