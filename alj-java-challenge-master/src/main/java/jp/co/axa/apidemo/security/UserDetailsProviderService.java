package jp.co.axa.apidemo.security;

import jp.co.axa.apidemo.entities.UserInfo;
import jp.co.axa.apidemo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(UserDetailsProviderService.class);

    private String userName;

    public UserDetailsProviderService(String userName) {
        this.userName = userName;
    }

    public UserDetailsProviderService() {
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserInfo> myUser = userRepository.findByUserName(userName);
        if (!myUser.isPresent()) {
            logger.error("Unable to find [" + userName + "]. Please check if user login details are valid or not.");
        }
        myUser.orElseThrow(() -> new UsernameNotFoundException("Given user name [" + userName + "] is invalid or does not exist."));
        return myUser.map(CustomUserDetails::new).get();
    }

}
