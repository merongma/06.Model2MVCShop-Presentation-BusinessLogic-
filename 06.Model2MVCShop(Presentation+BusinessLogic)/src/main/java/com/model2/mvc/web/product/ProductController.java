package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

//==> 회원관리 Controller
@Controller
public class ProductController {

	/// Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	// setter Method 구현 않음

	public ProductController() {
		System.out.println(this.getClass());
	}

	// ==> classpath:config/common.properties , classpath:config/commonservice.xml
	// 참조 할것
	// ==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	// @Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	// @Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;

	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception {

		System.out.println("/addProductView.do");

		return "forward:/product/addProductView.jsp";
	}

	@RequestMapping("/addProduct.do")
	public String addUser(@ModelAttribute("product") Product product) throws Exception {

		System.out.println("/addProduct.do");
		// Business Logic
		productService.addProduct(product);

		return "forward:/product/addProduct.jsp";
	}

	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") int prodNo, Model model, @RequestParam("menu") String menu) throws Exception {

		System.out.println("/getProduct.do");
		// Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);

		
		System.out.println("menu값"+menu);
		if (menu.equals("manage")) {

			return "forward:/product/updateProductView.jsp";

		} else {

			return "forward:/product/getProduct.jsp";
		}

	}

	@RequestMapping("/updateProductView.do")
	public String updateProductView(@RequestParam("prodNo") int prodNo, Model model) throws Exception {

		System.out.println("/updateProductView.do");
		// Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);

		return "forward:/product/updateProductView.jsp";
	}

	@RequestMapping("/updateProduct.do")
	public String updateProduct(@ModelAttribute("product") Product product, Model model, HttpSession session, @RequestParam("prodNo") int prodNo)
			throws Exception {

		System.out.println("/updateProduct.do");
		
		productService.updateProduct(product);
		System.out.println("prodNo 값 확인 : "+prodNo);
		
		Product product2 =productService.getProduct(prodNo);
		product.setRegDate(product2.getRegDate());
		
		model.addAttribute("product",product);
		
//		session.setAttribute("product", product);
			
		System.out.println("리턴되기 전 product 값 :"+product);

		return "forward:/product/updateProduct.jsp";

	}

	@RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("search") Search search, Model model)
			throws Exception {

		System.out.println("/listProduct.do");

		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);

		// Business logic 수행
		Map<String, Object> map = productService.getProductList(search);

		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);
		System.out.println(resultPage);
		

		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "forward:/product/listProduct.jsp";
	}
}