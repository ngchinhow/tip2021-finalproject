package com.tfip2021.module4.models;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import static com.tfip2021.module4.models.Constants.PACKAGE_SERIAL_VERSION_UID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(
    name = "USER",
    indexes = @Index(
        name = "email_index",
        columnList = "email"
    )
)
public class DatabaseUser implements OidcUser, UserDetails {
    private static final long serialVersionUID = PACKAGE_SERIAL_VERSION_UID;
    // For org.springframework.security.core.AuthenticatedPrincipal
    @Transient
    private static final String NAME_ATTRIBUTE_KEY = IdTokenClaimNames.SUB;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userId;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provideruserid", length = 500)
    private String providerUserId;

    @Column(name = "email")
    private String email;

    @Column(name = "displayname")
    private String displayName;

    @Column(name = "profilepictureurl")
    private String profilePictureUrl;

    // From org.springframework.security.core.userdetails.User
    @Column(name = "username")
    private String username;
    
    @Column(name = "password")
    private String password;

    @Transient
    @Singular
    @Getter(AccessLevel.NONE)
    private Set<GrantedAuthority> authorities;

    @Column(name = "accountnonexpired")
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private boolean accountNonExpired = true;

    @Column(name = "accountnonlocked")
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private boolean accountNonLocked = true;

    @Column(name = "credentialsnonexpired")
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Column(name = "enabled")
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private boolean enabled = true;

    // From org.springframework.security.oauth2.core.user.DefaultOAuth2User
    @Transient
    @Singular
    @Getter(AccessLevel.NONE)
    private transient Map<String, Object> attributes;

    // From org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
    @Transient
    @Getter(AccessLevel.NONE)
    private OidcIdToken idToken;
    @Transient
    @Getter(AccessLevel.NONE)
    private OidcUserInfo userInfo;

    @CreationTimestamp
    @Column(name = "createdat", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "modifiedat", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Override // From UserDetails
    public Set<GrantedAuthority> getAuthorities() { return authorities; }
    @Override // From UserDetails
    public boolean isAccountNonExpired() { return accountNonExpired; }
    @Override // From UserDetails
    public boolean isAccountNonLocked() { return accountNonLocked; }
    @Override // From UserDetails
    public boolean isCredentialsNonExpired() { return credentialsNonExpired; }
    @Override // From UserDetails
    public boolean isEnabled() { return enabled; }
    @Override // From OidcUser
    public Map<String, Object> getClaims() { return this.getAttributes(); }
    @Override // From OAuth2AuthenticatedPrincipal
    public Map<String, Object> getAttributes() { return attributes; }
    @Override // From OidcUser
    public OidcIdToken getIdToken() { return idToken; }
    @Override // From OidcUser
    public OidcUserInfo getUserInfo() { return userInfo; }
    @Override // From AuthenticatedPrincipal
    public String getName() {
        return this.getAttribute(NAME_ATTRIBUTE_KEY).toString();
    }
}
