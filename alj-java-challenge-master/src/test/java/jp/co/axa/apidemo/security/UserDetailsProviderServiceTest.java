package jp.co.axa.apidemo.security;

import jp.co.axa.apidemo.entities.UserInfo;
import jp.co.axa.apidemo.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(UserDetailsProviderService.class)
public class UserDetailsProviderServiceTest {

    Map<String, UserInfo> userInfoList = new HashMap<>(10);
    private final static String ADMIN_NAME = "admin";
    private final static String EDITOR_NAME = "editor";
    private final static String VIEWER_NAME = "viewer";
    private final static String ADMIN_ROLES = "ROLE_ADMIN, ROLE_EDITOR, ROLE_VIEWER";
    private final static String EDITOR_ROLES = "ROLE_EDITOR, ROLE_VIEWER";
    private final static String VIEWER_ROLES = "ROLE_VIEWER";

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDetailsProviderService userDetailsProviderService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        UserInfo adminUserInfo = new UserInfo("1", ADMIN_NAME, "admin", true, ADMIN_ROLES);
        UserInfo editorUserInfo = new UserInfo("2", EDITOR_NAME, "editor", true, EDITOR_ROLES);
        UserInfo viewerUserInfo = new UserInfo("3", VIEWER_NAME, "viewer", true, VIEWER_ROLES);
        userInfoList.put("admin", adminUserInfo);
        userInfoList.put("editor", editorUserInfo);
        userInfoList.put("viewer", viewerUserInfo);
    }

    @Test
    public void testLoadUserByUserName_loadAdminUserDetails() {

        when(userRepository.findByUserName(ADMIN_NAME)).thenReturn(java.util.Optional.ofNullable(userInfoList.get(ADMIN_NAME)));
        UserDetails userDetails = userDetailsProviderService.loadUserByUsername(ADMIN_NAME);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        assertThat(customUserDetails.getUsername()).isEqualTo(ADMIN_NAME);
        assertThat(customUserDetails.getUserRoles()).isNotNull();
        assertThat(customUserDetails.getUserRoles()).isNotEmpty();
        assertThat(customUserDetails.getUserRoles().size()).isEqualTo(3);
    }

    @Test
    public void testLoadUserByUserName_loadEditorUserDetails() {

        when(userRepository.findByUserName(EDITOR_NAME)).thenReturn(java.util.Optional.ofNullable(userInfoList.get(EDITOR_NAME)));
        UserDetails userDetails = userDetailsProviderService.loadUserByUsername(EDITOR_NAME);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        assertThat(customUserDetails.getUsername()).isEqualTo(EDITOR_NAME);
        assertThat(customUserDetails.getUserRoles()).isNotNull();
        assertThat(customUserDetails.getUserRoles()).isNotEmpty();
        assertThat(customUserDetails.getUserRoles().size()).isEqualTo(2);

    }

    @Test
    public void testLoadUserByUserName_loadViewerUserDetails() {

        when(userRepository.findByUserName(VIEWER_NAME)).thenReturn(java.util.Optional.ofNullable(userInfoList.get(VIEWER_NAME)));
        UserDetails userDetails = userDetailsProviderService.loadUserByUsername(VIEWER_NAME);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        assertThat(customUserDetails.getUsername()).isEqualTo(VIEWER_NAME);
        assertThat(customUserDetails.getUserRoles()).isNotNull();
        assertThat(customUserDetails.getUserRoles()).isNotEmpty();
        assertThat(customUserDetails.getUserRoles().size()).isEqualTo(1);

    }

    @Test
    public void testLoadUserByUserName_invalidUserName_throwUserNameNotFoundException() {

        String invalidUserName = "dummy";
        when(userRepository.findByUserName(invalidUserName)).thenReturn(java.util.Optional.empty());
        try {
            UserDetails userDetails = userDetailsProviderService.loadUserByUsername(invalidUserName);
        } catch (UsernameNotFoundException ex) {
            assertThat(ex).isNotNull();
            assertThat(ex).isInstanceOf(UsernameNotFoundException.class);
            assertThat(ex.getLocalizedMessage()).isEqualTo("Given user name [" + invalidUserName + "] is invalid or does not exist.");
        }
    }

}