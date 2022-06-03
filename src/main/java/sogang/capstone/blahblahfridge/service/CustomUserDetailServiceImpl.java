package sogang.capstone.blahblahfridge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sogang.capstone.blahblahfridge.exception.NotFoundException;
import sogang.capstone.blahblahfridge.persistence.UserRepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findById(Long.parseLong(username))
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }
}
