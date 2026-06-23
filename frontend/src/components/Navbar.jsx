import { Link, useNavigate } from "react-router-dom";

import { useAuth } from "../store/authStore";
import { useCart } from "../store/cartStore";

import { useState } from "react";

function Navbar() {
  const navigate = useNavigate();

  const [showMenu, setShowMenu] = useState(false);

  const { logout, isAuthenticated, user } = useAuth();

  const { cartItems } = useCart();

  const totalCartItems = cartItems.reduce(
    (total, item) => total + item.quantity,
    0,
  );

  const handleLogout = () => {
    logout();

    navigate("/login");
  };

  return (
    <nav className="bg-slate-900 text-white">
      <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between">
        <Link to="/" className="text-2xl font-bold">
          Ayush Ecommerce
        </Link>

        <div className="flex gap-6 items-center">
          <Link to="/">Home</Link>

          <Link to="/products">Products</Link>

          <Link to="/cart" className="relative">
            Cart
            {totalCartItems > 0 && (
              <span
                className="
        absolute
        -top-2
        -right-4
        bg-red-500
        text-white
        text-xs
        px-2
        py-0.5
        rounded-full
      "
              >
                {totalCartItems}
              </span>
            )}
          </Link>

          <Link to="/orders">Orders</Link>

          {user?.role === "ROLE_ADMIN" && (
            <Link to="/admin/dashboard">Admin Panel</Link>
          )}

          {!isAuthenticated ? (
            <>
              <Link to="/login">Login</Link>

              <Link to="/register">Register</Link>
            </>
          ) : (
            <div className="relative">
              <button
                onClick={() => setShowMenu(!showMenu)}
                className="flex items-center gap-2"
              >
                👤 {user?.name}
              </button>

              {showMenu && (
                <div
                  className="
      absolute
      right-0
      mt-2
      w-48
      bg-white
      text-black
      rounded
      shadow-lg
      z-50
    "
                >
                  <Link
                    to="/profile"
                    className=" block px-4  py-2   hover:bg-gray-100"
                  >
                    My Profile
                  </Link>

                  <Link
                    to="/orders"
                    className="
        block
        px-4
        py-2
        hover:bg-gray-100
      "
                  >
                    My Orders
                  </Link>

                  <Link
                    to="/addresses"
                    className="
        block
        px-4
        py-2
        hover:bg-gray-100
      "
                  >
                    Addresses
                  </Link>

                  {user?.role === "ROLE_ADMIN" && (
                    <>
                      <hr />

                      <Link
                        to="/admin/dashboard"
                        className="
        block
        px-4
        py-2
        hover:bg-gray-100
      "
                      >
                        Admin Dashboard
                      </Link>

                      <Link
                        to="/admin/users"
                        className="
    block
    px-4
    py-2
    hover:bg-gray-100
  "
                      >
                        Admin Users
                      </Link>

                      <Link
                        to="/admin/products"
                        className="
        block
        px-4
        py-2
        hover:bg-gray-100
      "
                      >
                        Manage Products
                      </Link>

                      <Link
                        to="/admin/categories"
                        className="
        block
        px-4
        py-2
        hover:bg-gray-100
      "
                      >
                        Manage Categories
                      </Link>

                      <Link
                        to="/admin/orders"
                        className="
        block
        px-4
        py-2
        hover:bg-gray-100
      "
                      >
                        Manage Orders
                      </Link>
                    </>
                  )}

                  <button
                    onClick={handleLogout}
                    className="
        block
        w-full
        text-left
        px-4
        py-2
        text-red-600
        hover:bg-gray-100
      "
                  >
                    Logout
                  </button>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
