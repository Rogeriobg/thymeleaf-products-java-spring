package br.com.rbgord.LojaCompleta.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rbgord.LojaCompleta.models.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {

	List<Product> findAll();

}
