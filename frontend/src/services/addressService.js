import axiosClient from "../api/axiosClient";

export const getAddresses = async () => {
  const response = await axiosClient.get("/addresses");
  return response.data;
};

export const createAddress = async (data) => {
  const response = await axiosClient.post("/addresses", data);

  return response.data;
};

export const updateAddress = async (id, data) => {
  const response = await axiosClient.put(`/addresses/${id}`, data);

  return response.data;
};

export const deleteAddress = async (id) => {
  const response = await axiosClient.delete(`/addresses/${id}`);

  return response.data;
};

export const setDefaultAddress = async (id) => {
  const response = await axiosClient.put(`/addresses/${id}/default`);

  return response.data;
};
