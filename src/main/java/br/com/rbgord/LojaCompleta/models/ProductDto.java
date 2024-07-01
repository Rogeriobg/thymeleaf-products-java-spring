package br.com.rbgord.LojaCompleta.models;



import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public class ProductDto {

	@NotEmpty(message = "O nome é requerido")
	private String name;
	
	@NotEmpty(message = "A marca é obrigatória")
	private String brand;
	
	@NotEmpty(message = "A categoria é obrigatória")
    private String category;
	
	@Min(0)
    private double price;
	
	@Size(min = 10, message = "A descrição deve ter pelo menos 10 caracteres")
	@Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
	private String description;
	
	private MultipartFile  imageFile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	
	
}
