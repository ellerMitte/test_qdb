package qa.qdb.api.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import qa.qdb.api.model.User;
import qa.qdb.api.repository.UserRepository;

@Slf4j
@Service
public class ApiUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public ApiUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        return new ApiUserPrincipal(user);
    }

    public ApiUserPrincipal getApiPrincipal(Authentication authentication) {
        return (ApiUserPrincipal) authentication.getPrincipal();
    }
}
