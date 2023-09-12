package by.touchme.commentservice.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
@Getter
public enum Role implements GrantedAuthority {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_JOURNALIST("ROLE_JOURNALIST"),
    ROLE_USER("ROLE_USER");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
