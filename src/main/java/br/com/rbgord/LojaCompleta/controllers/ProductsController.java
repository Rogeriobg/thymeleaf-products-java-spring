package br.com.rbgord.LojaCompleta.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.sql.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.rbgord.LojaCompleta.models.Product;
import br.com.rbgord.LojaCompleta.models.ProductDto;
import br.com.rbgord.LojaCompleta.services.ProductsRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {

	@Autowired
	private ProductsRepository repo;
	
	@GetMapping({"", "/"})
	public String ShowProductList (Model model) {
		List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("products", products);
		return "products/index";
	}
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ProductDto productDto = new ProductDto();
		model.addAttribute("productDto", productDto);
		return "products/CreateProduct";
	}
	
	

	    @PostMapping("/create")
	    public String createProduct(
	    		@Valid @ModelAttribute ProductDto productDto, 
	    		BindingResult result
	    		){
	    	if(productDto.getImageFile().isEmpty()){
	    		
	    		result.addError(new FieldError("productDto", "imageFile", "E necessário um arquivo de imagem"));
	    	}
	    	   if (result.hasErrors()) {
	               return "products/CreateProduct";
	           }
	    	  
	    	
	    
	    MultipartFile image = productDto.getImageFile();
	    Date createdAt = new Date(System.currentTimeMillis());
	    String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
	    
	    try {
	    	String uploadDir = "public/images/";
	    	Path uploadPath = Paths.get(uploadDir);
	    	
	    	if(!Files.exists(uploadPath)) {
	    		Files.createDirectories(uploadPath);
	    	}
	    	
	    	try (InputStream inputStream = image.getInputStream()){
	    		Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
	    				StandardCopyOption.REPLACE_EXISTING);
	    	}
	    	
	    } catch (Exception ex) {
	    	System.out.println("Exception: " + ex.getMessage());
	    }
        
        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);
        
        repo.save(product);

        return "redirect:/products";
    }
	    
	    
	    @GetMapping("/edit")
	    public String showEditPage(Model model, @RequestParam int id) {
	        try {
	            Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	            model.addAttribute("product", product);

	            ProductDto productDto = new ProductDto();
	            productDto.setName(product.getName());
	            productDto.setBrand(product.getBrand());
	            productDto.setCategory(product.getCategory());
	            productDto.setPrice(product.getPrice());
	            productDto.setDescription(product.getDescription());

	            model.addAttribute("productDto", productDto);
	        } catch (Exception ex) {
	            System.out.println("Exception: " + ex.getMessage());
	            return "redirect:/products";
	        }
	     
	        return "products/EditProduct"; // Certifique-se de que esta string corresponde ao caminho do arquivo HTML
	    }
	    
	    
	    @PostMapping("/edit")
	    public String updateProduct(
	            Model model,
	            @RequestParam int id,
	            @Valid @ModelAttribute("productDto") ProductDto productDto,
	            BindingResult result
	    ) {
	        try {
	            Product products = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	            model.addAttribute("products", products);

	            if (result.hasErrors()) {
	                return "products/EditProduct";
	            }

	            if (!productDto.getImageFile().isEmpty()) {
	                // Delete old image
	                String uploadDir = "public/images/";
	                Path oldImagePath = Paths.get(uploadDir + products.getImageFileName());

	                try {
	                    Files.deleteIfExists(oldImagePath);
	                } catch (Exception ex) {
	                    System.out.println("Exception: " + ex.getMessage());
	                }

	                MultipartFile image = productDto.getImageFile();
	                Date createdAt = new Date(System.currentTimeMillis());
	                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

	                try (InputStream inputStream = image.getInputStream()) {
	                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
	                }

	                products.setImageFileName(storageFileName);
	            }

	            products.setName(productDto.getName());
	            products.setBrand(productDto.getBrand());
	            products.setCategory(productDto.getCategory());
	            products.setPrice(productDto.getPrice());
	            products.setDescription(productDto.getDescription());

	            repo.save(products);

	        } catch (Exception ex) {
	            System.out.println("Exception: " + ex.getMessage());
	            
	        }

	        return "redirect:/products";
	    }
	    
	    @GetMapping("/delete")
	    public String deleteProduct(@RequestParam int id) {
	        try {
	            Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	            
	            // Corrigir o caminho da imagem
	            Path imagePath = Paths.get("public/images/" + product.getImageFileName());
	            
	            try {
	                Files.deleteIfExists(imagePath);
	            } catch (Exception ex) {
	                System.out.println("Exception: " + ex.getMessage());
	            }
	            
	            // Deletar o produto
	            repo.delete(product);
	        } catch (Exception ex) {
	            System.out.println("Exception: " + ex.getMessage());
	        }
	        return "redirect:/products";
	    }
}

	    

