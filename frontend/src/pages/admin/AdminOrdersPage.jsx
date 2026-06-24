import { useEffect, useState } from "react";

import MainLayout from "../../layouts/MainLayout";

import {
  getAllOrders,
  updateOrderStatus,
  getOrdersByStatus,
} from "../../services/adminOrderService";

import { searchOrders } from "../../services/adminOrderService";

function AdminOrdersPage() {
  const [orders, setOrders] = useState([]);

  const [loading, setLoading] = useState(true);

  const [keyword, setKeyword] = useState("");

  const [statusFilter, setStatusFilter] = useState("ALL");

  const handleStatusFilter = async (status) => {
    setStatusFilter(status);

    try {
      if (status === "ALL") {
        loadOrders();

        return;
      }

      const data = await getOrdersByStatus(status);

      setOrders(data);
    } catch (error) {
      console.error(error);
    }
  };

  const handleSearch = async (value) => {
    setKeyword(value);

    try {
      if (!value.trim()) {
        if (statusFilter === "ALL") {
          loadOrders();
        } else {
          const data = await getOrdersByStatus(statusFilter);

          setOrders(data);
        }

        return;
      }

      const data = await searchOrders(value);

      setOrders(data);
    } catch (error) {
      console.error(error);
    }
  };

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

  const handleStatusUpdate = async (orderNumber, status) => {
    try {
      await updateOrderStatus(orderNumber, status);

      alert("Order updated");

      if (statusFilter === "ALL") {
        loadOrders();
      } else {
        const data = await getOrdersByStatus(statusFilter);

        setOrders(data);
      }
    } catch (error) {
      console.error(error);

      alert("Failed to update order");
    }
  };

  if (loading) {
    return (
      <MainLayout>
        <div className="p-10">Loading...</div>
      </MainLayout>
    );
  }

  return (
  <MainLayout>
    <div className="max-w-7xl mx-auto p-10">

<h1 className="text-4xl font-bold mb-8">
  Manage Orders
</h1>

<div className="flex gap-4 mb-6">

  <input
    type="text"
    placeholder="Search Order, Customer or Email"
    value={keyword}
    onChange={(e) => handleSearch(e.target.value)}
    className="
      border
      p-3
      rounded
      flex-1
    "
  />

  <select
    value={statusFilter}
    onChange={(e) => handleStatusFilter(e.target.value)}
    className="
      border
      p-3
      rounded
      w-64
    "
  >
    <option value="ALL">
      All Orders
    </option>

    <option value="PENDING">
      Pending
    </option>

    <option value="PAID">
      Paid
    </option>

    <option value="PROCESSING">
      Processing
    </option>

    <option value="SHIPPED">
      Shipped
    </option>

    <option value="DELIVERED">
      Delivered
    </option>

    <option value="CANCELLED">
      Cancelled
    </option>
  </select>

</div>

        <div className="space-y-4">
          {orders.map((order) => (
            <div key={order.orderNumber} className="border rounded-lg p-5">
              <div className="flex justify-between">
                <div>
                  <h2 className="font-bold text-xl">{order.orderNumber}</h2>

                  <p>Items: {order.items.length}</p>

                  <p>Total: ₹{order.totalAmount}</p>

                  <p>Status: {order.status}</p>
                </div>

                <div>
                  <p>Customer: {order.customerName}</p>

                  <p>Email: {order.customerEmail}</p>

                  <p>Date: {new Date(order.createdAt).toLocaleString()}</p>

                  <p>Total: ₹{order.totalAmount}</p>

                  <p>Status: {order.status}</p>

                  <div className="mt-4">
                    <h3 className="font-semibold mb-2">Ordered Products</h3>

                    {order.items.map((item) => (
                      <div key={item.productId} className="text-sm mb-2">
                        <p>{item.productName}</p>

                        <p>Qty: {item.quantity}</p>

                        <p>Unit Price: ₹{item.unitPrice}</p>

                        <p>Subtotal: ₹{item.subtotal}</p>
                      </div>
                    ))}
                  </div>
                </div>

                <div>
                  <select
                    className="border p-2 rounded"
                    defaultValue=""
                    onChange={(e) =>
                      handleStatusUpdate(order.orderNumber, e.target.value)
                    }
                  >
                    <option value="">Update Status</option>

                    <option value="PROCESSING">PROCESSING</option>

                    <option value="SHIPPED">SHIPPED</option>

                    <option value="DELIVERED">DELIVERED</option>
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
