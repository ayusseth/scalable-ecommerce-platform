import axiosClient from "../api/axiosClient";

export const getCategories = async () => {
  const response = await axiosClient.get(
    "/admin/categories"
  );

  return response.data;
};

export const getCategoryById = async (
  id
) => {
  const response = await axiosClient.get(
    `/admin/categories/${id}`
  );

  return response.data;
};

export const createCategory = async (
  data
) => {
  const response = await axiosClient.post(
    "/admin/categories",
    data
  );

  return response.data;
};

export const updateCategory = async (
  id,
  data
) => {
  const response = await axiosClient.put(
    `/admin/categories/${id}`,
    data
  );

  return response.data;
};

export const deleteCategory = async (
  id
) => {
  await axiosClient.delete(
    `/admin/categories/${id}`
  );
};