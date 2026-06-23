import { useEffect, useState } from "react";

import MainLayout from "../../layouts/MainLayout";

import {
  getAllOrders,
  updateOrderStatus,
} from "../../services/adminOrderService";

function AdminOrdersPage() {
  const [orders, setOrders] = useState([]);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    try {
      const data = await getAllOrders();

      setOrders(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleStatusUpdate = async (
    orderNumber,
    status
  ) => {
    try {
      await updateOrderStatus(
        orderNumber,
        status
      );

      alert("Order updated");

      loadOrders();
    } catch (error) {
      console.error(error);

      alert("Failed to update order");
    }
  };

  if (loading) {
    return (
      <MainLayout>
        <div className="p-10">
          Loading...
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="max-w-7xl mx-auto p-10">
        <h1 className="text-4xl font-bold mb-8">
          Manage Orders
        </h1>

        <div className="space-y-4">
          {orders.map((order) => (
            <div
              key={order.orderNumber}
              className="border rounded-lg p-5"
            >
              <div className="flex justify-between">
                <div>
                  <h2 className="font-bold text-xl">
                    {order.orderNumber}
                  </h2>

                  <p>
                    Items:{" "}
                    {order.items.length}
                  </p>

                  <p>
                    Total: ₹
                    {order.totalAmount}
                  </p>

                  <p>
                    Status:
                    {" "}
                    {order.status}
                  </p>
                </div>

                <div>
                  <select
                    className="border p-2 rounded"
                    defaultValue=""
                    onChange={(e) =>
                      handleStatusUpdate(
                        order.orderNumber,
                        e.target.value
                      )
                    }
                  >
                    <option value="">
                      Update Status
                    </option>

                    <option value="PROCESSING">
                      PROCESSING
                    </option>

                    <option value="SHIPPED">
                      SHIPPED
                    </option>

                    <option value="DELIVERED">
                      DELIVERED
                    </option>
                  </select>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </MainLayout>
  );
}

export default AdminOrdersPage;