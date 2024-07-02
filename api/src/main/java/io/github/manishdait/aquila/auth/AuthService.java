package io.github.manishdait.aquila.auth;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.manishdait.aquila.auth.jwt.JwtProvider;
import io.github.manishdait.aquila.mail.MailService;
import io.github.manishdait.aquila.mail.NotificationEmail;
import io.github.manishdait.aquila.token.referesh.RefereshTokenRequest;
import io.github.manishdait.aquila.token.referesh.RefereshTokenService;
import io.github.manishdait.aquila.token.verification.VerificationToken;
import io.github.manishdait.aquila.token.verification.VerificationTokenRepository;
import io.github.manishdait.aquila.users.User;
import io.github.manishdait.aquila.users.UserRepository;
import io.github.manishdait.aquila.users.Role;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;
    private final RefereshTokenService refereshTokenService;
    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void signUp(SignupRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setEnabled(false);
        user.setRole(Role.USER);

        User response = userRepository.save(user);

        String token = generateToken(response);

        NotificationEmail notificationEmail = new NotificationEmail();
        notificationEmail.setRecipient(request.email());
        notificationEmail.setSubject("Activate your Account");
        notificationEmail.setBody("Hi," + request.username() + 
                        "\nWelcome to Aquila, Click the below link for activating your account." +
                        "\nhttp://localhost:8080/aquila-api/activate/account/" + token +
                        "\n\n Without activation you will not able to login to your account.");
                        
        mailService.sendMail(notificationEmail);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        if (!authentication.isAuthenticated()) {
           throw new UsernameNotFoundException("Invalid Credential");
        }
        String token = jwtProvider.generateToken(request.username());

        return new AuthResponse(
            request.username(), 
            authentication.getAuthorities().stream().map(g -> g.getAuthority()).toList().get(0), 
            token, 
            refereshTokenService.generateToken(), 
            Instant.now(), 
            Instant.now().plusSeconds(900)
        );
    } 

    public void activateAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow();
        User user = userRepository.findByUsername(verificationToken.getUser().getUsername()).orElseThrow();

        user.setEnabled(true);
        verificationTokenRepository.delete(verificationToken);
        userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof String) {
            return null;
        }

        User user = (User) authentication.getPrincipal();
        return userRepository.findByUsername(user.getUsername()).orElseThrow();
    }

    public AuthResponse refreshToken(RefereshTokenRequest request) {
        if (!refereshTokenService.isValidToken(request.refreshToken())) {
            throw new IllegalStateException("Invalid Referesh Token");
        }

        return new AuthResponse(
            request.username(), 
            null, 
            jwtProvider.generateToken(request.username()), 
            request.refreshToken(), 
            Instant.now(), 
            Instant.now().plusSeconds(900)
        );
    }

    public void logout(String token) {
        refereshTokenService.deleteToken(token);
    }

    private String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder().token(token)
            .createdAt(Instant.now())
            .user(user)
            .build();
        
        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
