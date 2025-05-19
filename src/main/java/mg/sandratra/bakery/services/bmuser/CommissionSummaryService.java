package mg.sandratra.bakery.services.bmuser;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.models.bmuser.CommissionSummary;
import mg.sandratra.bakery.repository.bmuser.CommissionSummaryRepository;

@Service
@RequiredArgsConstructor
public class CommissionSummaryService {

    private final CommissionSummaryRepository commissionSummaryRepository;

    // Method to get commission summaries by gender ID (or all if genderId is null)
    public List<CommissionSummary> getCommissionSummaries(Long genderId) {
        return commissionSummaryRepository.getCommissionSummaries(genderId);
    }
}