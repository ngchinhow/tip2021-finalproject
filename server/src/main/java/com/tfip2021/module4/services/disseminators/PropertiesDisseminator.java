package com.tfip2021.module4.services.disseminators;

import java.util.Map;

import com.tfip2021.module4.models.DatabaseUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class PropertiesDisseminator {
    private DatabaseUser user;
    private String operation;
    private String provider;
    private String providerUserId;
    Map<String, Object> attributes;

    protected PropertiesDisseminator(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    /*
    * Used by each subclass corresponding to each Social Provider to 
    * disseminate attributes into individual properties in the DatabaseUser
    */
    public abstract DatabaseUser disseminateAttributes();

    public String getEmail() {
        return (String) this.getAttributes().get("email");
    }

    public boolean isSupportedOperation() {
        return this.getOperation().equals("create")
            || this.getOperation().equals("update");
    }
}
