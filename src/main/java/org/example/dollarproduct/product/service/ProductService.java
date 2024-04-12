package org.example.dollarproduct.product.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dollarproduct.entity.OrderDetail;
import org.example.dollarproduct.feign.FeignOrderClient;
import org.example.dollarproduct.product.dto.request.ProductRequest;
import org.example.dollarproduct.product.dto.request.ProductUpdateRequest;
import org.example.dollarproduct.product.dto.request.StockUpdateRequest;
import org.example.dollarproduct.product.dto.response.OrderDetailAdminResponse;
import org.example.dollarproduct.product.dto.response.ProductAdminResponse;
import org.example.dollarproduct.product.dto.response.ProductDetailResponse;
import org.example.dollarproduct.product.dto.response.ProductResponse;
import org.example.dollarproduct.product.entity.Product;
import org.example.dollarproduct.product.repository.ProductRepository;
import org.example.dollarproduct.s3.S3Service;
import org.example.dollarproduct.feign.FeignUserClient;
import org.example.share.config.global.entity.user.User;
import org.example.share.config.global.entity.user.UserRoleEnum;
import org.example.share.config.global.exception.AccessDeniedException;
import org.example.share.config.global.exception.NotFoundException;
import org.example.share.config.global.exception.UnauthorizedAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FeignUserClient feignUserClient;
    private final S3Service s3Service;
    private final FeignOrderClient feignOrderClient;

    @Value("${product.bucket.name}")
    String bucketName;

    public List<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByStateTrue(pageable);
        return getPageResponse(productPage);
    }

    public ProductDetailResponse getProductDetail(Long productId){
        Product product = getProduct(productId);
        checkProductStateIsFalse(product);
        User user = feignUserClient.findById(product.getUserId());
        return new ProductDetailResponse(product, user.getUsername());
    }

    public List<ProductResponse> getAllProductsBySearch(String search, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCaseAndStateTrue(
            search, pageable);
        return getPageResponse(productPage);
    }

    public void createAdminProduct(ProductRequest productRequest, User user) {
        validateUserRole(user);

        Product product = Product.builder()
            .name(productRequest.getName())
            .price(productRequest.getPrice())
            .description(productRequest.getDescription())
            .stock(productRequest.getStock())
            .userId(user.getId())
            .build();

        productRepository.save(product);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public List<ProductAdminResponse> getAdminProducts(User user, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByUserIdAndStateTrue(user.getId(), pageable);
        return getPageResponse2(productPage);
    }

    @Transactional
    public void updateAdminProduct(Long productId, ProductUpdateRequest productRequest, User user) {
        Product product = getProduct(productId);

        checkProductStateIsFalse(product);

        validateProductOwner(user, product);

        product.update(productRequest);
    }

    @Transactional
    public void updateAdminProductStock(Long productId, StockUpdateRequest stockupdateRequest,
        User user) {
        Product product = getProduct(productId);

        checkProductStateIsFalse(product);

        validateProductOwner(user, product);

        product.updateStock(stockupdateRequest);
    }

    @Transactional
    public void deleteAdminProduct(Long productId, User user){
        Product product = getProduct(productId);

        checkProductStateIsFalse(product);

        validateProductOwner(user, product);

        product.delete();
    }

    public void checkProductStateIsFalse(Product product) {
        if (!product.isState()) {
            throw new NotFoundException("해당 상품은 삭제되었습니다.");
        }
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new NotFoundException("해당 상품이 존재하지 않습니다.")
        );
    }

    private void validateUserRole(User user) {
        if (!user.getRole().equals(UserRoleEnum.SELLER)) {
            throw new UnauthorizedAccessException("인증되지 않은 유저입니다.");
        }
    }

    private void validateProductOwner(User user, Product product) {
        if (!product.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("해당 상품의 권한유저가 아닙니다.");
        }
    }

    private List<ProductResponse> getPageResponse(Page<Product> productPage) {
        return productPage.getContent().stream()
            .map(ProductResponse::new)
            .collect(Collectors.toList());
    }

    private List<ProductAdminResponse> getPageResponse2(Page<Product> productPage) {
        return productPage.getContent().stream()
            .map(product -> {
                List<OrderDetail> orderDetails = feignOrderClient.findOrderDetailsByProductId(product.getId());
                List<OrderDetailAdminResponse> orderDetailResponseDtos = orderDetails.stream()
                    .map(OrderDetailAdminResponse::new)
                    .collect(Collectors.toList());
                return new ProductAdminResponse(product, orderDetailResponseDtos);
            })
            .collect(Collectors.toList());
    }

    public void uploadProductImage(Long productId, MultipartFile file) throws IOException {
        String imageKey = UUID.randomUUID().toString();
        String format = "product-images/%s/%s".formatted(productId,
            imageKey)+".PNG";
        s3Service.putObject(
            bucketName,format,
            file);
        String url = "https://"+bucketName+".s3"+".ap-northeast-2.amazonaws.com/"+format;
        Product product = getProduct(productId);
        product.updateImageUrl(url);
        productRepository.save(product);
    }

    public String getProductImage(Long productId) {
        try {
            return getProduct(productId).getImageUrl();
        } catch (NoSuchKeyException e) {
            throw new NotFoundException("요청한 상품 이미지가 S3 버킷에 존재하지 않습니다. 이미지 키를 확인해주세요.");
        }

    }

}

