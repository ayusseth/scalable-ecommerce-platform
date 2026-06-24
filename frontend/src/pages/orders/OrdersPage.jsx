import { useEffect, useState } from "react";

import MainLayout from "../../layouts/MainLayout";

import { getMyOrders } from "../../services/orderService";

import {
  createRazorpayOrder,
  verifyPayment,
} from "../../services/paymentService";

function OrdersPage() {
  const [orders, setOrders] = useState([]);

  const [loading, setLoading] = useState(true);

  const loadRazorpayScript = () => {
    return new Promise((resolve) => {
      const script = document.createElement("script");

      script.src = "https://checkout.razorpay.com/v1/checkout.js";

      script.onload = () => resolve(true);

      script.onerror = () => resolve(false);

      document.body.appendChild(script);
    });
  };

  const handlePayNow = async (order) => {
    try {
      const loaded = await loadRazorpayScript();

      if (!loaded) {
        alert("Unable to load Razorpay");

        return;
      }

      const paymentOrder = await createRazorpayOrder(order.orderId);

      const options = {
        key: paymentOrder.keyId,

        amount: paymentOrder.amount,

        currency: paymentOrder.currency,

        order_id: paymentOrder.razorpayOrderId,

        name: "Ayush Ecommerce",

        description: order.orderNumber,

        handler: async function (response) {
          await verifyPayment({
            razorpay_order_id: response.razorpay_order_id,

            razorpay_payment_id: response.razorpay_payment_id,

            razorpay_signature: response.razorpay_signature,
          });

          alert("Payment Successful");

          loadOrders();
        },
      };

      const razorpay = new window.Razorpay(options);

      razorpay.open();
    } catch (error) {
      console.error(error);

      alert("Payment failed");
    }
  };

  const loadOrders = async () => {
    try {
      const data = await getMyOrders();

      setOrders(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOrders();
  }, []);

  if (loading) {
    return (
      <MainLayout>
        <div className="p-10">Loading orders...</div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="max-w-6xl mx-auto p-10">
        <h1 className="text-4xl font-bold mb-8">My Orders</h1>

        {orders.length === 0 ? (
          <p>No orders found.</p>
        ) : (
          <div className="space-y-6">
            {orders.map((order) => (
              <div
                key={order.orderId}
                className="
                  border
                  rounded-lg
                  p-6
                  shadow-sm
                "
              >
                <div className="flex justify-between items-center">
                  <div>
                    <h2 className="font-bold text-lg">{order.orderNumber}</h2>

                    <p>Items: {order.items?.length || 0}</p>

                    <p>Total: ₹{order.totalAmount}</p>
                  </div>

                  <div className="flex flex-col items-end gap-3">
                    <span
                      className={`
      px-4
      py-2
      rounded-full
      text-sm
      font-semibold
      ${
        order.status === "PAID"
          ? "bg-green-100 text-green-700"
          : "bg-yellow-100 text-yellow-700"
      }
    `}
                    >
                      {order.status}
                    </span>

                    {order.status === "PENDING" && (
                      <button
                        onClick={() => handlePayNow(order)}
                        className="
        bg-blue-600
        text-white
        px-4
        py-2
        rounded
      "
                      >
                        Pay Now
                      </button>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </MainLayout>
  );
}

export default OrdersPage;
