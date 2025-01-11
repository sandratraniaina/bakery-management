package mg.sandratra.bakery.services.sale;

import java.util.List;
import mg.sandratra.bakery.dto.sale.SaleDto;
import mg.sandratra.bakery.dto.sale.SaleDetailsDto;
import mg.sandratra.bakery.models.sale.Sale;
import mg.sandratra.bakery.repository.sale.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailsService saleDetailsService;

    // Map Sale to SaleDto
    public SaleDto mapToDto(Sale sale) {
        List<SaleDetailsDto> saleDetails = saleDetailsService.findBySaleId(sale.getId()).stream()
                .map(saleDetailsService::mapToDto)
                .toList();

        // Map Sale to SaleDto
        return new SaleDto(
                sale.getId(),
                sale.getCreatedBy(),
                sale.getClientName(),
                saleDetails,
                sale.getTotalAmount(),
                sale.getSaleDate(),
                sale.getCreatedAt());
    }

    // Map SaleDto to Sale
    public Sale mapToModel(SaleDto saleDto) {
        return new Sale(
                saleDto.getId(),
                saleDto.getCreatedBy(),
                saleDto.getClientName(),
                saleDto.getTotalAmount(),
                saleDto.getSaleDate(),
                saleDto.getCreatedAt());
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id);
    }

    public int saveOrUpdate(Sale sale) {
        validateSale(sale);
        if (sale.getId() == null) {
            return saleRepository.save(sale);
        } else {
            return saleRepository.update(sale);
        }
    }

    public int deleteById(Long id) {
        return saleRepository.deleteById(id);
    }

    private void validateSale(Sale sale) {
        Assert.notNull(sale.getCreatedBy(), "CreatedBy field must not be null");
        Assert.hasText(sale.getClientName(), "Client name must not be empty");
        Assert.notNull(sale.getTotalAmount(), "Total amount must not be null");
        Assert.notNull(sale.getSaleDate(), "Sale date must not be null");
    }
}
