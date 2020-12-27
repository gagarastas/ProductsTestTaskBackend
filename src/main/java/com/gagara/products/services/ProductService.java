package com.gagara.products.services;

import com.gagara.products.entities.Product;
import com.gagara.products.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product getSingleProduct(long id){
        return productRepository.getOne(id);
    }

    public List<Product> getSingleProductByName(String name){
        List<Product> products = productRepository.getOneByName(name);
        return products;
    }

    public void addProduct(String name, long calories, MultipartFile file) throws IOException {
        String filePath = file == null ? null : savePhoto(file, name);
        Product product = new Product(name, calories, filePath);

        productRepository.save(product);
    }

    public void changeProduct(long id, String name, long calories, MultipartFile file) throws IOException, EntityNotFoundException{
        if(!productRepository.existsById(id)){
            throw new EntityNotFoundException("product does not exist");
        }
        Product product = productRepository.getOne(id);
        String imagePath = product.getImagePath();

        product.setName(name);
        product.setCalories(calories);

        if(file != null) {
            if(imagePath != null) {
                deletePhoto(imagePath);
            }
            String newImagePath = savePhoto(file, name);
            product.setImagePath(newImagePath);
        }
        productRepository.save(product);

    }

    public void removeProduct(long id) throws EntityNotFoundException, IOException {
        if(!productRepository.existsById(id)){
            throw new EntityNotFoundException("product does not exist");
        }
        Product product = productRepository.getOne(id);
        String path = product.getImagePath();
        if(path != null){
            deletePhoto(path);
        }
        productRepository.deleteById(id);
    }

    public Path getPhotoPath(String imagePath){
        return Paths.get(imagePath);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String savePhoto(MultipartFile photo, String name) throws IOException {
        long currentTime = System.currentTimeMillis();
        Files.createDirectory(Paths.get("src", "main", "resources", "images", name + currentTime));
        byte[] photoBytes = photo.getBytes();
        Path imagePath = Paths.get("src", "main", "resources", "images", name + currentTime, photo.getOriginalFilename());
        Files.write(imagePath, photoBytes);
        return imagePath.toString();
    }

    private void deletePhoto(String photoPath) throws IOException {
        int index = photoPath.lastIndexOf("\\");
        String directory = photoPath.substring(0, index);

        Path filePath = Paths.get(photoPath);
        Path directoryPath = Paths.get(directory);

        if(Files.exists(filePath)){
            Files.delete(filePath);
            Files.delete(directoryPath);
        }
    }

}

