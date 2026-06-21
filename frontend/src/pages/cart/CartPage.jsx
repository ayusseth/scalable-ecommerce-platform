import MainLayout from "../../layouts/MainLayout";
import { useCart } from "../../store/cartStore";

function CartPage() {
  const { cartItems, increaseQuantity, decreaseQuantity, removeFromCart } =
    useCart();

  const totalAmount = cartItems.reduce(
    (total, item) => total + item.product.price * item.quantity,
    0,
  );

  console.log("CART ITEMS:", cartItems);

  return (
    <MainLayout>
      <div className="max-w-7xl mx-auto p-8">
        <h1 className="text-4xl font-bold mb-8">Shopping Cart</h1>

        {cartItems.length === 0 ? (
          <p>Your cart is empty</p>
        ) : (
          <div className="space-y-4">
            {cartItems.map((item, index) => (
              <div key={index} className="border p-4 rounded">
                <h2 className="font-semibold">{item.product.name}</h2>

                <p>₹ {item.product.price}</p>

                <div className="flex items-center gap-3 mt-2">
                  <button
                    onClick={() => decreaseQuantity(item.product.id)}
                    className="px-3 py-1 bg-gray-300 rounded"
                  >
                    -
                  </button>

                  <span>{item.quantity}</span>

                  <button
                    onClick={() => increaseQuantity(item.product.id)}
                    className="px-3 py-1 bg-gray-300 rounded"
                  >
                    +
                  </button>

                  <button
                    onClick={() => removeFromCart(item.product.id)}
                    className="bg-red-600 text-white px-4 py-2 rounded"
                  >
                    Remove
                  </button>
                </div>
              </div>
            ))}

            <div className="border-t pt-6">
              <h2 className="text-3xl font-bold">
                Total: ₹ {totalAmount.toFixed(2)}
              </h2>
            </div>
          </div>
        )}
      </div>
    </MainLayout>
  );
}

export default CartPage;
