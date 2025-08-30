package org.nikita.core.security.user;

import lombok.RequiredArgsConstructor;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.security.exceptions.FeedBackMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UPCUserDetailsService implements UserDetailsService {
    private final PersonRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PersonEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(FeedBackMessage.RESOURCE_NOT_FOUND));
        return UPCUserDetails.buildUserDetails(user);
    }
}


