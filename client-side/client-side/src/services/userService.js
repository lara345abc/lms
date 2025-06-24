import axiosInstance from "../api/axiosInstance";

const userService = {
  // Get user by ID
  getUserById: async (id) => {
    const response = await axiosInstance.get(`/user/getUserById`, {
      params: { id },
    });
    console.log("axios instance ", axiosInstance)
    return response.data;
  },

  getUserWithPackages: async (id) => {
    const response = await axiosInstance.get(`/user/getUserWithPackages`, {
      params: { id },
    });
    console.log("axios instance ", axiosInstance)
    return response.data;
  },

  getUserByEmail: async () => {
    const response = await axiosInstance.get(`/user/getUserByEmail`);
    console.log("axios instance ", )
    return response.data;
  },

  // Get paginated users
  getPaginatedUsers: async (page = 0, size = 20) => {
    console.log("inside paginaed users api request ")
    const response = await axiosInstance.get(`/user/getPaginatedUsers`, {
      params: { page, size },
    });
    console.log("axios instance ")
    return response.data;
  },

  // Verify user with token
  verifyUser: async (token) => {
    const response = await axiosInstance.get(`/user/verify`, {
      params: { token },
    });
    return response.data;
  },

  // Resend verification email
  resendVerificationEmail: async (email) => {
    const response = await axiosInstance.post(`/user/resend-verification`, null, {
      params: { email },
    });
    return response.data;
  },
};

export default userService;
