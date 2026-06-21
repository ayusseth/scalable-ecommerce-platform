import MainLayout from "../../layouts/MainLayout";
import { useCart } from "../../store/cartStore";

function CartPage() {

  const { cartItems } = useCart();
  
  console.log("CART ITEMS:", cartItems);

  return (
    <MainLayout>

      <div className="max-w-7xl mx-auto p-8">

        <h1 className="text-4xl font-bold mb-8">
          Shopping Cart
        </h1>

        {cartItems.length === 0 ? (
          <p>Your cart is empty</p>
        ) : (
          <div className="space-y-4">

            {cartItems.map((item, index) => (

              <div
                key={index}
                className="border p-4 rounded"
              >
                <h2 className="font-semibold">
                  {item.name}
                </h2>

                <p>
                  ₹ {item.price}
                </p>

              </div>

            ))}

          </div>
        )}

      </div>

    </MainLayout>
  );
}

export default CartPage;