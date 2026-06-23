import { useEffect, useState } from "react";

import MainLayout from "../../layouts/MainLayout";

import { getDashboardStats } from "../../services/adminService";

function AdminDashboardPage() {
  const [stats, setStats] = useState(null);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const data = await getDashboardStats();

      setStats(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
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
          Admin Dashboard
        </h1>

        <div className="grid grid-cols-3 gap-6">
          <div className="border p-6 rounded">
            <h3>Total Users</h3>
            <p className="text-3xl font-bold">
              {stats.totalUsers}
            </p>
          </div>

          <div className="border p-6 rounded">
            <h3>Total Products</h3>
            <p className="text-3xl font-bold">
              {stats.totalProducts}
            </p>
          </div>

          <div className="border p-6 rounded">
            <h3>Total Orders</h3>
            <p className="text-3xl font-bold">
              {stats.totalOrders}
            </p>
          </div>

          <div className="border p-6 rounded">
            <h3>Revenue</h3>
            <p className="text-3xl font-bold">
              ₹{stats.totalRevenue}
            </p>
          </div>

          <div className="border p-6 rounded">
            <h3>Pending Orders</h3>
            <p className="text-3xl font-bold">
              {stats.pendingOrders}
            </p>
          </div>

          <div className="border p-6 rounded">
            <h3>Low Stock Products</h3>
            <p className="text-3xl font-bold">
              {stats.lowStockProducts}
            </p>
          </div>
        </div>
      </div>
    </MainLayout>
  );
}

export default AdminDashboardPage;