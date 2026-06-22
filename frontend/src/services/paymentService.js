import axiosClient from "../api/axiosClient";

export const createRazorpayOrder = async (orderId) => {
  const response = await axiosClient.post("/payments/razorpay/create-order", {
    orderId,
  });

  return response.data;
};

export const verifyPayment = async (data) => {
  const response = await axiosClient.post("/payments/razorpay/verify", data);

  return response.data;
};
