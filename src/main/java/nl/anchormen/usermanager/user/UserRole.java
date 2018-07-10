package nl.anchormen.usermanager.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole
{
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
    
    private final String name;
    
    private UserRole(String name)
    {
        this.name = name;
    }

    // Use name property when translating to json, this requires the name needs to be unique.
    @JsonValue
    public String getName()
    {
        return name;
    }

}
