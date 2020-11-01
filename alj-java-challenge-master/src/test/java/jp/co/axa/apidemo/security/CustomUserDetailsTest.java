package jp.co.axa.apidemo.security;

import jp.co.axa.apidemo.entities.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(CustomUserDetails.class)
public class CustomUserDetailsTest {

    Map<String, UserInfo> userInfoList = new HashMap<>(10);
    private final static String ADMIN_NAME = "admin";
    private final static String EDITOR_NAME = "editor";
    private final static String VIEWER_NAME = "viewer";
    private final static String ADMIN_ROLES = "ROLE_ADMIN, ROLE_EDITOR, ROLE_VIEWER";
    private final static String EDITOR_ROLES = "ROLE_EDITOR, ROLE_VIEWER";
    private final static String VIEWER_ROLES = "ROLE_VIEWER";

    @Before
    public void init() {

        UserInfo adminUserInfo = new UserInfo("1", ADMIN_NAME, "admin", true, ADMIN_ROLES);
        UserInfo editorUserInfo = new UserInfo("2", EDITOR_NAME, "editor", true, EDITOR_ROLES);
        UserInfo viewerUserInfo = new UserInfo("3", VIEWER_NAME, "viewer", true, VIEWER_ROLES);

        userInfoList.put(ADMIN_NAME, adminUserInfo);
        userInfoList.put(EDITOR_NAME, editorUserInfo);
        userInfoList.put(VIEWER_NAME, viewerUserInfo);
    }

    @Test
    public void testCustomUserDetails_getAdminRoles() {
        CustomUserDetails customUserDetails = new CustomUserDetails(userInfoList.get(ADMIN_NAME));
        Collection<? extends GrantedAuthority> userRoles = customUserDetails.getAuthorities();
        assertThat(userRoles).isNotNull();
        assertThat(userRoles).isNotEmpty();
        assertThat(userRoles.size()).isEqualTo(3);
    }
    @Test
    public void testCustomUserDetails_getEditorRoles() {
        CustomUserDetails customUserDetails = new CustomUserDetails(userInfoList.get(EDITOR_NAME));
        Collection<? extends GrantedAuthority> userRoles = customUserDetails.getAuthorities();
        assertThat(userRoles).isNotNull();
        assertThat(userRoles).isNotEmpty();
        assertThat(userRoles.size()).isEqualTo(2);
    }
    @Test
    public void testCustomUserDetails_getViewerRoles() {
        CustomUserDetails customUserDetails = new CustomUserDetails(userInfoList.get(VIEWER_NAME));
        Collection<? extends GrantedAuthority> userRoles = customUserDetails.getAuthorities();
        assertThat(userRoles).isNotNull();
        assertThat(userRoles).isNotEmpty();
        assertThat(userRoles.size()).isEqualTo(1);
    }
}
