import { Link } from "react-router-dom";
import { useCart } from "../store/cartStore";

function Navbar() {
  const { cartItems } = useCart();

  const cartCount = cartItems.reduce((total, item) => total + item.quantity, 0);
  return (
    <nav className="bg-slate-900 text-white">
      <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between">
        <Link to="/" className="text-2xl font-bold">
          Ayush Ecommerce
        </Link>

        <div className="flex gap-6">
          <Link to="/">Home</Link>

          <Link to="/products">Products</Link>

          <Link to="/cart">Cart ({cartCount})</Link>

          <Link to="/orders">Orders</Link>

          <Link to="/login">Login</Link>

          <Link to="/register">Register</Link>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
