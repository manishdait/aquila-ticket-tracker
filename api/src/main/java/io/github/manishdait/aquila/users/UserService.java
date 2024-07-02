package io.github.manishdait.aquila.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("Invalid User"));
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(u -> maprToUserResponse(u)).toList();
    }

    public UserResponse updateUser(UserRequest request) {
        User user = userRepository.findByUsername(request.name()).orElseThrow();
        user.setEmail(request.email());
        if (request.role().equals(Role.USER.name())) {
            user.setRole(Role.USER);
        } else if (request.role().equals(Role.ADMIN.name())) {
            user.setRole(Role.ADMIN);
        }

        if (request.password() != null) {
            user.setPassword(request.password());
        }
        
        userRepository.save(user);
        return maprToUserResponse(user);
    }

    private UserResponse maprToUserResponse(User user) {
        return new UserResponse(
            user.getUsername(), 
            user.getEmail(), 
            user.getRole().name(), 
            user.isEnabled()
        );
    }
}
