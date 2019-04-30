package com.model2.mvc.service.product;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;

public interface ProductDao {
		//insert
		public void addProduct(Product product) throws Exception;
		//select one
		public Product getProduct(int prodNo) throws Exception;
		//select list
		public List<Product> getProductList(Search search) throws Exception;
		//update
		public void updateProduct(Product product) throws Exception;
		//totalcount
		public int getTotalCount(Search search);
		
	

}
