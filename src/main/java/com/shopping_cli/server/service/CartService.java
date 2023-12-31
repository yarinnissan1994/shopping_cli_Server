package com.shopping_cli.server.service;

import com.shopping_cli.server.model.OrderItem;
import com.shopping_cli.server.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private static final String CART_SESSION_ATTRIBUTE = "cart";

    public void addToCart(HttpSession session, OrderItem product) {
        try {
            List<OrderItem> cartProducts = getOrCreateCartSession(session);
            cartProducts.add(product);
            System.out.println("Product added to cart!");
        } catch (Exception e) {
            System.err.println("Error occurred while adding product to cart: " + e.getMessage());
            throw e;
        }
    }

    public void removeFromCart(HttpSession session, int productId) {
        try {
            List<OrderItem> cartProducts = getOrCreateCartSession(session);
            cartProducts.removeIf(product -> product.getId() == productId);
            System.out.println("Product removed from cart: ID " + productId);
        } catch (Exception e) {
            System.err.println("Error occurred while removing product from cart: " + e.getMessage());
            throw e;
        }
    }

    public List<OrderItem> getCart(HttpSession session) {
        return getOrCreateCartSession(session);
    }

    public void clearCart(HttpSession session) {
        try {
            session.removeAttribute(CART_SESSION_ATTRIBUTE);
            System.out.println("Cart cleared");
        } catch (Exception e) {
            System.err.println("Error occurred while clearing cart: " + e.getMessage());
            throw e;
        }
    }

    public void updateCart(HttpSession session, List<OrderItem> cartProducts) {
        try {
            session.setAttribute(CART_SESSION_ATTRIBUTE, cartProducts);
            System.err.println("Cart updated");
        } catch (Exception e) {
            System.err.println("Error occurred while updating cart: " + e.getMessage());
            throw e;
        }
    }

    public void updateCart(HttpSession session, OrderItem product) {
        try {
            List<OrderItem> cartProducts = getOrCreateCartSession(session);
            int index = findProductIndexInCart(cartProducts, product.getId());
            if (index != -1) {
                cartProducts.set(index, product);
                System.out.println("Product updated in cart: ID " + product.getId());
            } else {
                cartProducts.add(product);
                System.err.println("Product added to cart!");
            }
        } catch (Exception e) {
            System.err.println("Error occurred while updating cart with product: " + e.getMessage());
            throw e;
        }
    }

    public double getTotal(HttpSession session) {
        try {
            List<OrderItem> cartProducts = getOrCreateCartSession(session);
            double total = 0;
            for (OrderItem product : cartProducts) {
                total += product.getItemAmount();
            }
            return total;
        } catch (Exception e) {
            System.err.println("Error occurred while calculating total: " + e.getMessage());
            throw e;
        }
    }

    private List<OrderItem> getOrCreateCartSession(HttpSession session) {
        try {
            List<OrderItem> cartProducts = (List<OrderItem>) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cartProducts == null) {
                cartProducts = new ArrayList<>();
                session.setAttribute(CART_SESSION_ATTRIBUTE, cartProducts);
            }
            return cartProducts;
        } catch (Exception e) {
            System.err.println("Error occurred while getting or creating cart session: " + e.getMessage());
            throw e;
        }
    }

    public void replaceListWithUpdatedList(HttpSession session, List<OrderItem> orderItems) {
        try {
            session.setAttribute(CART_SESSION_ATTRIBUTE, orderItems);
        } catch (Exception e) {
            System.err.println("Error occurred while replacing list with updated list: " + e.getMessage());
            throw e;
        }
    }

    private int findProductIndexInCart(List<OrderItem> cartProducts, int productId) {
        try {
            for (int i = 0; i < cartProducts.size(); i++) {
                if (cartProducts.get(i).getId() == productId) {
                    return i;
                }
            }
            return -1;
        } catch (Exception e) {
            System.err.println("Error occurred while finding product index in cart: " + e.getMessage());
            throw e;
        }
    }

}

