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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERID")
    private Long userId;

    @Column(name = "PROVIDER")
    private String provider;

    @Column(name = "PROVIDERUSERID")
    private String providerUserId;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DISPLAYNAME")
    private String displayName;

    @Column(name = "PROFILEPICTUREURL")
    private String profilePictureUrl;

    // From org.springframework.security.core.userdetails.User
    @Column(name = "USERNAME")
    private String username;
    @Transient
    private String password;
    @Transient
    @Singular
    private Set<GrantedAuthority> authorities;
    @Column(name = "ACCOUNTNONEXPIRED")
    @Builder.Default
    private boolean accountNonExpired = true;
    @Column(name = "ACCOUNTNONLOCKED")
    @Builder.Default
    private boolean accountNonLocked = true;
    @Column(name = "CREDENTIALSNONEXPIRED")
    @Builder.Default
    private boolean credentialsNonExpired = true;
    @Column(name = "ENABLED")
    @Builder.Default
    private boolean enabled = true;

    // From org.springframework.security.oauth2.core.user.DefaultOAuth2User
    @Transient
    @Singular
    private Map<String, Object> attributes;

    // From org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
    @Transient
    private OidcIdToken idToken;
    @Transient
    private OidcUserInfo userInfo;

    // For org.springframework.security.core.AuthenticatedPrincipal
    @Transient
    private final String nameAttributeKey = IdTokenClaimNames.SUB;

    @CreationTimestamp
    @Column(name = "CREATEDAT", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "MODIFIEDAT", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    // Explicit Getters. Need to override superinterfaces. Setters set by Lombok.
    public static long getSerialversionuid() { return serialVersionUID; }
    public Long getUserId() { return userId; }
    public String getProvider() { return provider; }
    public String getProviderUserId() { return providerUserId; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

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
        return this.getAttribute(this.nameAttributeKey).toString();
    }
    public Date getCreatedAt() { return createdAt; }
    public Date getModifiedAt() { return modifiedAt; }
}
