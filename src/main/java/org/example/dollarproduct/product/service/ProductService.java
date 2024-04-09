package org.example.dollarproduct.product.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dollarproduct.product.dto.request.ProductRequest;
import org.example.dollarproduct.product.dto.request.ProductUpdateRequest;
import org.example.dollarproduct.product.dto.request.StockUpdateRequest;
import org.example.dollarproduct.product.dto.response.ProductDetailResponse;
import org.example.dollarproduct.product.dto.response.ProductResponse;
import org.example.dollarproduct.product.entity.Product;
import org.example.dollarproduct.product.repository.ProductRepository;
import org.example.dollarproduct.s3.S3Service;
import org.example.dollarproduct.user.FeignUserClient;
import org.example.share.config.global.entity.user.User;
import org.example.share.config.global.entity.user.UserRoleEnum;
import org.example.share.config.global.exception.AccessDeniedException;
import org.example.share.config.global.exception.NotFoundException;
import org.example.share.config.global.exception.UnauthorizedAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    public List<ProductResponse> getAdminProducts(User user, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByUserIdAndStateTrue(user.getId(), pageable);
        return getPageResponse(productPage);
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

    public void uploadProductImage(Long productId, MultipartFile file) throws IOException {
        String imageKey = UUID.randomUUID().toString();
        s3Service.putObject(
            bucketName, "product-images/%s/%s".formatted(productId,
                imageKey),
            file.getBytes());
        Product product = getProduct(productId);
        product.updateImageKey(imageKey);
        productRepository.save(product);
    }

    public ResponseEntity<byte[]> getProductImage(Long productId) {
        try {
            String ImageKey = "product-images/1/"+getProduct(productId).getImageKey();
            return s3Service.getImage(bucketName,ImageKey);
        } catch (NoSuchKeyException e) {
            throw new NotFoundException("요청한 상품 이미지가 S3 버킷에 존재하지 않습니다. 이미지 키를 확인해주세요.");
        } catch (IOException e) {
            throw new RuntimeException("상품 이미지 조회 중 오류가 발생했습니다.", e);
        }

    }

}

