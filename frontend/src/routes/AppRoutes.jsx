import { BrowserRouter, Routes, Route } from "react-router-dom";

import HomePage from "../pages/home/HomePage";
import LoginPage from "../pages/auth/LoginPage";
import RegisterPage from "../pages/auth/RegisterPage";
import ProductsPage from "../pages/products/ProductsPage";
import CartPage from "../pages/cart/CartPage";
import OrdersPage from "../pages/orders/OrdersPage";
import VerifyOtpPage from "../pages/auth/VerifyOtpPage";
import ProtectedRoute from "../components/common/ProtectedRoute";
import ProfilePage from "../pages/profile/ProfilePage";
import AddressPage from "../pages/address/AddressPage";
import CreateAddressPage from "../pages/address/CreateAddressPage";
import CheckoutPage from "../pages/checkout/CheckoutPage";
import AdminDashboardPage from "../pages/admin/AdminDashboardPage";
import AdminOrdersPage from "../pages/admin/AdminOrdersPage";
import AdminProductsPage from "../pages/admin/AdminProductsPage";
import CreateProductPage from "../pages/admin/CreateProductPage";
import EditProductPage from "../pages/admin/EditProductPage";

import CreateCategoryPage from "../pages/admin/CreateCategoryPage";
import EditCategoryPage from "../pages/admin/EditCategoryPage";

import AdminCategoriesPage from "../pages/admin/AdminCategoriesPage";

import AdminUsersPage from "../pages/admin/AdminUsersPage";

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />

        <Route path="/login" element={<LoginPage />} />

        <Route path="/register" element={<RegisterPage />} />

        <Route path="/products" element={<ProductsPage />} />

        <Route path="/cart" element={<CartPage />} />

        <Route
          path="/admin/products/:id/edit"
          element={
            <ProtectedRoute>
              <EditProductPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/users"
          element={
            <ProtectedRoute>
              <AdminUsersPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/categories/new"
          element={
            <ProtectedRoute>
              <CreateCategoryPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/categories/:id/edit"
          element={
            <ProtectedRoute>
              <EditCategoryPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/categories"
          element={
            <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
              <AdminCategoriesPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/products"
          element={
            <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
              <AdminProductsPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/orders"
          element={
            <ProtectedRoute>
              <OrdersPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/products/new"
          element={
            <ProtectedRoute>
              <CreateProductPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/addresses/new"
          element={
            <ProtectedRoute>
              <CreateAddressPage />
            </ProtectedRoute>
          }
        />
        <Route path="/verify-otp" element={<VerifyOtpPage />} />

        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <ProfilePage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/addresses"
          element={
            <ProtectedRoute>
              <AddressPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/checkout"
          element={
            <ProtectedRoute>
              <CheckoutPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
              <AdminDashboardPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/orders"
          element={
            <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
              <AdminOrdersPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;
