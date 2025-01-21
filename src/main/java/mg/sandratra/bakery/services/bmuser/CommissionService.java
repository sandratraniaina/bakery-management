package mg.sandratra.bakery.services.bmuser;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.models.bmuser.Commission;
import mg.sandratra.bakery.repository.bmuser.CommissionRepository;
import mg.sandratra.bakery.utils.filter.Filter;

@Service
@RequiredArgsConstructor
public class CommissionService {
    private final CommissionRepository commissionRepository;

    public List<Commission> search(Filter filter) {
        return commissionRepository.search(filter);
    }
}
