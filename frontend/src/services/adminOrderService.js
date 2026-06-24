import axiosClient from "../api/axiosClient";

export const getAllOrders = async () => {
  const response = await axiosClient.get("/admin/orders");

  return response.data;
};

export const updateOrderStatus = async (orderNumber, status) => {
  const response = await axiosClient.put(
    `/admin/orders/${orderNumber}/status`,
    {
      status,
    },
  );

  return response.data;
};

export const getOrdersByStatus = async (status) => {
  const response = await axiosClient.get(
    `/admin/orders/status?status=${status}`,
  );

  return response.data;
};

export const searchOrders = async (keyword) => {
  const response = await axiosClient.get(
    `/admin/orders/search?keyword=${keyword}`,
  );

  return response.data;
};
