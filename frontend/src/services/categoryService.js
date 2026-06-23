import axiosClient from "../api/axiosClient";

export const getCategories = async () => {
  const response = await axiosClient.get(
    "/admin/categories"
  );

  return response.data;
};