package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.service.UserService;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user with this id"));
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void addUserPhoto(Long id, MultipartFile image) {

    }

}
