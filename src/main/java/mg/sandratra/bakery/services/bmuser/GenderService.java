package mg.sandratra.bakery.services.bmuser;

import mg.sandratra.bakery.models.bmuser.Gender;
import mg.sandratra.bakery.repository.bmuser.GenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;

    public List<Gender> findAll() {
        return genderRepository.findAll();
    }

    public void validateGender(Gender gender) {
        Assert.hasText(gender.getName(), "Gender name must not be empty");
    }
}