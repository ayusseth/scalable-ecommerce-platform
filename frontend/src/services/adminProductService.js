import axiosClient from "../api/axiosClient";

export const getProducts = async () => {
  const response = await axiosClient.get(
    "/products"
  );

  return response.data.content;
};

export const createProduct = async (data) => {
  const response = await axiosClient.post(
    "/admin/products",
    data
  );

  return response.data;
};

export const updateProduct = async (
  productId,
  data
) => {
  const response = await axiosClient.put(
    `/admin/products/${productId}`,
    data
  );

  return response.data;
};

export const getProductById = async (
  productId
) => {

  const response =
    await axiosClient.get(
      `/products/${productId}`
    );

  return response.data;
};

export const deleteProduct = async (
  productId
) => {
  const response = await axiosClient.delete(
    `/admin/products/${productId}`
  );

  return response.data;
};

export const getLowStockProducts =
  async () => {
    const response = await axiosClient.get(
      "/admin/products/low-stock"
    );

    return response.data;
  };