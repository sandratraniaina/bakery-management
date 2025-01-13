package mg.sandratra.bakery.models.bmuser;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.enums.Role;
import mg.sandratra.bakery.utils.PasswordEncoderUtil;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmUser {
    private Long id;
    private String userName;
    private String passwordHash;
    private Role role;
    private boolean enabled;
    private Timestamp createdAt;

    public String getEncodedPassword() {
        return PasswordEncoderUtil.encode(getPasswordHash());
    }

    public boolean getEnabled() {
        return enabled;
    }
}
