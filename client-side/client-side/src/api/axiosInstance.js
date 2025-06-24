import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'https://api.laragrooming.com',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor – Attach token
axiosInstance.interceptors.request.use(
  (config) => {
    const publicEndpoints = ['/oauth2/login', '/oauth2/loginUser', '/oauth2/registerUser'];
    const isPublic = publicEndpoints.some(endpoint => config.url.includes(endpoint));

    if (!isPublic) {
      const token = localStorage.getItem('token');
      console.log("Token from local storage :", token)
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
      }
    }

    return config;
  },
  (error) => Promise.reject(error)
);


// Response Interceptor – Handle 401
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      console.error('Unauthorized. Redirecting to login...');

      localStorage.removeItem('token');
      localStorage.removeItem('user');
      
      window.location.href = '/'; 
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
