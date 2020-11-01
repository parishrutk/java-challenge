package jp.co.axa.apidemo.security;

import jp.co.axa.apidemo.entities.UserInfo;
import jp.co.axa.apidemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsProviderService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserInfo> myUser = userRepository.findByUserName(userName);
        //TODO: add a logger here for error case.
        myUser.orElseThrow(() -> new UsernameNotFoundException("Given user name [" + userName + "] is invalid or does not exist."));
        //TODO: Need to handle UsernameNotFoundException if the findByUserName does not return any resultSet.
        return myUser.map(CustomUserDetails::new).get();
    }

    /*private Collection<GrantedAuthority> getGrantedAuthority(UserInfo user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getName().equalsIgnoreCase("admin")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }*/

}
