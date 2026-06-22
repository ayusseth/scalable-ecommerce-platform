import axiosClient from "../api/axiosClient";

export const createOrder = async (data) => {
  const response = await axiosClient.post("/orders", data);

  return response.data;
};

export const getMyOrders = async () => {
  const response = await axiosClient.get(
    "/orders/my-orders",
  );

  return response.data;
};
