package com.gagara.products.controllers;

import com.gagara.products.entities.Product;
import com.gagara.products.services.ProductService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class ProductController {
    private ProductService productService;
    private ServletContext servletContext;

    public ProductController(ProductService productService, ServletContext servletContext) {
        this.productService = productService;
        this.servletContext = servletContext;
    }

    @GetMapping("/products")
    public List<Product> allProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/addProduct")
    public ResponseEntity<String> addNewProduct(@RequestParam("name") String name, @RequestParam("calories") long calories, @RequestParam(name = "photo", required = false) MultipartFile photo){
        try {
            productService.addProduct(name, calories, photo);
            return ResponseEntity.ok("product has been added");
        }
        catch (IOException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/getProduct/{id}")
    public Product getProduct(@PathVariable long id){
        return productService.getSingleProduct(id);
    }

    @GetMapping("/getProductsByName/{name}")
    public List<Product> getProductByName(@PathVariable String name){
        return productService.getSingleProductByName(name);
    }

    @PostMapping("/updateProduct/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable long id, @RequestParam("name") String name, @RequestParam("calories") long calories,
        @RequestParam(name = "photo", required = false) MultipartFile photo)
    {
        try{
            productService.changeProduct(id, name, calories, photo);
            return ResponseEntity.ok("product has been updated");
        }
        catch (IOException | EntityNotFoundException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        try{
            productService.removeProduct(id);
            return ResponseEntity.ok("product has been updated");
        }
        catch (IOException | EntityNotFoundException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/getPhoto")
    public ResponseEntity<byte[]> getPhoto(@RequestBody String path){
        try {
            Path photoPath = productService.getPhotoPath(path);
            byte[] photoBytes = Files.readAllBytes(photoPath);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(servletContext.getMimeType(photoPath.toAbsolutePath().toString())))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + photoPath.getFileName() + "\"")
                .body(photoBytes);
        }
        catch (IOException exception){
            return ResponseEntity.ok().body(null);
        }
    }

}
