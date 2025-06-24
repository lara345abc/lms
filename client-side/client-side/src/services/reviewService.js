import axiosInstance from "../api/axiosInstance";

const reviewService = {
  createReview: async ({ targetType, targetId, presentationRating, reviewText }) => {
    const response = await axiosInstance.post(`/reviews`, {
      targetType,
      targetId,
      presentationRating,
      reviewText,
      rating: 5 
    });
    return response.data;
  },

  getReviewSummary: async (targetType, targetId) => {
    const response = await axiosInstance.get(`/reviews/summary`, {
      params: { targetType, targetId },
    });
    return response.data;
  },

  getReviewsByTarget: async (targetType, targetId) => {
  const response = await axiosInstance.get(`/reviews/target`, {
    params: { targetType, targetId },
  });
  return response.data;
},

};

export default reviewService;
