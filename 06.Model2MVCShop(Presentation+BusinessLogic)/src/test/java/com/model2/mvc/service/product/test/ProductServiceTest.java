package com.model2.mvc.service.product.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

/*
 *	FileName :  UserServiceTest.java
 * ㅇ JUnit4 (Test Framework) 과 Spring Framework 통합 Test( Unit Test)
 * ㅇ Spring 은 JUnit 4를 위한 지원 클래스를 통해 스프링 기반 통합 테스트 코드를 작성 할 수 있다.
 * ㅇ @RunWith : Meta-data 를 통한 wiring(생성,DI) 할 객체 구현체 지정
 * ㅇ @ContextConfiguration : Meta-data location 지정
 * ㅇ @Test : 테스트 실행 소스 지정
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/commonservice.xml" })
public class ProductServiceTest {

	// ==>@RunWith,@ContextConfiguration 이용 Wiring, Test 할 instance DI
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	// @Test
	public void testAddProduct() throws Exception {

		Product product = new Product();
		product.setProdName("사과맛쿠키");
		product.setProdDetail("사과잼이 들어있어요");
		product.setManuDate("2019-04-23");
		product.setPrice(2000);
		product.setFileName("fileName");

		productService.addProduct(product);

		product = productService.getProduct(10010);

		// ==> console 확인
		// System.out.println(user);

		// ==> API 확인
		Assert.assertEquals("사과맛쿠키", product.getProdName());
		Assert.assertEquals("사과잼이 들어있어요", product.getProdDetail());
		Assert.assertEquals("2019-04-23", product.getManuDate());
		Assert.assertEquals(2000, product.getPrice());
		Assert.assertEquals("fileName", product.getFileName());
	}

	// @Test
	public void testGetProduct() throws Exception {

		Product product = productService.getProduct(10010);

		// ==> console 확인
		// System.out.println(user);

		// ==> API 확인
		System.out.println(product);
		System.out.println(productService.getProduct(10020));

		Assert.assertNotNull(productService.getProduct(10010));
		Assert.assertNotNull(productService.getProduct(10020));
	}

	// @Test
	public void testUpdateProduct() throws Exception {

		Product product = productService.getProduct(10013);
		Assert.assertNotNull(product);

		Assert.assertEquals("사과맛쿠키", product.getProdName());
		Assert.assertEquals("사과잼이 들어있어요", product.getProdDetail());
		Assert.assertEquals("2019-04-23", product.getManuDate());
		Assert.assertEquals(2000, product.getPrice());
		Assert.assertEquals("fileName", product.getFileName());

		product.setProdName("초코칩쿠키");
		product.setProdDetail("촉촉한");
		product.setPrice(2300);

		productService.updateProduct(product);

		product = productService.getProduct(10013);
		Assert.assertNotNull(product);
		System.out.println(productService.getProduct(10013));

	}

	//@Test
	public void testGetProductListAll() throws Exception {

		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		Map<String, Object> map = productService.getProductList(search);

		List<Object> list = (List<Object>) map.get("list");
		Assert.assertEquals(3, list.size());

		// ==> console 확인
		// System.out.println(list);

		Integer totalCount = (Integer) map.get("totalCount");
		System.out.println(totalCount);

		System.out.println("=======================================");

		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("0");
		search.setSearchKeyword("");
		map = productService.getProductList(search);

		list = (List<Object>) map.get("list");
		Assert.assertEquals(3, list.size());

		// ==> console 확인
		// System.out.println(list);

		totalCount = (Integer) map.get("totalCount");
		System.out.println(totalCount);
	}

	@Test
	public void testGetProductListByProdNo() throws Exception {

		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("0");
		search.setSearchKeyword("10011");
		Map<String, Object> map = productService.getProductList(search);
		

		List<Object> list = (List<Object>) map.get("list");
		Assert.assertEquals(1, list.size());

		// ==> console 확인
		 System.out.println(list);

		Integer totalCount = (Integer) map.get("totalCount");
		System.out.println(totalCount);

		System.out.println("=======================================");

		search.setSearchCondition("0");
		search.setSearchKeyword("10004");
		//search.setSearchKeyword("" + System.currentTimeMillis());
		map=productService.getProductList(search);

		list = (List<Object>) map.get("list");
		Assert.assertEquals(1, list.size());

		// ==> console 확인
		System.out.println(list);

		totalCount = (Integer) map.get("totalCount");
		System.out.println(totalCount);
	}

	//@Test
	public void testGetProductListByProdName() throws Exception {

		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("1");
		search.setSearchKeyword("누네띠네");
		Map<String, Object> map = productService.getProductList(search);

		List<Object> list = (List<Object>) map.get("list");
		System.out.println(list);

		Assert.assertEquals(1, list.size());


		// ==> console 확인
		System.out.println(list);

		Integer totalCount = (Integer) map.get("totalCount");
		System.out.println(totalCount);

		System.out.println("=======================================");

		search.setSearchCondition("1");
		search.setSearchKeyword("인라인");
		//search.setSearchKeyword("" + System.currentTimeMillis());
		map=productService.getProductList(search);

		list = (List<Object>) map.get("list");
		Assert.assertEquals(1, list.size());

		// ==> console 확인
		System.out.println(list);

		totalCount = (Integer) map.get("totalCount");
		System.out.println(totalCount);
	}
}