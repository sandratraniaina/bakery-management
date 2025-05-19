package mg.sandratra.bakery.services.sale;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mg.sandratra.bakery.dto.sale.SaleDto;
import mg.sandratra.bakery.dto.sale.SaleDetailsDto;
import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.models.sale.Sale;
import mg.sandratra.bakery.repository.sale.SaleRepository;
import mg.sandratra.bakery.services.product.ProductService;
import mg.sandratra.bakery.utils.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailsService saleDetailsService;
    private final ProductService productService;

    // Map Sale to SaleDto
    public SaleDto mapToDto(Sale sale) {
        List<SaleDetailsDto> saleDetails = saleDetailsService.findBySaleId(sale.getId()).stream()
                .map(saleDetailsService::mapToDto)
                .toList();

        saleDetails = new ArrayList<>(saleDetails);

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

    public SaleDto generateSaleDto() {
        List<Product> products = productService.findAll();

        List<SaleDetailsDto> saleDetails = saleDetailsService.generateSaleDetailsDtoFromProducts(products);

        return new SaleDto(null, null, "Anonymous", saleDetails, null, null, null);
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public List<Sale> search(Filter filter) {
        return saleRepository.search(filter);
    }

    @Transactional(rollbackFor = { Exception.class, SQLException.class })
    public Long saveSale(SaleDto saleDto) {
        Sale sale = mapToModel(saleDto);
        sale.setTotalAmount(saleDto.calculateTotalAmount());
        Long result;

        if (sale.getId() != null) {
            saleRepository.update(sale);
            result = sale.getId();
        } else {
            result = saleRepository.save(sale);
        }

        if (result > 0) {
            for (SaleDetailsDto saleDetailsDto : saleDto.getSaleDetails()) {
                if (saleDetailsDto.getQuantity() == 0) {
                    continue;
                }
                saleDetailsService.saveOrUpdate(saleDetailsService.mapToModel(saleDetailsDto, result));
            }
        }

        return result;
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id);
    }

    public List<Sale> findByDate(Date date) {
        if (date == null) {
            return saleRepository.findAll();
        } else {
            return saleRepository.findByDate(date);
        }
    }

    public Long saveOrUpdate(Sale sale) {
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
        Assert.hasText(sale.getClientName(), "Client name must not be empty");
        Assert.notNull(sale.getTotalAmount(), "Total amount must not be null");
        Assert.notNull(sale.getSaleDate(), "Sale date must not be null");
    }
}
