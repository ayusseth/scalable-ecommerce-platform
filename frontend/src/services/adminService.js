import axiosClient from "../api/axiosClient";

export const getDashboardStats = async () => {
  const response = await axiosClient.get(
    "/admin/dashboard/stats",
  );

  return response.data;
};