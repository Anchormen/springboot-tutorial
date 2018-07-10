package nl.anchormen.usermanager.user;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    @Email
    private String email;
    @Pattern(regexp="^(\\s|\\d|[\\+\\(\\)\\-])*$", message="Invalid phonenumber")
    private String phoneNumber;
    
    @JsonIgnore
    @NotEmpty
    private String encodedPassword;
    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole UserRole;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }


    public UserRole getUserRole()
    {
        return UserRole;
    }

    public void setUserRole(UserRole userRole)
    {
        UserRole = userRole;
    }

    public String getEncodedPassword()
    {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword)
    {
        this.encodedPassword = encodedPassword;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((UserRole == null) ? 0 : UserRole.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((encodedPassword == null) ? 0 : encodedPassword.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (UserRole != other.UserRole)
            return false;
        if (email == null)
        {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (encodedPassword == null)
        {
            if (other.encodedPassword != null)
                return false;
        } else if (!encodedPassword.equals(other.encodedPassword))
            return false;
        if (id == null)
        {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (phoneNumber == null)
        {
            if (other.phoneNumber != null)
                return false;
        } else if (!phoneNumber.equals(other.phoneNumber))
            return false;
        return true;
    }
    
}
