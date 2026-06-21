import { useCart } from "../store/cartStore";

function ProductCard({ product }) {

  const { addToCart } = useCart();

  return (
    <div className="border rounded-lg p-4 shadow hover:shadow-lg transition">

      <h2 className="text-xl font-semibold">
        {product.name}
      </h2>

      <p className="mt-2 text-gray-600">
        {product.description}
      </p>

      <p className="mt-4 text-2xl font-bold text-green-600">
        ₹ {product.price}
      </p>

      <p className="mt-2">
        Stock: {product.stockQuantity}
      </p>

      <button
        onClick={() => addToCart(product)}
        className="mt-4 w-full bg-blue-600 text-white py-2 rounded"
      >
        Add To Cart
      </button>

    </div>
  );
}

export default ProductCard;