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
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
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
	public String addPurchaseView(@RequestParam("prodNo") int prodNo, Model model) throws Exception {

		System.out.println("/addPurchaseView.do");

		Product product = productService.getProduct(prodNo);

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

		purchase.setPaymentOption(purchase.getPaymentOption().trim());

		model.addAttribute(purchase);

		return "forward:/purchase/addPurchaseViewResult.jsp";
	}

	@RequestMapping("/getPurchase.do")
	public String getPurchase(@RequestParam("tranNo") int tranNo, Model model) throws Exception {

		System.out.println("/getPurchase.do");

		Purchase purchase = purchaseService.getPurchase(tranNo);

		model.addAttribute("purchase", purchase);

		purchase.setPaymentOption(purchase.getPaymentOption().trim());

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
	public String updatePurchase(@ModelAttribute("Purchase") Purchase purchase, @RequestParam("tranNo") int tranNo,
			Model model) throws Exception {

		System.out.println("/updatePurchase.do");

		purchase.setTranNo(tranNo);
		purchaseService.updatePurchase(purchase);

		model.addAttribute("purchase", purchase);

		return "forward:/getPurchase.do";
	}

	@RequestMapping("/updatePurchaseView.do")
	public String updatePurchaseView(@ModelAttribute("Purchase") Purchase purchase, @RequestParam("tranNo") int tranNo,
			Model model) throws Exception {

		System.out.println("/updatePurchaseView.do");

		purchase = purchaseService.getPurchase(tranNo);

		model.addAttribute("purchase", purchase);

		return "forward:/purchase/updatePurchaseView.jsp";

	}

	@RequestMapping("/updateTranCodeByProd.do")
	public String updateTranCodeByProd(@RequestParam("tranCode") String tranCode, @RequestParam("prodNo") int prodNo,
			@ModelAttribute("Purchase") Purchase purchase, @ModelAttribute("Product") Product product)
			throws Exception {

		System.out.println("/updateTranCodeByProd.do");

		product.setProdNo(prodNo);

		purchase.setPurchaseProd(product);
		purchase.setTranCode(tranCode);

		purchaseService.updateTranCode(purchase);

		return "forward:/listProduct.do?prodNo=" + prodNo;
	}

	@RequestMapping("/updateTranCode.do")
	public String updateTranCode(@RequestParam("tranCode") String tranCode, @RequestParam("tranNo") int tranNo,
			@ModelAttribute("Product") Product product) throws Exception {

		System.out.println("/updateTranCode.do");

		Purchase purchase = purchaseService.getPurchase(tranNo);

		product.setProdNo(purchase.getPurchaseProd().getProdNo());

		purchase.setTranCode(tranCode);
		purchase.setPurchaseProd(product);

		purchaseService.updateTranCode(purchase);

		return "forward:/listPurchase.do?tranNo=" + tranNo;
	}

}