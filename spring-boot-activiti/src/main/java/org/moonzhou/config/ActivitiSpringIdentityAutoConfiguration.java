package org.moonzhou.config;


import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author moon zhou
 */
@Configuration
public class ActivitiSpringIdentityAutoConfiguration {
    @Bean
    public UserGroupManager userGroupManager() {
        return new UserGroupManager() {
            @Override
            public List<String> getUserGroups(String s) {
//                return ImmutableList.of("指定用户归属组");
                return Arrays.asList("指定用户归属组");
            }
            @Override
            public List<String> getUserRoles(String s) {
                return null;
            }
            @Override
            public List<String> getGroups() {
                return null;
            }
            @Override
            public List<String> getUsers() {
                return null;
            }
        };
    }
}
