import axiosClient from "../api/axiosClient";

export const getDashboardStats = async () => {
  const response = await axiosClient.get(
    "/admin/dashboard/stats",
  );

  return response.data;
};

export const getUsers = async () => {
  const response = await axiosClient.get(
    "/admin/users"
  );

  return response.data;
};

export const searchUsers = async (keyword) => {
  const response = await axiosClient.get(
    `/admin/users/search?keyword=${keyword}`
  );

  return response.data;
};

export const enableUser = async (userId) => {
  await axiosClient.put(
    `/admin/users/${userId}/enable`
  );
};

export const disableUser = async (userId) => {
  await axiosClient.put(
    `/admin/users/${userId}/disable`
  );
};