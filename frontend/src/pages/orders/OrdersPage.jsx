import { useEffect, useState } from "react";

import MainLayout from "../../layouts/MainLayout";

import { getMyOrders } from "../../services/orderService";

function OrdersPage() {
  const [orders, setOrders] = useState([]);

  const [loading, setLoading] = useState(true);

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
        <div className="p-10">
          Loading orders...
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="max-w-6xl mx-auto p-10">

        <h1 className="text-4xl font-bold mb-8">
          My Orders
        </h1>

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
                    <h2 className="font-bold text-lg">
                      {order.orderNumber}
                    </h2>

                    <p>
                      Items:
                      {" "}
                      {order.items?.length || 0}
                    </p>

                    <p>
                      Total:
                      {" "}
                      ₹
                      {order.totalAmount}
                    </p>
                  </div>

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