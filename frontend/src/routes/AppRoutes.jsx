import { BrowserRouter, Routes, Route } from "react-router-dom";

import HomePage from "../pages/home/HomePage";
import LoginPage from "../pages/auth/LoginPage";
import RegisterPage from "../pages/auth/RegisterPage";
import ProductsPage from "../pages/products/ProductsPage";
import CartPage from "../pages/cart/CartPage";
import OrdersPage from "../pages/orders/OrdersPage";
import VerifyOtpPage from "../pages/auth/VerifyOtpPage";

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />

        <Route path="/login" element={<LoginPage />} />

        <Route path="/register" element={<RegisterPage />} />

        <Route path="/products" element={<ProductsPage />} />

        <Route path="/cart" element={<CartPage />} />

        <Route path="/orders" element={<OrdersPage />} />

        <Route path="/verify-otp" element={<VerifyOtpPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;
