package mg.sandratra.bakery.services.bmuser;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.models.bmuser.BmUser;
import mg.sandratra.bakery.repository.bmuser.BmUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BmUserService {

    private final BmUserRepository bmUserRepository;

    public List<BmUser> findAll() {
        return bmUserRepository.findAll();
    }

    public BmUser findById(Long id) {
        return bmUserRepository.findById(id);
    }

    public int saveOrUpdate(BmUser bmUser) {
        validateBmUser(bmUser);
        if (bmUser.getId() == null) {
            return bmUserRepository.save(bmUser);
        } else {
            return bmUserRepository.update(bmUser);
        }
    }

    public int deleteById(Long id) {
        return bmUserRepository.deleteById(id);
    }

    private void validateBmUser(BmUser bmUser) {
        Assert.hasText(bmUser.getUserName(), "Username must not be empty");
        Assert.notNull(bmUser.getRole(), "Role must not be null");
    }
}
