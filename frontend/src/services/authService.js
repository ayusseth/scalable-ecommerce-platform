import axiosClient from "../api/axiosClient";

export const registerUser = async (data) => {
  const response =
    await axiosClient.post(
      "/auth/register",
      data
    );

  return response.data;
};

export const loginUser = async (data) => {
  const response =
    await axiosClient.post(
      "/auth/login",
      data
    );

  return response.data;
};

export const verifyOtp = async (data) => {
  const response =
    await axiosClient.post(
      "/auth/verify-otp",
      data
    );

  return response.data;
};

export const sendOtp = async (data) => {
  const response =
    await axiosClient.post(
      "/auth/send-otp",
      data
    );

  return response.data;
};