package com.OnlineShop.store.web;

import com.OnlineShop.store.domain.Customer;
import com.OnlineShop.store.domain.Goods;
import com.OnlineShop.store.domain.Merchants;
import com.OnlineShop.store.service.CustomerService;
import com.OnlineShop.store.service.MerchantsService;
import com.OnlineShop.store.service.GoodsService;
import com.OnlineShop.store.service.OrdersService;
import com.OnlineShop.store.service.ServiceException;
import com.OnlineShop.store.service.imp.CustomerServiceImp;
import com.OnlineShop.store.service.imp.MerchantsServiceImp;
import com.OnlineShop.store.service.imp.GoodsServiceImp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

//@javax.servlet.annotation.WebServlet(name = "Controller", urlPatterns = {"/controller"})
public class Controller extends javax.servlet.http.HttpServlet {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private CustomerService customerService = new CustomerServiceImp();
    private MerchantsService merchantsService = new MerchantsServiceImp();
    private GoodsService goodsService = new GoodsServiceImp();
    private OrdersService ordersService = new OrdersServiceImp();

    private int totalPageNumber = 0; // 总页数
    private int pageSize = 10; // 每页行数
    private int currentPage = 1; // 当前页数
    private String modify = new String();


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pageSize = new Integer(config.getInitParameter("pageSize"));
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        // 客户端提交的操作标志
        String action = request.getParameter("action");
        System.out.println(action);
        if ("reg".equals(action)) {
            // -----------客户注册------------
            String userid = request.getParameter("userid");
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String password2 = request.getParameter("password2");
            String birthday = request.getParameter("birthday");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");

            // 服务器端验证
            List<String> errors = new ArrayList<>();
            if (userid == null || userid.equals("")) {
                errors.add("客户账号不能为空！");
            }
            if (name == null || name.equals("")) {
                errors.add("客户姓名不能为空！");
            }
            if (password == null
                    || password2 == null
                    || !password.equals(password2)) {
                errors.add("两次输入的密码不一致！");
            }

            String pattern = "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((0[1,3-9])|(1[0-2]))-(29|30)))))$";
            if (!Pattern.matches(pattern, birthday)) {
                errors.add("出生日期格式无效！");
            }

            if (errors.size() > 0) { // 验证失败
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("customer_reg.jsp").forward(request, response);
            } else { // 验证成功
                Customer customer = new Customer();
                customer.setId(userid);
                customer.setName(name);
                customer.setPassword(password);
                customer.setAddress(address);
                customer.setPhone(phone);
                try {
                    Date d = dateFormat.parse(birthday);
                    customer.setBirthday(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 注册
                try {
                    // 注册成功
                    customerService.register(customer);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                } catch (ServiceException e) {
                    // 客户ID已经注册
                    errors.add("客户ID已经注册！");
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher("customer_reg.jsp").forward(request, response);
                }

            }
        }else if("mer_reg".equals(action)){
            // -----------商家注册------------
            String userid = request.getParameter("userid");
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String password2 = request.getParameter("password2");
            String birthday = request.getParameter("birthday");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");

            // 服务器端验证
            List<String> errors = new ArrayList<>();
            if (userid == null || userid.equals("")) {
                errors.add("商家账号不能为空！");
            }
            if (name == null || name.equals("")) {
                errors.add("商家名不能为空！");
            }
            if (password == null
                    || password2 == null
                    || !password.equals(password2)) {
                errors.add("两次输入的密码不一致！");
            }

            String pattern = "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((0[1,3-9])|(1[0-2]))-(29|30)))))$";
            if (!Pattern.matches(pattern, birthday)) {
                errors.add("出生日期格式无效！");
            }

            if (errors.size() > 0) { // 验证失败
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("mer_reg.jsp").forward(request, response);
            } else { // 验证成功
                Merchants merchants = new Merchants();
                merchants.setId(userid);
                merchants.setName(name);
                merchants.setPassword(password);
                merchants.setAddress(address);
                merchants.setPhone(phone);
                try {
                    Date d = dateFormat.parse(birthday);
                    merchants.setBirthday(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 注册
                try {
                    // 注册成功
                    merchantsService.register(merchants);
                    request.getRequestDispatcher("mer_login.jsp").forward(request, response);
                } catch (ServiceException e) {
                    // 商家ID已经注册
                    errors.add("商家ID已经注册！");
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher("mer_reg.jsp").forward(request, response);
                }

            }
        }else if ("login".equals(action)) {
            //------------客户登录--------------
            String userid = request.getParameter("userid");
            String password = request.getParameter("password");

            Customer customer = new Customer();
            customer.setId(userid);
            customer.setPassword(password);

            if (customerService.login(customer)) { // 登录成功
                HttpSession session = request.getSession();
                session.setAttribute("customer", customer);
                request.getRequestDispatcher("main.jsp").forward(request, response);
            } else { // 登录失败
                List<String> errors = new ArrayList<>();
                errors.add("您输入的客户账号或密码不正确！");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } else if ("login_m".equals(action)) {
            //------------商家登录--------------
            String userid = request.getParameter("userid");
            String password = request.getParameter("password");

            Merchants merchants = new Merchants();
            merchants.setId(userid);
            merchants.setPassword(password);

            if (merchantsService.login(merchants)) { // 登录成功
                HttpSession session = request.getSession();
                session.setAttribute("merchants", merchants);
                request.getRequestDispatcher("main_m.jsp").forward(request, response);
            } else { // 登录失败
                List<String> errors = new ArrayList<>();
                errors.add("您输入的商家账号或密码不正确！");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("mer_login.jsp").forward(request, response);
            }

        }else if ("list".equals(action)) {
            //------------商品列表--------------
            List<Goods> goodsList = goodsService.queryAll();

            if (goodsList.size() % pageSize == 0) {
                totalPageNumber = goodsList.size() / pageSize;
            } else {
                totalPageNumber = goodsList.size() / pageSize + 1;
            }

            request.setAttribute("totalPageNumber", totalPageNumber);
            request.setAttribute("currentPage", currentPage);

            int start = (currentPage - 1) * pageSize;
            int end = currentPage * pageSize;
            if (currentPage == totalPageNumber) { // 最后一页
                end = goodsList.size();
            }

            Merchants merchants = (Merchants)request.getSession().getAttribute("merchants") ;
            Customer customer = (Customer)request.getSession().getAttribute("customer");
            if(customer!=null){
                request.setAttribute("goodsList", goodsList.subList(start, end));
                request.getRequestDispatcher("goods_list.jsp").forward(request, response);
            }else if(merchants!=null){
                request.setAttribute("goodsList", goodsList.subList(start, end));
                request.getRequestDispatcher("goods_list_m.jsp").forward(request, response);
            }

        } else if ("paging".equals(action)) {
            //------------商品列表分页--------------
            String page = request.getParameter("page");

            if (page.equals("prev")) { //上一页
                currentPage--;
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } else if (page.equals("next")) {// 下一页
                currentPage++;
                if (currentPage > totalPageNumber) {
                    currentPage = totalPageNumber;
                }
            } else {
                currentPage = Integer.valueOf(page);
            }
            int start = (currentPage - 1) * pageSize;
            int end = currentPage * pageSize;

            List<Goods> goodsList = goodsService.queryByStartEnd(start, end);

            request.setAttribute("totalPageNumber", totalPageNumber);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("goodsList", goodsList);
            Merchants merchants = (Merchants)request.getSession().getAttribute("merchants");
            if(merchants ==null) {
                request.getRequestDispatcher("goods_list.jsp").forward(request, response);
            }else request.getRequestDispatcher("goods_list_m.jsp").forward(request, response);
        } else if ("detail".equals(action)) {
            //----------查看商品详细------------
            String goodsid = request.getParameter("id");
            Goods goods = goodsService.querDetail(new Long(goodsid));

            request.setAttribute("goods", goods);
            request.getRequestDispatcher("goods_detail.jsp").forward(request, response);

        } else if ("add".equals(action)) {
            //----------添加购物车------------
            Long goodsid = new Long(request.getParameter("id"));
            String goodsname = request.getParameter("name");
            Float price = new Float(request.getParameter("price"));

            // 购物车结构是List中包含Map，每一个Map是一个商品
            // 从Session中取出的购物车
            List<Map<String, Object>> cart = (List<Map<String, Object>>) request.getSession().getAttribute("cart");

            if (cart == null) { // 第一次取出是null
                cart = new ArrayList<Map<String, Object>>();
                request.getSession().setAttribute("cart", cart);
            }

            // 购物车中有选择的商品
            int flag = 0;
            for (Map<String, Object> item : cart) {
                Long goodsid2 = (Long) item.get("goodsid");
                if (goodsid.equals(goodsid2)) {

                    Integer quantity = (Integer) item.get("quantity");
                    quantity++;
                    item.put("quantity", quantity);

                    flag++;
                }
            }

            // 购物车中没有选择的商品
            if (flag == 0) {
                Map<String, Object> item = new HashMap<>();
                // item 结构是Map [商品id，商品名称，价格，数量]
                item.put("goodsid", goodsid);
                item.put("goodsname", goodsname);
                item.put("quantity", 1);
                item.put("price", price);
                // 添加到购物车
                cart.add(item);
            }

            System.out.println(cart);

            String pagename = request.getParameter("pagename");

            if (pagename.equals("list")) {
                int start = (currentPage - 1) * pageSize;
                int end = currentPage * pageSize;

                List<Goods> goodsList = goodsService.queryByStartEnd(start, end);

                request.setAttribute("totalPageNumber", totalPageNumber);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("goodsList", goodsList);
                request.getRequestDispatcher("goods_list.jsp").forward(request, response);

            } else if (pagename.equals("detail")) {

                Goods goods = goodsService.querDetail(new Long(goodsid));
                request.setAttribute("goods", goods);
                request.getRequestDispatcher("goods_detail.jsp").forward(request, response);
            }
        } else if ("cart".equals(action)) {
            //---------查看购物车---------
            // 从Session中取出的购物车
            List<Map<String, Object>> cart = (List<Map<String, Object>>) request.getSession().getAttribute("cart");

            double total = 0.0;

            if (cart != null) {
                for (Map<String, Object> item : cart) {

                    Integer quantity = (Integer) item.get("quantity");
                    Float price = (Float) item.get("price");
                    double subtotal = price * quantity;
                    total += subtotal;
                }
            }

            request.setAttribute("total", total);
            request.getRequestDispatcher("cart.jsp").forward(request, response);
        } else if ("sub_ord".equals(action)) {
            //------------提交订单-----------
            // 从Session中取出的购物车
            List<Map<String, Object>> cart = (List<Map<String, Object>>) request.getSession().getAttribute("cart");
            for (Map<String, Object> item : cart) {
                Long goodsid = (Long) item.get("goodsid");
                String strquantity = request.getParameter("quantity_" + goodsid);
                int quantity = 0;
                try {
                    quantity = new Integer(strquantity);
                } catch (Exception e) {
                }

                item.put("quantity", quantity);
            }

            // 提交订单
            String ordersid = ordersService.submitOrders(cart);
            request.setAttribute("ordersid", ordersid);
            request.getRequestDispatcher("order_finish.jsp").forward(request, response);
            // 清空购物车
            request.getSession().removeAttribute("cart");
        } else if ("main".equals(action)) {
            //--------------进入主页面--------------------
            request.getRequestDispatcher("main.jsp").forward(request, response);
        }else if ("logout".equals(action)) {
            //-----------注销-----------------------
            // 清空购物车
            request.getSession().removeAttribute("cart");
            // 清除登录信息
            request.getSession().removeAttribute("customer");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }else if ("reg_init".equals(action)) {
            // -----------客户注册页面进入------------
            request.getRequestDispatcher("customer_reg.jsp").forward(request, response);
        }else if ("mer_login".equals(action)){
            //------------商家页面进入----------------
            request.getRequestDispatcher("mer_login.jsp").forward(request, response);
        }else if ("mer_init".equals(action)) {
            // -----------商家注册页面进入------------
            request.getRequestDispatcher("mer_reg.jsp").forward(request, response);
        }else if ("modify".equals(action)) {
            // 商家添加商品信息
            modify = "create";
            request.setAttribute("modify",modify);
            request.getRequestDispatcher("modify.jsp").forward(request, response);
        }else if("create".equals(action)){
            //形成一个新的商品
            String name = request.getParameter("name");
            String price = request.getParameter("price");
            String description = request.getParameter("description");
            String brand = request.getParameter("brand");
            String cpu_brand = request.getParameter("cpu_brand");
            String cpu_type = request.getParameter("cpu_type");
            String memory_capacity = request.getParameter("memory_capacity");
            String hd_capacity = request.getParameter("hd_capacity");
            String card_model = request.getParameter("card_model");
            String displaysize = request.getParameter("displaysize");
            String storage = request.getParameter("storage");


            Goods goods = new Goods();
            goods.setName(name);
            goods.setPrice(Integer.parseInt(price));
            goods.setDescription(description);
            goods.setBrand(brand);
            goods.setCardModel(card_model);
            goods.setCpuBrand(cpu_brand);
            goods.setCpuType(cpu_type);
            goods.setDisplaysize(displaysize);
            goods.setMemoryCapacity(memory_capacity);
            goods.setHdCapacity(hd_capacity);
            goods.setStorage(Integer.parseInt(storage));

            goodsService.create(goods);

            request.getRequestDispatcher("main_m.jsp").forward(request, response);
        }else if ("change_goods".equals(action)) {
            modify="update";
            request.setAttribute("modify",modify);
            //----------查看商品详细------------
            String goodsid = request.getParameter("id");
            Goods goods = goodsService.querDetail(new Long(goodsid));

            request.setAttribute("goods", goods);
            request.getRequestDispatcher("modify.jsp").forward(request, response);
        }else if ("update".equals(action)){
            String goodsid = request.getParameter("id");

            String name = request.getParameter("name");
            String price = request.getParameter("price");
            String description = request.getParameter("description");
            String brand = request.getParameter("brand");
            String cpu_brand = request.getParameter("cpu_brand");
            String cpu_type = request.getParameter("cpu_type");
            String memory_capacity = request.getParameter("memory_capacity");
            String hd_capacity = request.getParameter("hd_capacity");
            String card_model = request.getParameter("card_model");
            String displaysize = request.getParameter("displaysize");
            String storage = request.getParameter("storage");
            String image = request.getParameter("image");

            System.out.println(name);
            System.out.println(price);
            System.out.println(description);
            System.out.println(brand);
            System.out.println(cpu_brand);
            System.out.println(cpu_type);
            System.out.println(memory_capacity);
            System.out.println(hd_capacity);
            System.out.println(card_model);
            System.out.println(image);
            System.out.println(displaysize);
            System.out.println(storage);


            Goods goods = new Goods();
            goods.setId(new Long(goodsid));
            goods.setName(name);
            goods.setPrice(Integer.parseInt(price));
            goods.setDescription(description);
            goods.setBrand(brand);
            goods.setCardModel(card_model);
            goods.setCpuBrand(cpu_brand);
            goods.setCpuType(cpu_type);
            goods.setDisplaysize(displaysize);
            goods.setMemoryCapacity(memory_capacity);
            goods.setHdCapacity(hd_capacity);
            goods.setStorage(Integer.parseInt(storage));
            goods.setImage(image);
            goodsService.update(goods);
            request.getRequestDispatcher("main_m.jsp").forward(request, response);
        }

    }

}
