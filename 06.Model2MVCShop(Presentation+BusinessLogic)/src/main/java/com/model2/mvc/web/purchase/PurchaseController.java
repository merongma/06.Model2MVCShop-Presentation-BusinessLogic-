package com.model2.mvc.web.purchase;

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
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

//==> 회원관리 Controller
@Controller
public class PurchaseController {

	/// Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	// setter Method 구현 않음

	public PurchaseController() {
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

	@RequestMapping("/addPurchaseView.do")
	public String addPurchaseView(@RequestParam("prod_no") int prodNo, Model model) throws Exception {

		System.out.println("/addPurchaseView.do");
		Product product = productService.getProduct(prodNo);
		System.out.println("prodNo값 확인 " + prodNo);

		model.addAttribute("product", product);

		System.out.println(product);

		return "forward:/purchase/addPurchaseView.jsp";
	}

	@RequestMapping("/addPurchase.do")
	public String addPurchase(@RequestParam("prodNo") int prodNo, @RequestParam("buyerId") String buyerId,
			@ModelAttribute("purchase") Purchase purchase, Model model) throws Exception {

		System.out.println("/addPurchase.do");

		Product product = productService.getProduct(prodNo);
		User user = userService.getUser(buyerId);

		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);

		purchaseService.addPurchase(purchase);
		System.out.println("purchase값 확인 " + purchase);

		model.addAttribute(purchase);

		return "forward:/purchase/addPurchaseViewResult.jsp";
	}

	@RequestMapping("/getPurchase.do")
	public String getPurchase(@RequestParam("tranNo") int tranNo, Model model) throws Exception {

		System.out.println("/getPurchase.do");

		System.out.println("tranNo값 확인 " + tranNo);

		Purchase purchase = purchaseService.getPurchase(tranNo);

		model.addAttribute("purchase", purchase);

		System.out.println("purchase 값 확인 " + purchase);

		return "forward:/purchase/getPurchaseView.jsp";

	}

	@RequestMapping("/listPurchase.do")
	public String listPurchase(@ModelAttribute("search") Search search, Model model, HttpServletRequest request)
			throws Exception {

		System.out.println("/listPurchase.do");

		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);

		User user = (User) request.getSession().getAttribute("user");
		String buyerId = user.getUserId();
		System.out.println("session buyerid : " + buyerId);

		// Business logic 수행
		Map<String, Object> map = purchaseService.getPurchaseList(search, buyerId);

		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);
		System.out.println(resultPage);

		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "forward:/purchase/listPurchase.jsp";
	}

	@RequestMapping("/updatePurchase.do")
	public String updatePurchase(@ModelAttribute("Purchase") Purchase purchase, @RequestParam("tranNo") int tranNo, Model model) throws Exception {

		System.out.println("/updatePurchase.do");
		// Business Logic
		
		
		purchase.setTranNo(tranNo);
		purchaseService.updatePurchase(purchase);
		
		model.addAttribute("purchase", purchase);

		return "forward:/getPurchase.do?tranNo="+tranNo;
	}

	@RequestMapping("/updatePurchaseView.do")
	public String updateProduct(@ModelAttribute("product") Product product, Model model, HttpSession session,
			@RequestParam("prodNo") int prodNo) throws Exception {

		System.out.println("/updatePurchaseView.do");

		productService.updateProduct(product);
		System.out.println("prodNo 값 확인 : " + prodNo);

		Product product2 = productService.getProduct(prodNo);
		product.setRegDate(product2.getRegDate());

		model.addAttribute("product", product);

		// session.setAttribute("product", product);

		System.out.println("리턴되기 전 product 값 :" + product);

		return "forward:/product/updateProduct.jsp";

	}

	// @RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("search") Search search, Model model, HttpServletRequest request)
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