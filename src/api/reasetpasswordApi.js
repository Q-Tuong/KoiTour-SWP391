import axios from "axios";

const BASE_URL = "https://641855f875be53f451dca0ae.mockapi.io/api/users"; // Thay đổi base URL tương ứng

export const reasetpasswordApi = {
  updatePassword: async (id, newPassword) => {
    try {
      const response = await axios.put(`${BASE_URL}/${id}`, {
        password: newPassword,
      });
      return response;
    } catch (error) {
      throw new Error(error.response.data.message);
    }
  },
};
