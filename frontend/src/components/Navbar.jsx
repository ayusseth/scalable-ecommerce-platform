import { Link, useNavigate } from "react-router-dom";

import { useAuth } from "../store/authStore";
import { useCart } from "../store/cartStore";

function Navbar() {
  const navigate = useNavigate();

  const { logout, isAuthenticated } = useAuth();

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

          {!isAuthenticated ? (
            <>
              <Link to="/login">Login</Link>

              <Link to="/register">Register</Link>
            </>
          ) : (
            <button onClick={handleLogout}>Logout</button>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
