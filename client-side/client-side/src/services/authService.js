import axiosInstance from '../api/axiosInstance';

const authService = {
  /**
   * Login using Google OAuth2 ID Token
   * @param {string} idToken - Google ID Token
   * @returns {Promise<ApiResponse<LoginResponseDTO>>}
   */
  loginWithGoogle: async (idToken) => {
    const response = await axiosInstance.post('/oauth2/login', { idToken });
    return response.data;
  },

  /**
   * Register a new user
   * @param {UserRegisterDTO} userData - User registration data
   * @returns {Promise<ApiResponse<UserResponseDTO>>}
   */
  registerUser: async (userData) => {
    const response = await axiosInstance.post('/oauth2/registerUser', userData);
    return response.data;
  },

  /**
   * Login user with email and password
   * @param {LoginRequestDTO} loginData - User login credentials
   * @returns {Promise<ApiResponse<LoginResponseDTO>>}
   */
  loginUser: async (loginData) => {
    const response = await axiosInstance.post('/oauth2/loginUser', loginData);
    return response.data;
  },

  /**
   * Basic test call
   * @returns {Promise<string>}
   */
  sayHello: async () => {
    const response = await axiosInstance.get('/oauth2/hello');
    return response.data;
  }
};

export default authService;
