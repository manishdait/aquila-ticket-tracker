package io.github.manishdait.aquila.users;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.manishdait.aquila.error.AquilaApiException;
import io.github.manishdait.aquila.error.Error;
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
        return userRepository.findByUsername(username).orElseThrow(
            () -> new AquilaApiException(
                HttpStatus.NOT_FOUND, 
                Error.USERNMAE_NOT_FOUND.error(), 
                String.format("The username '%s' was not found.Please check if username is valid.", username), 
                Instant.now()
            )
        );
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(u -> maprToUserResponse(u)).toList();
    }

    public UserResponse updateUser(UserRequest request) {
        User user = userRepository.findByUsername(request.name()).orElseThrow(
            () -> new AquilaApiException(
                HttpStatus.NOT_FOUND, 
                Error.USERNMAE_NOT_FOUND.error(), 
                String.format("The username '%s' was not found.Please check if username is valid.", request.name()), 
                Instant.now()
            )
        );

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
