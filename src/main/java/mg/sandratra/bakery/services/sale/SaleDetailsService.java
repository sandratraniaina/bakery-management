package mg.sandratra.bakery.services.sale;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.dto.sale.SaleDetailsDto;
import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.models.sale.SaleDetails;
import mg.sandratra.bakery.repository.product.ProductRepository;
import mg.sandratra.bakery.repository.sale.SaleDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleDetailsService {

    private final SaleDetailsRepository saleDetailsRepository;
    private final ProductRepository productRepository;

    public SaleDetailsDto mapToDto(SaleDetails saleDetails) {
        Product product = productRepository.findById(saleDetails.getProductId());

        return new SaleDetailsDto(
                saleDetails.getId(),
                product,
                saleDetails.getQuantity(),
                saleDetails.getUnitPrice(),
                saleDetails.getSubTotal(),
                saleDetails.getCreatedAt());
    }

    public SaleDetails mapToModel(SaleDetailsDto saleDetailsDto, Long saleId) {
        Assert.notNull(saleDetailsDto.getProduct(), "Product must not be null");
        return new SaleDetails(
                saleDetailsDto.getId(),
                saleId,
                saleDetailsDto.getProduct().getId(),
                saleDetailsDto.getQuantity(),
                saleDetailsDto.getUnitPrice(),
                saleDetailsDto.getUnitPrice().multiply(BigDecimal.valueOf(saleDetailsDto.getQuantity())),
                saleDetailsDto.getCreatedAt());
    }

    public List<SaleDetailsDto> getSaleDetailsNotAssignedProduct(Long saleId) {
        List<Product> notAssignedProduct = productRepository.getNotAssignedProduct(saleId);
        return generateSaleDetailsDtoFromProducts(notAssignedProduct);
    }

    public List<SaleDetailsDto> generateSaleDetailsDtoFromProducts(List<Product> products) {
        List<SaleDetailsDto> saleDetails = new ArrayList<>();

        for (Product product : products) {
            SaleDetailsDto saleDetailsDto = new SaleDetailsDto();
            saleDetailsDto.setProduct(product);
            saleDetailsDto.setQuantity(0);
            saleDetailsDto.setUnitPrice(product.getPrice());
            saleDetailsDto.setSubTotal(BigDecimal.ZERO);
            saleDetails.add(saleDetailsDto);
        }

        return saleDetails;
    }

    public List<SaleDetails> findAll() {
        return saleDetailsRepository.findAll();
    }

    public SaleDetails findById(Long id) {
        return saleDetailsRepository.findById(id);
    }

    public List<SaleDetails> findBySaleId(Long saleId) {
        return saleDetailsRepository.findBySaleId(saleId);
    }

    public int saveOrUpdate(SaleDetails saleDetails) {
        validateSaleDetails(saleDetails);
        if (saleDetails.getId() == null) {
            return saleDetailsRepository.save(saleDetails);
        } else {
            return saleDetailsRepository.update(saleDetails);
        }
    }

    public int deleteById(Long id) {
        return saleDetailsRepository.deleteById(id);
    }

    public void validateSaleDetails(SaleDetails saleDetails) {
        Assert.notNull(saleDetails.getProductId(), "Product ID must not be null");
        Assert.isTrue(saleDetails.getQuantity() > 0, "Quantity must be greater than zero");
        Assert.isTrue(saleDetails.getUnitPrice().compareTo(BigDecimal.ZERO) > 0,
                "Unit price must be greater than zero");
    }
}
